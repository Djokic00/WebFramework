package engine;

import annotations.di.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DIEngine {

    private static DIEngine instance = null;
    private HashMap<String, Object> initializedClasses = new HashMap<>();
    public HashMap<String, Object> initializedControllerClasses = new HashMap<>();
    private DIContainer dependencyContainer = DIContainer.getInstance();

    public static DIEngine getInstance() {
        if (instance == null) {
            instance = new DIEngine();
        }
        return instance;
    }

    public void initClasses() throws Exception {
        List<Class> allClasses = getAllClasses("");
        System.out.println("-----------All classes are initialized-----------\n");
        for (Class cl : allClasses) {
            if (cl.isAnnotationPresent(Controller.class)) {
                Router.getInstance().mapRoutes(cl);
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
                if (dependencyContainer.getQualifiers().get(value) == null) {
                    dependencyContainer.getQualifiers().put(value, cl);
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
                            String name = file.getName();
                            if (name.endsWith(".class")) {
                                System.out.println("Class " + file.getName());
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

    public void dependencyInjection(Object object) throws Exception {
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

    public void setField(Field field, Object object, boolean isInterface) throws Exception {
        boolean isVerbose = field.getAnnotation(Autowired.class).verbose();
        Class classType;
        if (isInterface) {
            String value = field.getAnnotation(Qualifier.class).value();
            if (dependencyContainer.getQualifiers().get(value) == null) {
                throw new Exception("No bean for qualifier");
            }
            classType = dependencyContainer.getQualifiers().get(value);
        }
        else classType = field.getType();

        Object instance = getInstance(classType);
        if (instance != null) {
            field.setAccessible(true);
            field.set(object, instance);
            if (isVerbose) {
                System.out.println("Initialized " + classType.getName() + " " + field.getName() + " " +
                        "in " + object.getClass().getName() + " on " + LocalDateTime.now() +
                        " with " + field.hashCode());
            }
            dependencyInjection(instance);
        }
    }

    public Object getInstance(Class classType) throws Exception {
        Object returnValue;

        if (classType.isAnnotationPresent(Bean.class)) {
            boolean scope = ((Bean) classType.getAnnotation(Bean.class)).singleton();
            if (scope) returnValue = initializedClasses.get(classType.getName());
            else returnValue = classType.getConstructor().newInstance();
        }
        else if (classType.isAnnotationPresent(Service.class)) {
            returnValue = initializedClasses.get(classType.getName());
        }
        else if (classType.isAnnotationPresent(Component.class)) {
            returnValue = classType.getConstructor().newInstance();
        }
        else {
            throw new Exception("Autowired attribute is not a Bean, Service or Component");
        }

        return returnValue;
    }

}
