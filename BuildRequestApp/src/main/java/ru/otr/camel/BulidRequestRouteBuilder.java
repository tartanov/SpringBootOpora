package ru.otr.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.cxf.common.message.CxfConstants;

/**
 * Created by tartanov.mikhail on 24.05.2016.
 */
public class BulidRequestRouteBuilder extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("ns3", "http://www.otr.ru/ufos/dlc/events");
        from("activemq:testQueue")
                .setHeader("docGUID", xpath("body/ns3:dlc-action-event/@document-guid").resultType(String.class).namespaces(ns))
                .setHeader(CxfConstants.OPERATION_NAME, constant("getAttachment"))

                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .to("velocity:META-INF/velocity/ListAttachmentsRequestPAYLOAD.vm")
                .to("cxf:bean:documentAttachmentsService")

                .to("activemq:testQueueResponse");
    }
}
