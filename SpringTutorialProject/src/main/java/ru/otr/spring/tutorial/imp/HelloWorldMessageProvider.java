package ru.otr.spring.tutorial.imp;

import ru.otr.spring.tutorial.domain.MessageProveder;

/**
 * Created by tartanov.mikhail on 07.04.2016.
 */
public class HelloWorldMessageProvider implements MessageProveder {
    public String getMessage() {
        return "Hello World";
    }
}
