package test.bean;

import annotations.di.Bean;
import annotations.di.Qualifier;

@Bean
@Qualifier(value = "opel")
public class Opel implements Car {
    @Override
    public String getModel() {
        return "Astra";
    }

    public Opel() {}
}
