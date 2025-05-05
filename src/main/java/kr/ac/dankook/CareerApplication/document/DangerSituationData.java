package kr.ac.dankook.CareerApplication.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "danger_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DangerSituationData {

    @Id
    private String id;
    private String type;
    private String jobType;
    private String subJobType;
    private String description;
    private String reasonThing;
    private String reason;
    private String solution;
}
