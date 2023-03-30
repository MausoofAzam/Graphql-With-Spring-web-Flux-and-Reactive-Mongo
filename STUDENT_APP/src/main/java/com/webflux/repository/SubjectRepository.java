package com.webflux.repository;

import com.webflux.entity.Subject;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends ReactiveMongoRepository<Subject,String > {
}
