package kr.ac.dankook.CareerApplication.dto.request.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class LLMChatRequest {
    private String question;
    private String answer;
}
