package service;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void initMemoryTaskManager() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        super.setDefault(manager);
    }
}
