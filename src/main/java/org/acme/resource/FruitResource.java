package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.orm.Fruit;
import org.acme.service.FruitService;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;
@Produces("application/json")
@Consumes("application/json")
@Path("/")
public class FruitResource {

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

    @Inject
    EntityManager entityManager;

    @Inject
    FruitService fruitService;
    @GET
    @Path("fruits")
    public Fruit[] getDefault() {
        return get();
    }

    @GET
    @Path("{tenant}/fruits")
    public Fruit[] getTenant() {
        return get();
    }

    private Fruit[] get() {
        return fruitService.get();
    }

    @GET
    @Path("fruits/{id}")
    public Fruit getSingleDefault(Integer id) {
        return fruitService.findById(id);
    }

    @GET
    @Path("{tenant}/fruits/{id}")
    public Fruit getSingleTenant(Integer id) {
        return fruitService.findById(id);
    }



    @POST
    @Transactional
    @Path("fruits")
    public Response createDefault(Fruit fruit) {
        return fruitService.create(fruit);
    }

    @POST
    @Transactional
    @Path("{tenant}/fruits")
    public Response createTenant(Fruit fruit) {
        return fruitService.create(fruit);
    }



    @PUT
    @Path("fruits/{id}")
    @Transactional
    public Fruit updateDefault(Integer id, Fruit fruit) {
        return fruitService.update(id, fruit);
    }

    @PUT
    @Path("{tenant}/fruits/{id}")
    @Transactional
    public Fruit updateTenant(Integer id, Fruit fruit) {
        return fruitService.update(id, fruit);
    }



    @DELETE
    @Path("fruits/{id}")
    @Transactional
    public Response deleteDefault(Integer id) {
        return fruitService.delete(id);
    }

    @DELETE
    @Path("{tenant}/fruits/{id}")
    @Transactional
    public Response deleteTenant(Integer id) {
        return fruitService.delete(id);
    }



    @GET
    @Path("fruitsFindBy")
    public Response findByDefault(@RestQuery String type, @RestQuery String value) {
        return fruitService.findBy(type, value);
    }

    @GET
    @Path("{tenant}/fruitsFindBy")
    public Response findByTenant(@RestQuery String type, @RestQuery String value) {
        return fruitService.findBy(type, value);
    }




}
