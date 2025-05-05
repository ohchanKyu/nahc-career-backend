package kr.ac.dankook.CareerApplication.service;

import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import kr.ac.dankook.CareerApplication.document.DiagnosisHistory;
import kr.ac.dankook.CareerApplication.dto.request.DiagnosisSaveRequest;
import kr.ac.dankook.CareerApplication.dto.response.RiskDiagnosisResultResponse;
import kr.ac.dankook.CareerApplication.repository.DangerSituationDataRepository;
import kr.ac.dankook.CareerApplication.repository.DiagnosisHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiskDiagnosisService {

    private final DangerSituationDataRepository dangerSituationDataRepository;
    private final DiagnosisHistoryRepository diagnosisHistoryRepository;

    @Transactional(readOnly = true)
    public List<DiagnosisHistory> getAllDiagnosisResultProcess(String memberId){
        return diagnosisHistoryRepository.findByMemberId(memberId);
    }

    @Transactional
    public boolean deleteDiagnosisResultProcess(String diagnosisResultId){
        if (diagnosisHistoryRepository.existsById(diagnosisResultId)){
            diagnosisHistoryRepository.deleteById(diagnosisResultId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean saveDiagnosisProcess(String memberId, DiagnosisSaveRequest diagnosisSaveRequest){
        DiagnosisHistory diagnosisHistory = DiagnosisHistory.builder()
                .diagnosis(diagnosisSaveRequest.getDiagnosis())
                .content(diagnosisSaveRequest.getContent())
                .riskScores(diagnosisSaveRequest.getRiskScores())
                .riskKeywords(diagnosisSaveRequest.getRiskKeywords())
                .similarCases(diagnosisSaveRequest.getSimilarCases())
                .memberId(memberId).build();
        diagnosisHistoryRepository.save(diagnosisHistory);
        return true;
    }


    public RiskDiagnosisResultResponse fullDiagnosisProcess(String jobType, String description) {

        List<String> keywords = diagnoseRiskFactorsProcess(jobType);
        List<DangerSituationData> similarCases = recommendSimilarCasesProcess(jobType,description);
        Map<String, Double> riskScores = calculateRiskScoresProcess(jobType);

        return RiskDiagnosisResultResponse.builder()
                .riskKeywords(keywords)
                .similarCases(similarCases)
                .riskScores(riskScores).build();
    }

    public List<String> diagnoseRiskFactorsProcess(String jobType) {

        List<DangerSituationData> cases = dangerSituationDataRepository.findByType(jobType);

        List<String> documents = cases.stream()
                .map(DangerSituationData::getDescription)
                .collect(Collectors.toList());

        Map<String, Double> tfIdfScores = calculateTfIdf(documents);

        return tfIdfScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<DangerSituationData> recommendSimilarCasesProcess(String jobType,String description) {

        List<DangerSituationData> allCases = dangerSituationDataRepository
                .findByType(jobType);

        Map<DangerSituationData, Double> similarityScores = new HashMap<>();
        for (DangerSituationData accCase : allCases) {
            double similarity = calculateCosineSimilarity(description, accCase.getDescription());
            similarityScores.put(accCase, similarity);
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<DangerSituationData, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Double> calculateRiskScoresProcess(String jobType) {

        List<DangerSituationData> cases = dangerSituationDataRepository.findByType(jobType);

        Map<String, Integer> keywordCount = new HashMap<>();
        for (DangerSituationData acc : cases) {
            String[] words = acc.getDescription().split("\\s+");
            for (String word : words) {
                word = word.trim();
                if (word.length() <= 1) continue;
                keywordCount.put(word, keywordCount.getOrDefault(word, 0) + 1);
            }
        }
        keywordCount.entrySet().removeIf(entry -> entry.getValue() == 0);
        List<Map.Entry<String, Integer>> topKeywords = keywordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();
        if (topKeywords.isEmpty()) {
            return new LinkedHashMap<>();
        }
        int totalTopCount = topKeywords.stream()
                .mapToInt(Map.Entry::getValue)
                .sum();
        if (totalTopCount == 0) {
            return new LinkedHashMap<>();
        }

        Map<String, Double> riskScores = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : topKeywords) {
            double percent = (double) entry.getValue() / totalTopCount * 100;
            riskScores.put(entry.getKey(), Math.round(percent * 10) / 10.0);
        }
        return riskScores;
    }

    private Map<String, Double> calculateTfIdf(List<String> docs) {

        Map<String, Double> tfIdf = new HashMap<>();
        Map<String, Integer> docCount = new HashMap<>();
        int totalDocs = docs.size();

        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "중", "위해", "하던", "작업", "작업을", "떨어져", "및", "대한", "통한",
                "으로", "에서", "하는", "한", "그", "및", "것", "아래", "잃고", "있던",
                "중심을", "재해자", "지상", "신축공사", "하여", "하고", "있고",
                "설치된", "시공하는", "해체작업", "발생한", "콘크리트", "OO건설", "OO철거공사",
                "OO건설(주)", "(주)", "(유)", "건설", "철거", "공사", "이용해"
        ));

        for (String doc : docs) {
            Set<String> seen = new HashSet<>();
            String[] words = doc.split("\\s+");

            for (String word : words) {
                word = word.trim();
                word = preprocessWord(word);

                if (word.length() <= 1) continue;
                if (stopWords.contains(word)) continue;
                if (word.matches(".*\\d.*")) continue;
                if (word.length() == 2) continue;

                tfIdf.put(word, tfIdf.getOrDefault(word, 0.0) + 1);
                if (!seen.contains(word)) {
                    docCount.put(word, docCount.getOrDefault(word, 0) + 1);
                    seen.add(word);
                }
            }
        }

        for (String word : tfIdf.keySet()) {
            double tf = tfIdf.get(word);
            double idf = Math.log((double) totalDocs / (docCount.getOrDefault(word, 1) + 1));
            tfIdf.put(word, tf * idf);
        }
        return tfIdf;
    }

    private String preprocessWord(String word) {
        word = word.replaceAll("\\(.*?\\)", "");
        word = word.replaceAll("OO", "");
        word = word.replaceAll("(을|를|에서|하여|하고|있던|된|하는|가|이|의|으로|로|한|에)$", ""); // 조사 제거
        return word;
    }

    private double calculateCosineSimilarity(String doc1, String doc2) {

        Map<String, Integer> freq1 = wordFreq(doc1);
        Map<String, Integer> freq2 = wordFreq(doc2);

        Set<String> words = new HashSet<>();
        words.addAll(freq1.keySet());
        words.addAll(freq2.keySet());

        int dotProduct = 0;
        int magnitude1 = 0;
        int magnitude2 = 0;

        for (String word : words) {
            int x = freq1.getOrDefault(word, 0);
            int y = freq2.getOrDefault(word, 0);
            dotProduct += x * y;
            magnitude1 += x * x;
            magnitude2 += y * y;
        }
        if (magnitude1 == 0 || magnitude2 == 0) return 0.0;
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }

    private Map<String, Integer> wordFreq(String text) {
        Map<String, Integer> freq = new HashMap<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            freq.put(word, freq.getOrDefault(word, 0) + 1);
        }
        return freq;
    }
}
