
import org.junit.Test;

import static org.example.Main.parseConfig;
import static org.junit.Assert.assertEquals;


public class MainTest {

    @Test
    public void parseConfigTest() {
        int actual = Integer.parseInt(parseConfig("port"));
        assertEquals(8080, actual);
    }
}
