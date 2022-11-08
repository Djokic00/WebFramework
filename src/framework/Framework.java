package framework;

import annotations.http.GET;
import annotations.http.POST;
import annotations.http.Path;
import engine.DIEngine;
import model.request.Request;
import model.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Framework {
    public static final List<Class> controllerClasses = new ArrayList<>();
    public static final HashMap<String, Method> methodsWithGetAnnotation = new HashMap<>();
    public static final HashMap<String, Method> methodsWithPostAnnotation = new HashMap<>();

    public static void addControllerClass(Class cl) {
        controllerClasses.add(cl);
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

    public static Response getResponse(Request request) throws InvocationTargetException, IllegalAccessException {

        if (request.getLocation().endsWith("favicon.ico")) {
            return null;
        }
        if (request.getMethod().toString().equals("GET")) {
            Method methodGet = methodsWithGetAnnotation.get(request.getLocation());
            return (Response) methodGet.invoke(DIEngine.initializedControllerClasses.get(methodGet.getDeclaringClass().getName()), request);
        }
        else if (request.getMethod().toString().equals("POST")) {
            String path = request.getLocation();
            int params = path.indexOf('?');
            if (params > -1)
                path = path.substring(0, params);
            Method methodPost = methodsWithPostAnnotation.get(path);
            return (Response) methodPost.invoke(DIEngine.initializedControllerClasses.get(methodPost.getDeclaringClass().getName()), request);
        }

        return null;
    }
}
