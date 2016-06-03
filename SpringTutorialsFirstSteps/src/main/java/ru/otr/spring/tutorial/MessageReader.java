package ru.otr.spring.tutorial;

import java.io.IOException;

/**
 * Created by tartanov.mikhail on 02.06.2016.
 */
public interface MessageReader {
    String getMessage() throws IOException;
}
