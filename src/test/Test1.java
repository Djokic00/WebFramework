package test;

import annotations.di.Autowired;
import annotations.di.Controller;
import annotations.di.Qualifier;
import annotations.http.GET;
import annotations.http.POST;
import annotations.http.Path;
import model.request.Request;
import model.response.JsonResponse;
import model.response.Response;
import test.bean.Car;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Test1 {

    @Autowired(verbose = true)
    @Qualifier("audi")
    private Car audi;

    @Autowired(verbose = true)
    @Qualifier("opel")
    private Car opel;

    @Path(path = "/getCar")
    @GET
    public Response getCar(Request request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("ClassName: ", "Test1");
        responseMap.put("route_method", request.getMethod());
        responseMap.put("route_location", request.getLocation());
        responseMap.put("return_value", audi);
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path(path = "/postCar")
    @POST
    public Response postCar(Request request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("ClassName: ", "Test1");
        responseMap.put("route_method", request.getMethod());
        responseMap.put("route_location", request.getLocation());
        responseMap.put("parameters", request.getParameters());
        Response response = new JsonResponse(responseMap);
        return response;
    }
}
