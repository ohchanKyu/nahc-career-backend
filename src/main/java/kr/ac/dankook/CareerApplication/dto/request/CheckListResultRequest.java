package kr.ac.dankook.CareerApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckListResultRequest {
    private String stage;
    private String content;
    private String importance;
}
