package ru.otr.spring.tutorial.domain;

/**
 * Created by tartanov.mikhail on 07.04.2016.
 */
public interface MessageRenderer {
    void setMessageProvider(MessageProveder provider);
    MessageProveder getMessageProvider();
    void render();
}
