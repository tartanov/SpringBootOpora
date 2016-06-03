package ru.otr.spring.tutorial;

import java.io.IOException;

/**
 * Created by tartanov.mikhail on 02.06.2016.
 */
public class HtmlMessageFormatter extends MessageFormatter {
    @Override
    public String formatMessage() throws IOException {
        return "<message>"+messageReader.getMessage() +"</message>";
    }
}
