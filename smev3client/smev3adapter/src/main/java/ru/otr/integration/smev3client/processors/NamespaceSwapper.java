package ru.otr.integration.smev3client.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by tartanov.mikhail on 26.07.2016.
 */
@Component
public class NamespaceSwapper implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        /*if(body != null && !body.isEmpty())  {
            String result = body.replaceAll("http://otr.ru/irs/services/message-exchange/types", "urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1")
                    .replaceAll("http://otr.ru/irs/services/message-exchange/types/basic","urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1");
            exchange.getIn().setBody(result);}
        else throw new RuntimeException("Request body is empty!");*/
    }
}

