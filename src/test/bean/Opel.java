package test.bean;

import annotations.di.Bean;
import annotations.di.Qualifier;

@Bean
@Qualifier(value = "opel")
public class Opel implements Car {

    private String model = "astra";
    @Override
    public String getModel() {
        return model;
    }
    @Override
    public void setModel(String model) {
        this.model = model;
    }

    public Opel() {}
}
