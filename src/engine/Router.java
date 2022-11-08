package engine;

import annotations.http.GET;
import annotations.http.POST;
import annotations.http.Path;
import engine.DIEngine;
import model.request.Request;
import model.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Router {
    public static final HashMap<String, Method> methodsWithGetAnnotation = new HashMap<>();
    public static final HashMap<String, Method> methodsWithPostAnnotation = new HashMap<>();
    private static Router instance = null;
    public static Router getInstance(){
        if (instance == null){
            instance = new Router();
        }
        return instance;
    }

    public void mapRoutes(Class cl) {
        Method[] methods = cl.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Path.class)) {
                String path = method.getAnnotation(Path.class).path();
                if (method.isAnnotationPresent(GET.class)) {
                    methodsWithGetAnnotation.put(path, method);
                }
                else if (method.isAnnotationPresent(POST.class)) {
                    methodsWithPostAnnotation.put(path, method);
                }
            }
        }
    }

    public Response getResponse(Request request) throws InvocationTargetException, IllegalAccessException {

        if (request.getLocation().endsWith("favicon.ico")) {
            return null;
        }
        if (request.getMethod().toString().equals("GET")) {
            Method methodGet = methodsWithGetAnnotation.get(request.getLocation());
            System.out.println(methodsWithGetAnnotation);
            return (Response) methodGet.invoke(DIEngine.getInstance().initializedControllerClasses.get(methodGet.getDeclaringClass().getName()), request);
        }
        else if (request.getMethod().toString().equals("POST")) {
            String path = request.getLocation();
            int params = path.indexOf('?');
            if (params > -1)
                path = path.substring(0, params);
            Method methodPost = methodsWithPostAnnotation.get(path);
            return (Response) methodPost.invoke(DIEngine.getInstance().initializedControllerClasses.get(methodPost.getDeclaringClass().getName()), request);
        }

        return null;
    }
}
