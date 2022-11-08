package test;

import annotations.di.Autowired;
import annotations.di.Controller;
import annotations.di.Qualifier;
import annotations.http.GET;
import annotations.http.Path;
import model.request.Request;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Car;

@Controller
public class Test3 {

    @Autowired(verbose = true)
//    @Qualifier(value = "opel")
    private Car emptyObject;

    @Path(path = "/getError")
    @GET
    public Response getError(Request request) {
        Response response = new JsonResponse(emptyObject);
        return response;
    }
}
