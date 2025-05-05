package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.entity.LLMChat;
import kr.ac.dankook.CareerApplication.entity.LLMChatSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LLMChatRepository extends JpaRepository<LLMChat, Long> {
    List<LLMChat> findByLlmChatSection(LLMChatSection section);
}
