package kr.ac.dankook.CareerApplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LLMChat extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "llm_chat_section_id", nullable = false)
    private LLMChatSection llmChatSection;

    private String question;
    @Column(columnDefinition = "TEXT")
    private String answer;
}
