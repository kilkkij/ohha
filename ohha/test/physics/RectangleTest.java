
package physics;

import logic.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juho
 */
public class RectangleTest {
    
    Rectangle rectangle;
    
    public RectangleTest() {
        rectangle = new Rectangle(0, new Vector(0, 0), 2, 1);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void eiOsuRinnakkaiseenSuorakulmioon() {
        Rectangle other = new Rectangle(0, new Vector(-3, 0), 1, 1);
        boolean expectedResult = false;
        boolean result = rectangle.collidesWith(other);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void osuuSuorakulmioon() {
        Rectangle other = new Rectangle(0, new Vector(-1.5, 1), 2, 2);
        boolean expectedResult = true;
        boolean result = rectangle.collidesWith(other);
        assertEquals(expectedResult, result);
    }
    
}
