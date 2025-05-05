package kr.ac.dankook.CareerApplication.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chat_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryData {

    @Id
    private String id;
    @Field("session_id")
    private String sessionId;
}
