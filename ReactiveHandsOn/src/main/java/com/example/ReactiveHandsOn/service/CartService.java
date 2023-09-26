package com.example.ReactiveHandsOn.service;

import com.example.ReactiveHandsOn.entity.Cart;
import com.example.ReactiveHandsOn.repository.PersistentCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;

@Service
public class CartService {


    private final PersistentCartRepository persistentCartRepository;
    private  final Logger logger= LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(PersistentCartRepository persistentCartRepository) {
        this.persistentCartRepository = persistentCartRepository;
    }

//    public Mono<Cart> createCart(Cart cart)  {
//
//        Optional<Cart> existCartMono = persistentCartRepository.findByProfile_UniqueID(cart.getUserProfile().getAltCustId());
//        if(existCartMono.isPresent()){
//            Cart updatedCart=new Cart();
//            updatedCart.setLineItem(cart.getLineItem());
//            return persistentCartRepository.save(updatedCart);
//        }
//        else{
//             return  persistentCartRepository.save(cart);
//        }
//
//    }



    public Mono<Cart> createCartV2(Cart cart)  {
//        logger.info("before sleep in cart method in service");
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        logger.info("inside create cart method in service");

              persistentCartRepository.save(cart).subscribeOn(Schedulers.parallel()).subscribe();
        return Mono.just(cart) ;
    }

    @Async
    public void CreateCartServiceAsync(Cart cart){
        logger.info("inside create cart async method in service");
         persistentCartRepository.save(cart);
    }
    public CompletableFuture<Cart> createCartV3(Cart cart) {
        logger.info("inside create cart method in service");
        return CompletableFuture.supplyAsync(() -> persistentCartRepository.save(cart).block());
    }

}
