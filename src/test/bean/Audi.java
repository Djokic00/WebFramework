package test.bean;

import annotations.Bean;
import annotations.Qualifier;

@Bean
@Qualifier(value = "audi")
public class Audi implements Car {

    @Override
    public String getModel() {
        return "A6";
    };
}
