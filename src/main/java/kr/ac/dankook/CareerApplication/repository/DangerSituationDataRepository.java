package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DangerSituationDataRepository extends MongoRepository<DangerSituationData,String> {

    @Query("{ '$or': [ " +
            "{ 'jobType': { $regex: ?0, $options: 'i' } }, " +
            "{ 'type': { $regex: ?0, $options: 'i' } }, " +
            "{ 'subJobType': { $regex: ?0, $options: 'i' } }, " +
            "{ 'reason': { $regex: ?0, $options: 'i' } }, " +
            "] }")
    List<DangerSituationData> searchByKeyword(String keyword);
    List<DangerSituationData> findByType(String type);
}
