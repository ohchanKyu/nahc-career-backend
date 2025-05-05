package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.document.ChatHistoryData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatHistoryDataRepository extends MongoRepository<ChatHistoryData,String> {
    List<ChatHistoryData> findBySessionId(String sessionId);
}
