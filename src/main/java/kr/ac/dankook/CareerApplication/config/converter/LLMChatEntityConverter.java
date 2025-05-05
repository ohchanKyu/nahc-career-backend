package kr.ac.dankook.CareerApplication.config.converter;

import kr.ac.dankook.CareerApplication.entity.LLMChatSection;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ApiException;
import kr.ac.dankook.CareerApplication.repository.LLMChatSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LLMChatEntityConverter {

    private final LLMChatSectionRepository llmChatSectionRepository;

    private LLMChatSection getChatSectionRepository(Long sectionId){
        Optional<LLMChatSection> target = llmChatSectionRepository.findById(sectionId);
        if(target.isPresent()){
            return target.get();
        }
        throw new ApiException(ApiErrorCode.CHAT_SECTION_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public LLMChatSection getLLMChatSectionBySectionId(Long sectionId){
        return getChatSectionRepository(sectionId);
    }

}
