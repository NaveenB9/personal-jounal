package com.naveen.springboot.journal.repository;


import com.naveen.springboot.journal.entity.JournalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends MongoRepository<JournalEntity, ObjectId> {

}
