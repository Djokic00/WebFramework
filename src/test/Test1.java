package test;

import annotations.*;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Car;

@Controller
public class Test1 {

    @Autowired(verbose = true)
    @Qualifier("audi")
    private Car audi;

    @Autowired(verbose = true)
    @Qualifier("mercedes")
    private Car mercedes;

    @Path(path = "/getCar")
    @GET
    public void get() {
        Response response = new JsonResponse(audi);
    }

    @Path(path = "/postCar")
    @POST
    public void post() {

    }
}
