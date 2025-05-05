package kr.ac.dankook.CareerApplication.dto.response;

import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class RiskDiagnosisResultResponse {

    private List<String> riskKeywords;
    private List<DangerSituationData> similarCases;
    private Map<String, Double> riskScores;
}
