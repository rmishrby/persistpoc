package com.example.ReactiveHandsOn.Router;

import com.example.ReactiveHandsOn.handler.PersistHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PersistRouter {


     Logger logger= LoggerFactory.getLogger(PersistRouter.class);
    @Bean
    public RouterFunction<ServerResponse> cartCaptureRoutes(PersistHandler persistHandler){
        logger.info("Inside router function");
        return RouterFunctions.route()
                .POST("/api/create",accept(MediaType.APPLICATION_JSON),persistHandler::processRequest)
                .build();
    }




        }
