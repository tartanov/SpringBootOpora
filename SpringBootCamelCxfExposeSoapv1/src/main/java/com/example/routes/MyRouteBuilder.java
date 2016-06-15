package com.example.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by tartanov.mikhail on 15.06.2016.
 */
@Component
public class MyRouteBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        System.out.println("Hello from " + Thread.currentThread().getName());
        from("servlet:///hello").transform().constant("Hello from Camel!")
                .setExchangePattern(ExchangePattern.InOnly)
                .to("activemq:testQueueResponse2");
    }
}
