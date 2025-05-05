package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.document.ChecklistHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChecklistHistoryRepository extends MongoRepository<ChecklistHistory,String> {
    List<ChecklistHistory> findByMemberId(String memberId);
}
