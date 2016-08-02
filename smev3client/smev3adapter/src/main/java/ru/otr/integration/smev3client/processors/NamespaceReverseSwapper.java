package ru.otr.integration.smev3client.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */
public class NamespaceReverseSwapper implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        if(body.isEmpty()) {
            exchange.getIn().setBody("The body is empty!");
        }
        else    {
            String result = body.replaceAll("urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1", "http://otr.ru/irs/services/message-exchange/types")
                    .replaceAll("urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1","http://otr.ru/irs/services/message-exchange/types/basic");
            exchange.getIn().setBody(result);}
    }
}
