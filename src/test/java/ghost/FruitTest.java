package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FruitTest {
    @Test
    public void createFruit() {
        App app = new App();
        Fruit fruitObject = new Fruit(0, 0, app);
        assertNotNull(fruitObject);
    }
}