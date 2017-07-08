package com.iqmsoft.karaf.camel.osgi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.tinybundles.core.TinyBundles;

import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.ACTIVEMQ;
import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.CAMEL_CDI;
import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.CAMEL_COMMANDS;
import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.CAMEL_SJMS;
import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.KARAF;
import static com.iqmsoft.karaf.camel.osgi.PaxExamOptions.PAX_CDI_IMPL;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class CdiOsgiIT {

    @Inject
    private CamelContext context;

    @Inject
    private SessionFactory sessionFactory;

    @Configuration
    public Option[] config() throws IOException {
        return options(
            KARAF.option(),
            CAMEL_COMMANDS.option(),
            PAX_CDI_IMPL.option(),
            CAMEL_CDI.option(),
            CAMEL_SJMS.option(),
            ACTIVEMQ.option(),
            streamBundle(
                TinyBundles.bundle()
                    .read(
                        Files.newInputStream(
                            Paths.get("target")
                                .resolve("CamelCdiOsgi.jar")))
                    .build()),
            when(false)
                .useOptions(
                    debugConfiguration("5005", true))
        );
    }

    @Test
    public void testRouteStatus() {
        assertThat("Route status is incorrect!",
            context.getRouteStatus("consumer-route"), equalTo(ServiceStatus.Started));
    }

    @Test
    public void testExchangesCompleted() throws Exception {
        ManagedRouteMBean route = context.getManagedRoute(context.getRoute("consumer-route").getId(), ManagedRouteMBean.class);
        assertThat("Number of exchanges completed is incorrect!",
            route.getExchangesCompleted(), equalTo(1L));
    }

    @Test
    public void testExecuteCommands() throws Exception {
        Session session = sessionFactory.create(System.in, System.out, System.err);
        session.execute("camel:context-list");
        session.execute("camel:route-list");
        session.execute("camel:route-info consumer-route");
    }
}
