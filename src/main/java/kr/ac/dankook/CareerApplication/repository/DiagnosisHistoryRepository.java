package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.document.DiagnosisHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiagnosisHistoryRepository extends MongoRepository<DiagnosisHistory,String> {
    List<DiagnosisHistory> findByMemberId(String memberId);
}
