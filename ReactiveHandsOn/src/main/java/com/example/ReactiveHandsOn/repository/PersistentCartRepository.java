package com.example.ReactiveHandsOn.repository;

import com.example.ReactiveHandsOn.entity.Cart;
import com.example.ReactiveHandsOn.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface PersistentCartRepository extends ReactiveMongoRepository<Cart,String> {
//    Optional<Cart> findByProfile_UniqueID(String uniqueID);
}
