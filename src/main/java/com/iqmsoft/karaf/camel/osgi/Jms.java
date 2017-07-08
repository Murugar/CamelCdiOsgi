
package com.iqmsoft.karaf.camel.osgi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.PropertyInject;
import org.apache.camel.component.sjms.SjmsComponent;

public class Jms {

    @PropertyInject("jms.maxConnections")
    int maxConnections;

    @Produces
    @Named("sjms")
    @ApplicationScoped
    SjmsComponent sjms() {
        SjmsComponent component = new SjmsComponent();
        component.setConnectionFactory
        (new ActiveMQConnectionFactory("vm://broker?broker.persistent=false&broker.useShutdownHook=false&broker.useJmx=false"));
        component.setConnectionCount(maxConnections);
        return component;
    }
}