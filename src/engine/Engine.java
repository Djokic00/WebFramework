package engine;

import annotations.Controller;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    public static void initClasses() throws Exception {
        List<Class> allClasses = new ArrayList<>();
        for (Class cl : allClasses) {
            if (cl.isAnnotationPresent(Controller.class)) {

            }
        }
    }
}
