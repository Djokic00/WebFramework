package test;

import annotations.*;
import model.request.Request;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Car;

@Controller
public class Test1 {

    @Autowired(verbose = true)
    @Qualifier("audi")
    private Car audi;

    @Autowired(verbose = true)
    @Qualifier("astra")
    private Car opel;

    @Path(path = "/getCar")
    @GET
    public Response getCar(Request request) {
        Response response = new JsonResponse(audi);
        return response;
    }

    @Path(path = "/postCar")
    @POST
    public Response postCar(Request request) {
        Response response = new JsonResponse(opel);
        return response;
    }
}
