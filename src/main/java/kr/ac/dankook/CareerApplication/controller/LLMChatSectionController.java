package kr.ac.dankook.CareerApplication.controller;

import kr.ac.dankook.CareerApplication.config.converter.MemberEntityConverter;
import kr.ac.dankook.CareerApplication.dto.response.ApiResponse;
import kr.ac.dankook.CareerApplication.dto.response.llm.LLMChatSectionResponse;
import kr.ac.dankook.CareerApplication.entity.Member;
import kr.ac.dankook.CareerApplication.service.LLMChatSectionService;
import kr.ac.dankook.CareerApplication.util.DecryptId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/llm")
public class LLMChatSectionController {

    private final LLMChatSectionService llmChatSectionService;
    private final MemberEntityConverter memberEntityConverter;

    @PostMapping("/chat-section/{memberId}/{title}")
    public ResponseEntity<ApiResponse<LLMChatSectionResponse>> saveNewChatSection(
            @PathVariable @DecryptId Long memberId,
            @PathVariable String title) {
        Member member = memberEntityConverter.getMemberByMemberId(memberId);
        return ResponseEntity.ok(new ApiResponse<>(200,
                llmChatSectionService.saveNewChatSectionProcess(member,title)));
    }

    @DeleteMapping("/chat-section/{chatSectionId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteChatSection(
            @PathVariable @DecryptId Long chatSectionId){
        llmChatSectionService.deleteChatSectionProcess(chatSectionId);
        return ResponseEntity.ok(new ApiResponse<>(200,true));
    }

    @PatchMapping("/chat-section/{chatSectionId}/{title}")
    public ResponseEntity<ApiResponse<Boolean>> updateChatSection(
        @PathVariable @DecryptId Long chatSectionId,
        @PathVariable String title
    ){
        return ResponseEntity.ok(new ApiResponse<>(200,
                llmChatSectionService.updateChatSectionProcess(chatSectionId,title)));
    }

    @GetMapping("/chat-sections/{memberId}")
    public ResponseEntity<ApiResponse<List<LLMChatSectionResponse>>> getAllSectionsByMember(
            @PathVariable @DecryptId Long memberId
    ){
        Member member = memberEntityConverter.getMemberByMemberId(memberId);
        return ResponseEntity.ok(new ApiResponse<>(200,
                llmChatSectionService.getAllChatSectionsByMemberProcess(member)));
    }
}
