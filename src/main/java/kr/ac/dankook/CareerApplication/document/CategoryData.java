package kr.ac.dankook.CareerApplication.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryData {

    private String type;
    private String subtype;
    private Map<String, Double> data;
}
