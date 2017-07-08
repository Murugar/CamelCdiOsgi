
package com.iqmsoft.karaf.camel.osgi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.component.properties.PropertiesComponent;

public class Config {

    @Produces
    @ApplicationScoped
    @Named("properties")
    PropertiesComponent properties() {
        PropertiesComponent component = new PropertiesComponent();
        component.setLocation("classpath:jms.properties");
        return component;
    }
}
