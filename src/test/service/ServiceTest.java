package test.service;

import annotations.di.Service;

@Service
public class ServiceTest {
    private String repository = "mysql";

    public ServiceTest() {}

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
