package test;

import annotations.*;
import model.request.Request;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Audi;
import test.bean.Person;
import test.component.ComponentTest;
import test.service.ServiceTest;

@Controller
public class Test2 {

    @Autowired(verbose = false)
    private Audi audi;

    @Autowired(verbose = true)
    private Person person;

    @Autowired(verbose = true)
    private ServiceTest serviceTest;

    @Autowired(verbose = false)
    private ComponentTest componentTest;

    @Path(path = "/getAudi")
    @GET
    public Response getCar(Request req) {
        System.out.println("Route hit: getAudi");
        Response response = new JsonResponse(audi);
        return response;
    }

    @Path(path = "/getPerson")
    @GET
    public Response getPerson(Request req) {
        System.out.println("Route hit: getPerson");
        Response response = new JsonResponse(person);
        return response;
    }

    @Path(path = "/getComponent")
    @GET
    public Response testComponent(Request req) {
        System.out.println("Route hit: getComponent");
        Response response = new JsonResponse(componentTest);
        return response;
    }

    @Path(path = "/postService")
    @POST
    public Response testService(Request req) {
        System.out.println("Route hit: postService");
        Response response = new JsonResponse(serviceTest);
        return response;
    }

    @Path(path = "/changePersonName")
    @POST
    public Response changePerson(Request req) {
        person.setName("Nikola");
        Response response = new JsonResponse(person);
        return response;
    }
}
