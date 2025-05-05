package kr.ac.dankook.CareerApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeywordResponse {
    private String id;
    private String type;
    private String jobType;
    private String subJobType;
    private String description;
    private String reasonThing;
    private String reason;
    private String solution;
}
