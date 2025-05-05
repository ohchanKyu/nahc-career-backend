package kr.ac.dankook.CareerApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckListFormRequest {
    private String type;
    private String jobType;
    private String subJobType;
    private String reason;
    private String difficulty;
    private String riskLevel;
    private int workTime;
}
