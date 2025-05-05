package kr.ac.dankook.CareerApplication.document;


import kr.ac.dankook.CareerApplication.dto.request.CheckListFormRequest;
import kr.ac.dankook.CareerApplication.dto.request.CheckListResultRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "checklist_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistHistory {

    @Id
    private String id;
    private String memberId;
    private LocalDateTime createdAt;
    private List<CheckListResultRequest> checklist;
    private CheckListFormRequest forms;
}
