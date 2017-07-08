
package com.iqmsoft.karaf.camel.osgi;

import javax.enterprise.event.Observes;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.management.event.CamelContextStartedEvent;

public class Producer {

    void sendMessage(@Observes CamelContextStartedEvent event, @Uri("sjms:sample.queue") ProducerTemplate producer) {
        producer.sendBodyAndHeader("Sample Message", "Sender", getClass().getSimpleName());
    }
}
