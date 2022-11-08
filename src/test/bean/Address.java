package test.bean;

import annotations.di.Bean;

@Bean(singleton = false)
public class Address {
    private String street = "Decanska 23";

    public Address() {}
}
