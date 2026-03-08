package com.naveen.springboot.journal.repository;

import com.naveen.springboot.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

  User findByUserName(String userName);
}
