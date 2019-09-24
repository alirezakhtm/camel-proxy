package com.khtm.demo.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.component.netty.http.NettyHttpComponent;
import org.apache.camel.component.rest.RestComponent;
import org.apache.camel.component.restlet.RestletComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class MainApplication {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addComponent("restlet", new RestletComponent());
        context.addComponent("http", new HttpComponent());
        context.addComponent("rest", new RestComponent());
        context.addComponent("netty-http", new NettyHttpComponent());
        context.addRoutes(new CustomeRestRoute());
        context.start();
    }
}
