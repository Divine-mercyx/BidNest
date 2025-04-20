package org.BidNest2.data.repositories;

import org.BidNest2.data.models.Subscribe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepo extends MongoRepository<Subscribe, String> {
    List<Subscribe> findAllBySubscribedToId(String subscribedToId);
    void deleteBySubscribedToIdAndSubscriberId(String subscribedToId, String id);
}
