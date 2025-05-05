package kr.ac.dankook.CareerApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeywordListResponse {

    private List<KeywordResponse> keywordResponses;
    private int count;
}
