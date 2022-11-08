package engine;

import annotations.di.*;
import framework.Framework;
import test.component.ComponentTest;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DIEngine {
    public static HashMap<String, Object> initializedClasses = new HashMap<>();
    public static HashMap<String, Object> initializedControllerClasses = new HashMap<>();

    private static HashMap<String, Class> qualifiers = new HashMap<>();

    public static void initClasses() throws Exception {
        List<Class> allClasses = getAllClasses("");
        System.out.println("-----------All classes are initialized-----------\n");
        for (Class cl : allClasses) {
            if (cl.isAnnotationPresent(Controller.class)) {
                Framework.addControllerClass(cl);
                initializedControllerClasses.put(cl.getName(), cl.getConstructor().newInstance());
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
            }

            if ((cl.isAnnotationPresent(Bean.class) && ((Bean) cl.getAnnotation(Bean.class)).singleton())
                    || cl.isAnnotationPresent(Service.class) || cl.isAnnotationPresent(Component.class)) {
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
            }

            if (cl.isAnnotationPresent(Qualifier.class) && (cl.isAnnotationPresent(Bean.class) ||
                    cl.isAnnotationPresent(Service.class) || cl.isAnnotationPresent(Component.class))) {
                String value = ((Qualifier) cl.getDeclaredAnnotation(Qualifier.class)).value();
                if (qualifiers.get(value) == null) {
                    qualifiers.put(value, cl);
                }
                else throw new Exception("Class already has qualifier");
            }
        }

        for (Object value : initializedControllerClasses.values()) {
            dependencyInjection(value);
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

    public static void dependencyInjection(Object object) throws Exception {
        Class cl = object.getClass();
        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                if (!field.getType().isPrimitive() && !field.getType().isInterface()) {
                    setField(field, object, false);
                }
                else if (field.getType().isInterface() && field.isAnnotationPresent(Qualifier.class)) {
                    setField(field, object, true);
                }
            }
        }
    }

    public static void setField(Field field, Object object, boolean isInterface) throws Exception {
        boolean isVerbose = field.getAnnotation(Autowired.class).verbose();
        Class classType;
        if (isInterface) {
            String value = field.getAnnotation(Qualifier.class).value();
            if (qualifiers.get(value) == null) {
                throw new Exception("No bean for qualifier");
            }
            classType = qualifiers.get(value);
        }
        else classType = field.getType();

        Object instance = getInstance(classType);
        if (instance != null) {
            field.setAccessible(true);
            field.set(object, instance);
//            if (isVerbose) {
                System.out.println("Initialized " + classType.getName() + " " + field.getName() + " " +
                        "in " + object.getClass().getName() + " on " + LocalDateTime.now() +
                        " with " + field.hashCode());
//            }
            dependencyInjection(instance);
        }
    }

    public static Object getInstance(Class classType) throws Exception {
        Object returnValue;

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
            if (scope) returnValue = initializedClasses.get(classType.getName());
            else returnValue = classType.getConstructor().newInstance();
        }
        else {
            throw new Exception("Autowired attribute is not a Bean, Service or Component");
        }

        return returnValue;
    }

}
