package kr.ac.dankook.CareerApplication.controller;

import kr.ac.dankook.CareerApplication.config.converter.LLMChatEntityConverter;
import kr.ac.dankook.CareerApplication.dto.request.llm.LLMChatRequest;
import kr.ac.dankook.CareerApplication.dto.response.ApiResponse;
import kr.ac.dankook.CareerApplication.dto.response.llm.LLMChatResponse;
import kr.ac.dankook.CareerApplication.entity.LLMChatSection;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ValidationException;
import kr.ac.dankook.CareerApplication.service.LLMChatService;
import kr.ac.dankook.CareerApplication.util.DecryptId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/llm/chat")
public class LLMChatController {

    private final LLMChatService llmChatService;
    private final LLMChatEntityConverter llmChatEntityConverter;

    @GetMapping("/{chatSectionId}")
    public ResponseEntity<ApiResponse<List<LLMChatResponse>>> getAllChats(
            @PathVariable @DecryptId Long chatSectionId) {
        LLMChatSection llmChatSection = llmChatEntityConverter.getLLMChatSectionBySectionId(chatSectionId);
        return ResponseEntity.ok(new ApiResponse<>(200,
                llmChatService.getAllChatHistoryProcess(llmChatSection)));
    }

    @PostMapping("/{chatSectionId}")
    public ResponseEntity<ApiResponse<LLMChatResponse>> saveNewChat(
            @PathVariable @DecryptId Long chatSectionId,
            @RequestBody LLMChatRequest llmChatRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            validateBindingResult(bindingResult);
        }
        LLMChatSection llmChatSection = llmChatEntityConverter.getLLMChatSectionBySectionId(chatSectionId);
        return ResponseEntity.ok(new ApiResponse<>(200,
                llmChatService.saveLLMChatProcess(llmChatSection, llmChatRequest)));
    }

    private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
            throw new ValidationException(ApiErrorCode.INVALID_REQUEST,errorMessages);
        }
    }
}
