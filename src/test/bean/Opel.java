package test.bean;

import annotations.Bean;
import annotations.Qualifier;

@Bean
@Qualifier(value = "astra")
public class Opel implements Car {

    @Override
    public String getModel() {
        return "Astra";
    }
}
