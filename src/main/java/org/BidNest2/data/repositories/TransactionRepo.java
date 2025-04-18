package org.BidNest2.data.repositories;

import org.BidNest2.data.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends MongoRepository<Transaction, String> {
    List<Transaction> findAllBySellerId(String sellerId);
    List<Transaction> findAllByBuyerId(String buyerId);
    List<Transaction> findAllBySellerIdAndBuyerId(String sellerId, String buyerId);
    List<Transaction> findAllByTransactionDate(LocalDateTime transactionDate);
}
