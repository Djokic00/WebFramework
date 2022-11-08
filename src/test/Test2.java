package test;

import annotations.di.Autowired;
import annotations.di.Controller;
import annotations.http.GET;
import annotations.http.POST;
import annotations.http.Path;
import model.request.Request;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Audi;
import test.bean.Person;
import test.component.ComponentTest;
import test.service.ServiceTest;

@Controller
public class Test2 {
    @Autowired(verbose = true)
    private Person person;

    @Autowired(verbose = true)
    private ServiceTest serviceTest;

    @Autowired(verbose = false)
    private ComponentTest componentTest;

    @Path(path = "/getPerson")
    @GET
    public Response getPerson(Request req) {
        Response response = new JsonResponse(person);
        return response;
    }

    @Path(path = "/getComponent")
    @GET
    public Response getComponent(Request req) {
        Response response = new JsonResponse(componentTest);
        return response;
    }

    @Path(path = "/postService")
    @POST
    public Response postService(Request req) {
        Response response = new JsonResponse(serviceTest);
        return response;
    }

    @Path(path = "/postPerson")
    @POST
    public Response postPerson(Request req) {
        person.setName("nikola");
        Response response = new JsonResponse(person);
        return response;
    }
}
