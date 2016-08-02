package ru.otr.integration.smev3client.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;
import ru.otr.integration.smev3client.processors.NamespaceSwapper;
import ru.otr.integration.smev3client.processors.OperationSetter;

/**
 * Created by tartanov.mikhail on 26.07.2016.
 */
@Component
public class ProxyRouteBuilder extends RouteBuilder {
    @Autowired
    CamelContext camelContext;



    @Override
    public void configure() throws Exception {
        camelContext.setStreamCaching(true);


        from("servlet:///hello")
                //from("file://D:\\IdeaProjects\\smev3client\\smev3adapter\\src\\main\\resources\\velocity\\GetIncomingQueueStatisticsRequest.vm")
                // .convertBodyTo(String.class, "utf-8")
                 .bean("namespaceSwapper")
       /* from("timer://simpleTimer?period=1000")
                .setBody(simple(" <ns:GetIncomingQueueStatisticsRequest xmlns:ns=\"urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1\" xmlns:ns1=\"urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1\">>\n" +
                        "         <!--Optional:-->\n" +
                        "         <ns:NodeID>?</ns:NodeID>\n" +
                        "         <ns1:Timestamp Id=\"?\">?</ns1:Timestamp>\n" +
                        "         <!--Optional:-->\n" +
                        "         <ns:CallerInformationSystemSignature>\n" +
                        "            <!--You may enter ANY elements at this point-->\n" +
                        "         </ns:CallerInformationSystemSignature>\n" +
                        "      </ns:GetIncomingQueueStatisticsRequest>"))*/
                .bean("operationSetter")
                .to("cxf:bean:smevMessageExchangeService")
                .to("file:C:\\out\\MyFile.txt")
        ;
    }
}
