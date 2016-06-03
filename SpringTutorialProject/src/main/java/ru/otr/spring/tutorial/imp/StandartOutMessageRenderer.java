package ru.otr.spring.tutorial.imp;

import ru.otr.spring.tutorial.domain.MessageProveder;
import ru.otr.spring.tutorial.domain.MessageRenderer;

/**
 * Created by tartanov.mikhail on 07.04.2016.
 */
public class StandartOutMessageRenderer implements MessageRenderer {
    private MessageProveder messageProveder;


    @Override
    public void setMessageProvider(MessageProveder provider) {
        this.messageProveder = provider;
    }
    @Override
    public MessageProveder getMessageProvider() {
        return null;
    }
    @Override
    public void render() {
        if(messageProveder == null) {
            throw new RuntimeException(
                    "You must set the property messageProvider of class:" + StandartOutMessageRenderer.class.getName());
        }
        System.out.println(messageProveder.getMessage());
    }
}
