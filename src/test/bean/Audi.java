package test.bean;

import annotations.di.Bean;
import annotations.di.Qualifier;

@Bean
@Qualifier(value = "audi")
public class Audi implements Car {
    @Override
    public String getModel() {
        return "A6";
    }

    public Audi() {}

}
