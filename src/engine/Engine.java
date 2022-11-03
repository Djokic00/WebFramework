package engine;

import annotations.*;
import framework.Framework;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Engine {
    public static HashMap<String, Object> initializedClasses = new HashMap<>();
    public static HashMap<String, Object> initializedControllerClasses = new HashMap<>();
    public static void initClasses() throws Exception {
        List<Class> allClasses = getAllClasses("");
        for (Class cl : allClasses) {
            if (cl.isAnnotationPresent(Controller.class)) {
                Framework.addControllerClass(cl);
                initializedControllerClasses.put(cl.getName(), cl.getConstructor().newInstance());
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
            }

            if ((cl.isAnnotationPresent(Bean.class) && ((Bean) cl.getAnnotation(Bean.class)).singleton())
                    || cl.isAnnotationPresent(Service.class)) {
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
            }

            if (cl.isAnnotationPresent(Qualifier.class) && (cl.isAnnotationPresent(Bean.class) ||
                    cl.isAnnotationPresent(Service.class) || cl.isAnnotationPresent(Component.class))) {
                String value = ((Qualifier) cl.getDeclaredAnnotation(Qualifier.class)).value();
                // to do
            }
        }
    }

    public static final List<Class> getAllClasses(String packageName) {
        List<Class> allClasses = new ArrayList<>();
        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        for (String path: classPaths) {
            if (!path.endsWith("jar")) {
                try {
                    File entry = new File(path + File.separatorChar + packageName);
                    File[] files = entry.listFiles();
                    if (files != null) {
                        for (File file :  files) {
                            System.out.println("Class " + file.getName());
                            String name = file.getName();
                            if (name.endsWith(".class")) {
                                name = name.replace(".class", "");
                                packageName = packageName.replace("/", ".");
                                allClasses.add(Class.forName(packageName + "." + name));
                            } else {
                                String temporaryPackageName;
                                if (packageName.equals("")) temporaryPackageName = name;
                                else temporaryPackageName = packageName + "/" + name;
                                List<Class> directorySubClasses = getAllClasses(temporaryPackageName);
                                allClasses.addAll(directorySubClasses);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return allClasses;
    }

    public static Object getInstance(Class classType) throws Exception {
        Object returnValue = null;

        if (classType.isAnnotationPresent(Bean.class)) {
            boolean scope = ((Bean) classType.getAnnotation(Bean.class)).singleton();
            if (scope) returnValue = initializedClasses.get(classType.getName());
            else returnValue = classType.getConstructor().newInstance();
        }
        else if (classType.isAnnotationPresent(Service.class)) {
            boolean scope = ((Service) classType.getAnnotation(Service.class)).singleton();
            if (scope) returnValue = initializedClasses.get(classType.getName());
            else returnValue = classType.getConstructor().newInstance();
        }
        else if (classType.isAnnotationPresent(Component.class)) {
            boolean scope = ((Component) classType.getAnnotation(Component.class)).singleton();
            if (scope) returnValue = classType.getConstructor().newInstance();
            else returnValue = initializedClasses.get(classType.getName());
        }
        else {
            System.out.println("Autowired attribute is not a Bean, Service or Component");
        }

        return returnValue;
    }

}
