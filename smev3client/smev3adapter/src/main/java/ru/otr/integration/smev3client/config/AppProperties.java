package ru.otr.integration.smev3client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */

@Component
@ConfigurationProperties(prefix = "dev")
public class AppProperties {
    public String namespace;

    public List<String> operations;

    public List<String> getOperations() {
        return operations;
    }

    //TODO make collection immutable
    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
