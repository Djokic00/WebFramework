package test.component;

import annotations.Autowired;
import annotations.Component;

@Component
public class ComponentTest {

    @Autowired(verbose = true)
    private ComponentTest componentTest;

    public ComponentTest() {}
}
