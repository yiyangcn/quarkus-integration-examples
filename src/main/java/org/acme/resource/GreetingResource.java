package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//@Path("/hello")
@Path("/hello")
@Produces("application/json")
@Consumes("application/json")
public class GreetingResource {

    @GET
    @Path("hello")
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("helloworld")
    public String helloWorld() {
        return "Hello world!";
    }
}
