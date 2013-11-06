
package logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VectorTest {
    
    Vector vector;
    Vector other;
    final double EPSILON = 1.e-12;
    
    public VectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        vector = new Vector(2, 3);
        other = new Vector(-1., 0.);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of increment method, of class Vector.
     */
    @Test
    public void incrementMuuttaaArvoaOikein() {
        vector.increment(other);
        assertEquals("[1.0, 3.0]", vector.toString());
    }

    /**
     * Test of dot method, of class Vector.
     */
    @Test
    public void dotLaskeeOikein() {
        double expResult = -2.;
        double result = vector.dot(other);
        assertEquals(expResult, result, EPSILON);
    }
    
    /**
     *
     */
    @Test
    public void distanceLaskeeOikein() {
        double expResult = Math.sqrt(18);
        double result = vector.distance(other);
        assertEquals(expResult, result, EPSILON);
    }
    
    @Test
    public void vertailuToimii() {
        Vector same = new Vector(vector.getX(), vector.getY());
        assertEquals(true, vector.equals(same));
    }

}
