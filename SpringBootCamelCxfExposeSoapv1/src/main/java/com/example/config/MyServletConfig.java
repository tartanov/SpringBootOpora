package com.example.config;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.jolokia.http.AgentServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tartanov.mikhail on 15.06.2016.
 */

@Configuration
public class MyServletConfig {

    private static final String CAMEL_URL_MAPPING = "/camel/*";
    private static final String CAMEL_SERVLET_NAME = "CamelServlet";

    private static final String JOLOKIA_URL_MAPPING = "/jolokia/*";
    private static final String JOLOKIA_SERVLET_NAME = "jolokia-agent";

    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), CAMEL_URL_MAPPING);
        registration.setName(CAMEL_SERVLET_NAME);
        return registration;
    }

    @Bean
    public ServletRegistrationBean jolokiaServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new AgentServlet(), JOLOKIA_URL_MAPPING);
        registration.setName(JOLOKIA_SERVLET_NAME);
        return registration;
    }
}
