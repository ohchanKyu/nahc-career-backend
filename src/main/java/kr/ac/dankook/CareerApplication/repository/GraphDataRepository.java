package kr.ac.dankook.CareerApplication.repository;

import kr.ac.dankook.CareerApplication.document.GraphData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphDataRepository extends MongoRepository<GraphData, String> {}
