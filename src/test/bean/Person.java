package test.bean;

import annotations.di.Autowired;
import annotations.di.Bean;

@Bean
public class Person {
    private String name = "aleksa";

    @Autowired(verbose = true)
    private Address address;

    public Person() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
