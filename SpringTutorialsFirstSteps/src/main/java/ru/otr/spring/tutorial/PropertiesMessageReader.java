package ru.otr.spring.tutorial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tartanov.mikhail on 02.06.2016.
 */
public class PropertiesMessageReader implements MessageReader {
    @Override
    public String getMessage() throws IOException {
        Properties prop = new Properties();
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties"))   {
            prop.load(in);
        }
        return  prop.getProperty("message");
    }
}
