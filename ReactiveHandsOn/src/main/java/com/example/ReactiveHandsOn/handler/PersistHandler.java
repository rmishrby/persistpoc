package com.example.ReactiveHandsOn.handler;

import com.example.ReactiveHandsOn.entity.Cart;
import com.example.ReactiveHandsOn.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.delay;

@Component
public class PersistHandler {

    @Autowired
    private CartService cartService;
    private final Logger logger = LoggerFactory.getLogger(PersistHandler.class);


    public Mono<ServerResponse> createCart(ServerRequest serverRequest) {
        logger.info("inside handler method");
//        createCartHandler(serverRequest);
//        logger.info("async call to create cart has been initiated");
//           Mono<Cart> cart=serverRequest.bodyToMono(Cart.class);
//          createCartHandler(serverRequest);
//        return Mono.defer(() -> {
//            logger.info("inside lambda function");
        return serverRequest.bodyToMono(Cart.class)
                .flatMap(cart -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return cartService.createCartV2(cart);
                })
                .flatMap(x -> ServerResponse.status(HttpStatus.CREATED).bodyValue(x));
    }

//        logger.info("after service method executed");
//        serverRequest.bodyToMono(Cart.class)
//                .flatMap(cart -> cartService.createCartV2(cart)).subscribeOn(Schedulers.parallel()).subscribe();
//        return  Mono.just("Request Acknowledgement").flatMap(x->ServerResponse.status(HttpStatus.CREATED).bodyValue(x));


    public Mono<ServerResponse> createCartV2(ServerRequest serverRequest) {
        logger.info("Inside handler method");

        return serverRequest.bodyToMono(Cart.class)
                .flatMap(cart -> Mono.defer(() -> cartService.createCartV2(cart)))
                .flatMap(savedCart -> {
                    logger.info("Async call to create cart has been completed");
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue("Successfully Created");
                })
                .onErrorResume(error -> {
                    logger.error("Error while creating cart: " + error.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error creating cart");
                });
    }


    public Mono<ServerResponse> createCartV4(ServerRequest serverRequest) {
        Mono<Cart> cartMono = serverRequest.bodyToMono(Cart.class);

        return cartMono.flatMap(cart -> {
            CompletableFuture<Cart> saveFuture = cartService.createCartV3(cart);

            return Mono.fromFuture(() -> saveFuture)
                    .flatMap(savedCart -> ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(savedCart))
                    .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Failed to save the cart: " + error.getMessage()));
        });
    }

    public Mono<Void> createCartV3(ServerRequest serverRequest) {
//        logger.info("Inside handler method");
//
//        // Send acknowledgment response immediately
//        Mono<ServerResponse> acknowledgmentResponse = ServerResponse.ok().bodyValue("Request received");
//
//        // Perform data operation asynchronously
//        Mono<ServerResponse> dataOperation = serverRequest.bodyToMono(Cart.class)
//                .flatMap(cart -> Mono.defer(() -> cartService.createCartV2(cart)))
//                .flatMap(savedCart -> {
//                    logger.info("Async call to create cart has been completed");
//                    return ServerResponse.status(HttpStatus.CREATED).bodyValue("Successfully Created");
//                })
//                .onErrorResume(error -> {
//                    logger.error("Error while creating cart: " + error.getMessage());
//                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error creating cart");
//                });
//
//        // Combine acknowledgment response and data operation using zipWith operator
//        return acknowledgmentResponse.zipWith(dataOperation).then();

        // Send acknowledgment response immediately
        Mono<ServerResponse> acknowledgmentResponse = ServerResponse.ok().bodyValue("Request received");

        // Perform data operation asynchronously
        Mono<ServerResponse> dataOperation = serverRequest.bodyToMono(Cart.class)
                .flatMap(cart -> Mono.defer(() -> cartService.createCartV2(cart)))
                .flatMap(savedCart -> {
                    logger.info("Async call to create cart has been completed");
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue("Successfully Created");
                })
                .onErrorResume(error -> {
                    logger.error("Error while creating cart: " + error.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error creating cart");
                });

        // Combine acknowledgment response and data operation using zipWith operator
        Mono<Void> result = acknowledgmentResponse.zipWith(dataOperation).then();

        // Return the result
        return result;

    }

    public Mono<ServerResponse> createCartV10(ServerRequest serverRequest) {
//        Mono<String> acknowledgment = Mono.just("Request received. Processing in progress...");
        logger.info("inside create cart v10");
        Mono<ServerResponse> acknowledgmentResponse = ServerResponse.ok().bodyValue("Request received");
        Mono<Cart> cartMono = serverRequest.bodyToMono(Cart.class);

        // Simulated parallel processing
        Mono<Cart> operation1 = cartMono.flatMap(cart -> cartService.createCartV2(cart)).subscribeOn(Schedulers.parallel());

        // Subscribe to the operations to trigger their execution, but don't block the acknowledgment
        operation1.subscribe();
//        operation2.subscribe();
        logger.info("before server response");
        // Return the acknowledgment immediately
        return acknowledgmentResponse;
    }


    public Mono<ServerResponse> processRequest(ServerRequest serverRequest) {
        // Acknowledgment
        Mono<ServerResponse> acknowledgment = Mono.just("Request received. Processing in progress...").flatMap(x -> ServerResponse.status(HttpStatus.ACCEPTED).bodyValue(x));

        // Simulated parallel processing
        Mono<Mono<String>> operation1 = simulateOperation(serverRequest).subscribeOn(Schedulers.parallel());
//        Mono<String> operation2 = simulateOperation("Operation 2").subscribeOn(Schedulers.parallel());

        // Subscribe to the operations to trigger their execution, but don't block the acknowledgment
        operation1.subscribe();
//        operation2.subscribe();

        // Return the acknowledgment immediately
        return acknowledgment;
    }

    private Mono<Mono<String>> simulateOperation(ServerRequest request) {
        return Mono.fromCallable(() -> {
            Thread.sleep(15000); // Simulate some processing time


            logger.info("inside thread sleep");

            Mono<Cart> monoCart = request.bodyToMono(Cart.class);
            return monoCart.flatMap(cart -> cartService.createCartV2(cart))
                    .doOnSuccess(result -> {
                        logger.info("after executing create service in thread");
                    })
                    .thenReturn("Done");
        });
    }
}

//    private Mono<String> simulateOperation(ServerRequest request) {
//        return Mono.fromCallable(() -> {
//            Thread.sleep(15000); // Simulate some processing time
//
//            logger.info("inside thread sleep");
//          Mono<Cart> monoCart=  request.bodyToMono(Cart.class);
//            monoCart.flatMap(cart -> cartService.createCartV2(cart));
//            logger.info("after executing create service in threa");
//            return "Done";
//        });
//    }
//}

//    public Mono<ServerResponse> processRequest(ServerRequest serverRequest) {
//        // Acknowledgment
//        Mono<ServerResponse> acknowledgment = Mono.just("Request received. Processing in progress...")
//                .flatMap(x -> ServerResponse.status(HttpStatus.ACCEPTED).bodyValue(x));
//
//        // Simulated parallel processing
//        Mono<Object> operation1 = simulateOperation(serverRequest)
//                .subscribeOn(Schedulers.parallel());
//
//        // Subscribe to the operation to trigger its execution
//        operation1.subscribe(cart -> {
//            // You can perform any post-processing or verification here
//            logger.info("Cart processed asynchronously: {}", cart);
//        });
//
//        // Return the acknowledgment immediately
//        return acknowledgment;
//    }
//
//    private Mono<Object> simulateOperation(ServerRequest request) {
//        return request.bodyToMono(Cart.class)
//                .flatMap(cart -> Mono.defer(() -> {
//                    // Simulate some processing time asynchronously
//                    return Mono.fromCallable(() -> {
//                        Thread.sleep(15000); // Simulate some processing time (asynchronously)
//                        logger.info("Inside thread sleep");
//                        return cartService.createCartV2(cart);
//                    }).subscribeOn(Schedulers.boundedElastic()); // Use elastic scheduler for asynchronous processing
//                }));
//    }
//}
//


//
//    @Async
//    public void createCartHandler(ServerRequest serverRequest){
//        logger.info("inside async function to persist cart");
//         serverRequest.bodyToMono(Cart.class)
////                .delayElement(Duration.ofSeconds(5))
//                .flatMap(cart -> cartService.CreateCartServiceAsync(cart));
//                .flatMap(cartData-> return ServerResponse.status(HttpStatus.CREATED).bodyValue(cartData,Cart.class));




