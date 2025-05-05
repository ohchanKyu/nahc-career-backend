package kr.ac.dankook.CareerApplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {

    @NotBlank(message = "Type is Required.")
    private String type;
    private String jobType;
    private String subJobType;
}
