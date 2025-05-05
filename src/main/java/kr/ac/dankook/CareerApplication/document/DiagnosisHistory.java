package kr.ac.dankook.CareerApplication.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "diagnosis_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisHistory {

    @Id
    private String id;
    private String memberId;
    private List<String> riskKeywords;
    private List<String> riskScores;
    private List<DangerSituationData> similarCases;
    private String content;
    private String diagnosis;
}
