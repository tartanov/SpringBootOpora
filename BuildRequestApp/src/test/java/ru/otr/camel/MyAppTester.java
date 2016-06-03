package ru.otr.camel;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tartanov.mikhail on 24.05.2016.
 */
public class MyAppTester extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] {"/META-INF/spring/camel-test-context.xml"});
    }
    @Test
    public void testContext()   {
        System.out.println("Hello");
        template.sendBody("activemq:testQueue","<p:pushDocumentStateRequest guid=\"123\" xmlns:p=\"http://ufos.otr.ru/ws/core/facade/document-life-cycle/v1_4\" xmlns:p1=\"http://ufos.otr.ru/ws/core/common/v1_2\" xmlns:p2=\"http://ufos.otr.ru/core/common/v1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <p:parameters>\n" +
                "    <p2:parameter name=\"token\" type=\"STRING\" value=\"\"/>\n" +
                "  </p:parameters>\n" +
                "</p:pushDocumentStateRequest>");
    }
}
