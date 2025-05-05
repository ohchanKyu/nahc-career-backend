package kr.ac.dankook.CareerApplication.dto.request;

import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DiagnosisSaveRequest {
    private List<String> riskKeywords;
    private List<String> riskScores;
    private List<DangerSituationData> similarCases;
    private String content;
    private String diagnosis;
}
