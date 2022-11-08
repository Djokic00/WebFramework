package test.bean;

import annotations.di.Bean;
import annotations.di.Qualifier;

@Bean
@Qualifier(value = "audi")
public class Audi implements Car {

    private String model = "A6";
    @Override
    public String getModel() {
        return model;
    }
    @Override
    public void setModel(String model) {
        this.model = model;
    }

    public Audi() {}

}
