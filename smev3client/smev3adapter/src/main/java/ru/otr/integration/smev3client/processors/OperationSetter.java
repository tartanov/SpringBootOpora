package ru.otr.integration.smev3client.processors;

import com.google.common.collect.ImmutableList;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;

import java.util.Optional;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */
@Component
public class OperationSetter implements Processor {
    @Autowired
    private AppProperties appProperties;

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String body1 = exchange.getIn().getBody(String.class);
        //lazy search oob
        String targetOperation = appProperties.operations.stream().filter(body::contains).findFirst().orElseThrow(()-> new RuntimeException("Unable to find target operation in request to SMEV"));
        exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, targetOperation);
        exchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, appProperties.namespace);
        }
    }
