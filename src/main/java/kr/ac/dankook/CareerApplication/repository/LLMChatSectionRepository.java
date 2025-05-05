package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.entity.LLMChatSection;
import kr.ac.dankook.CareerApplication.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LLMChatSectionRepository extends JpaRepository<LLMChatSection,Long> {
    List<LLMChatSection> findByMember(Member member);
}
