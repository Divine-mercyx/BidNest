package org.BidNest2.data.repositories;

import org.BidNest2.data.models.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends MongoRepository<Profile, String> {
}
