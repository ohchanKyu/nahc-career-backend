package kr.ac.dankook.CareerApplication.service;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import kr.ac.dankook.CareerApplication.dto.request.FilterRequest;
import kr.ac.dankook.CareerApplication.dto.response.KeywordResponse;
import kr.ac.dankook.CareerApplication.repository.DangerSituationDataRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DangerSituationService {

    private final DangerSituationDataRepository dangerSituationDataRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public List<DangerSituationData> getAllDangerSituationProcess() {
        return dangerSituationDataRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<String> getAllTypesProcess() {
        List<String> categoryList = new ArrayList<>();
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("danger_history");
        DistinctIterable<String> distinctIterable = mongoCollection.distinct("type",String.class);
        for (String category : distinctIterable) {
            categoryList.add(category);
        }
        return categoryList;
    }

    @Transactional(readOnly = true)
    public List<String> getJobTypesByTypeProcess(String type) {

        List<String> jobTypeList = new ArrayList<>();
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("danger_history");
        DistinctIterable<String> distinctIterable = mongoCollection.distinct("jobType",
                Filters.eq("type", type),
                String.class);
        for (String jobType : distinctIterable) {
            jobTypeList.add(jobType);
        }
        return jobTypeList;
    }
    public List<DangerSituationData> findByFilterProcess(FilterRequest filterRequest){

        String type = filterRequest.getType();
        String jobType = filterRequest.getJobType();
        String subJobType = filterRequest.getSubJobType();

        List<DangerSituationData> situations = dangerSituationDataRepository.
                findByType(type);

        if (jobType != null && !jobType.isEmpty()){
            situations =  situations.stream()
                    .filter(situation -> situation.getJobType()
                            .equals(jobType))
                    .toList();
        }
        if (subJobType != null && !subJobType.isEmpty()){
            situations =  situations.stream()
                    .filter(situation -> situation.getSubJobType()
                            .equals(subJobType))
                    .toList();
        }
        return situations;
    }

    @Transactional(readOnly = true)
    public List<String> getSubJobTypesByJobTypeProcess(String jobType) {
        List<String> subJobTypeList = new ArrayList<>();
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("danger_history");
        DistinctIterable<String> distinctIterable = mongoCollection.distinct("subJobType",
                Filters.eq("jobType", jobType),
                String.class);
        for (String subJobType:distinctIterable) {
            subJobTypeList.add(subJobType);
        }
        return subJobTypeList;
    }

    public List<KeywordResponse> findByKeywordProcess(String keyword){

        List<KeywordResponse> keywordResponses = new ArrayList<>();
        List<DangerSituationData> dangerSituations = dangerSituationDataRepository.searchByKeyword(keyword);
        for(DangerSituationData dangerSituation : dangerSituations){
            keywordResponses.add(KeywordResponse.builder()
                    .id(dangerSituation.getId())
                    .type(dangerSituation.getType())
                    .jobType(dangerSituation.getJobType())
                    .subJobType(dangerSituation.getSubJobType())
                    .description(dangerSituation.getDescription())
                    .reason(dangerSituation.getReason())
                    .reasonThing(dangerSituation.getReasonThing())
                    .solution(dangerSituation.getSolution())
                    .build()
            );
        }
        return keywordResponses;
    }
}
