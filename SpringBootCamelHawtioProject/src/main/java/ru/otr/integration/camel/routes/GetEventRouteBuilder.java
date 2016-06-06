package ru.otr.integration.camel.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.camel.routes.strategy.AttachmentsAggregationStrategy;
import ru.otr.integration.util.ZipHelper;

/**
 * Created by tartanov.mikhail on 03.06.2016.
 */

@Component
public class GetEventRouteBuilder extends RouteBuilder{

    @Autowired
    CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        camelContext.setStreamCaching(true);

        Namespaces ns = new Namespaces("ns3", "http://www.otr.ru/ufos/dlc/events");
        Namespaces nsa = new Namespaces("nsa", "http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1");
        from("activemq:testQueue").routeId("AttachSplitter")
                .setHeader("docGUID", xpath("body/ns3:dlc-action-event/@document-guid").resultType(String.class).namespaces(ns))
                .setHeader(CxfConstants.OPERATION_NAME, constant("listAttachments"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .to("velocity:velocity/ListAttachmentsRequest.vm")
                .to("cxf:bean:documentAttachmentsService")
                .setHeader("attachmentCount",xpath("count(//nsa:attachment)").resultType(String.class).namespaces(nsa))
                .split(xpath("//nsa:attachment").namespaces(nsa))
                .to("direct:ap");

        from("direct:ap").routeId("AttachmentProcessor")
                .setHeader("attachGUID", xpath("//@guid").resultType(String.class))
                .setHeader(CxfConstants.OPERATION_NAME, constant("getAttachment"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .setHeader("entryName",xpath("//@file-name").resultType(String.class))
                .to("velocity:velocity/GetAttachmentsRequest.vm")
                .to("cxf:bean:documentAttachmentsService")
                .setHeader("content",xpath("//nsa:content").resultType(String.class).namespaces(nsa))
                .setHeader("sigContent",xpath("//nsa:cms-signed-data").resultType(String.class).namespaces(nsa))
                .to("bean:zipHelper?method=createFile(${header.docGUID}, ${header.entryName}, ${header.content}, ${header.sigContent})")
                .to("direct:aa");

        from("direct:aa").routeId("AttachmentAggregator")
                .aggregate(header("docGUID"), new AttachmentsAggregationStrategy()).completionSize(header("attachmentCount"))
                .to("bean:zipHelper?method=getFileInBase64(${header.docGUID})")
                .to("velocity:velocity/ManageAttachmentsRequest.vm")
                .setHeader(CxfConstants.OPERATION_NAME, constant("manageAttachments"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.otr.ru/ufos/ws/core/facade/attachment/v1_1"))
                .removeHeader("content").removeHeader("sigContent")
                .to("cxf:bean:documentAttachmentsService")
                .to("activemq:testQueueResponse1");
    }
}

