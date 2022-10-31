package test.bean;

import annotations.Bean;
import annotations.Qualifier;

@Bean
@Qualifier(value = "mercedes")
public class MercedesBenz implements Car {

    @Override
    public String getModel() {
        return "GLK";
    }
}
