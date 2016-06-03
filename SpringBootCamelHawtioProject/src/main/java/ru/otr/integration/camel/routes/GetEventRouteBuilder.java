package ru.otr.integration.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.stereotype.Component;

/**
 * Created by tartanov.mikhail on 03.06.2016.
 */

@Component
public class GetEventRouteBuilder extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("ns3", "http://www.otr.ru/ufos/dlc/events");
        Namespaces nsa = new Namespaces("nsa", "http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1");
        from("activemq:testQueue")
                .setHeader("docGUID", xpath("body/ns3:dlc-action-event/@document-guid").resultType(String.class).namespaces(ns))
                .setHeader(CxfConstants.OPERATION_NAME, constant("listAttachments"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .to("velocity:velocity/ListAttachmentsRequest.vm")
                .to("cxf:bean:documentAttachmentsService")
                .split(xpath("//nsa:attachment").namespaces(nsa))
                .to("direct:attachGetter");

        from("direct:attachGetter")
                .setHeader("attachGUID", xpath("body/ns3:dlc-action-event/@document-guid").resultType(String.class).namespaces(ns))
                .setHeader(CxfConstants.OPERATION_NAME, constant("getAttachment"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .to("velocity:velocity/GetAttachmentsRequest.vm")
                .to("cxf:bean:documentAttachmentsService")
                .to("direct:aggregator");

        from("direct:aggregator")
                .aggregate(header("docGUID"), new AttachmentsAggregationStrategy()).completionSize(3)
                .to("activemq:testQueueResponse1");
        // .to("activemq:testQueueResponse1");

    }
}

