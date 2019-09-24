package com.khtm.demo.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingDefinition;
import org.apache.camel.model.rest.RestBindingMode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomeRestRoute extends RouteBuilder {

    private JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(User.class);

    public void configure() throws Exception {
        restConfiguration("rest-api")
                .component("restlet")
                .host("localhost")
                .port(9090)
//                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("pretty-print", "true");

        rest("/user")
                .produces("application/json").consumes("application/json")
                .get("/sample").to("direct:user-sample")
                .get("/echo").to("direct:user-echo");

        from("direct:user-sample")
                .id("camle :: user :: sample")
                .setHeader(Exchange.HTTP_METHOD, simple("GET"))
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                .to("netty-http:http://localhost:8080/user-sample")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("camle :: user :: sample :: 001 " + exchange.getIn().getBody(String.class));
                    }
                });

        from("direct:user-echo")
                .id("camel :: user :: echo")
                .process(new TimeProcessor("camel :: user :: echo called :: "))
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        User user = new User();
                        user.setFname("alireza");
                        user.setLname("khatami doost");
                        user.setId(2L);
                        exchange.getIn().setBody(user);
                    }
                })
                .marshal(jacksonDataFormat)
                .to("netty-http:http://localhost:8080/user-echo")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received response from service :: " + exchange.getIn().getBody(String.class));
                    }
                })
                .process(new TimeProcessor("camel :: user :: echo finish :: "));
    }



    private class TimeProcessor implements Processor{

        private String tag;

        public TimeProcessor(String tag){
            this.tag = tag;
        }

        public void process(Exchange exchange) throws Exception {
            printTime();
        }

        private void printTime() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = simpleDateFormat.format(new Date().getTime());
            System.out.println(tag + " ::: " + time);
        }
    }
}
