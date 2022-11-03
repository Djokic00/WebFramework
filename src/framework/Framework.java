package framework;

import annotations.GET;
import annotations.POST;
import annotations.Path;
import engine.Engine;
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

    public static Response getResponse(Request req) throws InvocationTargetException, IllegalAccessException {
        if (req.getMethod().toString().equals("GET")) {
            Method methodGet = methodsWithGetAnnotation.get(req.getLocation());
            return (Response) methodGet.invoke(Engine.initializedControllerClasses.get(methodGet.getDeclaringClass().getName()), req);
        }
        else if (req.getMethod().toString().equals("POST")) {
            Method methodPost = methodsWithPostAnnotation.get(req.getLocation());
            return (Response) methodPost.invoke(Engine.initializedClasses.get(methodPost.getDeclaringClass().getName()), req);
        }

        return null;
    }
}
