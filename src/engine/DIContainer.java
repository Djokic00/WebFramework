package engine;

import java.util.HashMap;

public class DIContainer {
    private HashMap<String, Class> qualifiers = new HashMap<>();

    private static DIContainer instance = null;
    public static DIContainer getInstance() {
        if (instance == null) {
            instance = new DIContainer();
        }
        return instance;
    }
    public HashMap<String, Class> getQualifiers() {
        return qualifiers;
    }
}
