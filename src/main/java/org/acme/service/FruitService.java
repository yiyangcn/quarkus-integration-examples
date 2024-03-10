package org.acme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.orm.Fruit;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class FruitService {
    private static final Logger LOGGER = Logger.getLogger(FruitService.class.getName());

    @Inject
    EntityManager entityManager;

    public Fruit[] get() {
        return entityManager.createNamedQuery("Fruits.findAll", Fruit.class)
                .getResultList().toArray(new Fruit[0]);
    }

    public Fruit findById(Integer id) {
        Fruit entity = entityManager.find(Fruit.class, id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    public Response create(Fruit fruit) {
        if (fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        LOGGER.debugv("Create {0}", fruit.getName());
        entityManager.persist(fruit);
        return Response.ok(fruit).status(201).build();
    }

    public Fruit update(Integer id, Fruit fruit) {
        if (fruit.getName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        Fruit entity = entityManager.find(Fruit.class, id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.setName(fruit.getName());

        LOGGER.debugv("Update #{0} {1}", fruit.getId(), fruit.getName());

        return entity;
    }

    public Response delete(Integer id) {
        Fruit fruit = entityManager.getReference(Fruit.class, id);
        if (fruit == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        LOGGER.debugv("Delete #{0} {1}", fruit.getId(), fruit.getName());
        entityManager.remove(fruit);
        return Response.status(204).build();
    }

    public Response findBy(String type, String value) {
        if (!"name".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Currently only 'fruitsFindBy?type=name' is supported");
        }
        List<Fruit> list = entityManager.createNamedQuery("Fruits.findByName", Fruit.class).setParameter("name", value).getResultList();
        if (list.size() == 0) {
            return Response.status(404).build();
        }
        Fruit fruit = list.get(0);
        return Response.status(200).entity(fruit).build();
    }

//    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
