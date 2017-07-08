
package com.iqmsoft.karaf.camel.osgi;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

@ContextName("osgi-example")
public class Consumer extends RouteBuilder {

    @Override
    public void configure() {
        from("sjms:sample.queue")
            .routeId("consumer-route")
            .log("Received Message [${body}] from [${header.Sender}]");
    }
}
