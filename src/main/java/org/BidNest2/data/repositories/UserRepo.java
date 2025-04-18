package org.BidNest2.data.repositories;

import org.BidNest2.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
