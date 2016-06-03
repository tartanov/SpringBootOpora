package ru.otr.spring.tutorial;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by tartanov.mikhail on 02.06.2016.
 */
public abstract class MessageFormatter {
    public MessageReader getMessageReader() {
        return messageReader;
    }
    public void setMessageReader(MessageReader messageReader) {
        this.messageReader = messageReader;
    }
    @Autowired
    protected MessageReader messageReader;
    abstract String formatMessage() throws IOException;
}
