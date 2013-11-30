
package logic;

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

    @Test
    public void incrementIncrements() {
        vector.increment(other);
        assertEquals("[1.0, 3.0]", vector.toString());
    }

    @Test
    public void dotWorks() {
        double expResult = -2.;
        double result = vector.dot(other);
        assertEquals(expResult, result, EPSILON);
    }
    
    @Test
    public void crossWorks() {
        Vector A = new Vector(1., 0.);
        Vector B = new Vector(0., 1);
        double expResult = 1.;
        assertEquals(expResult, A.cross(B), EPSILON);
    }
    
    @Test
    public void distanceWorks() {
        double expResult = Math.sqrt(18);
        double result = vector.distance(other);
        assertEquals(expResult, result, EPSILON);
    }
    
    @Test
    public void comparisonWorks() {
        Vector same = new Vector(vector.getX(), vector.getY());
        assertEquals(true, vector.equals(same));
    }
    
    @Test
    public void rotationRotates30Degrees() {
        vector = new Vector(1., 0.);
        Vector rotated = vector.rotate(30./180*Math.PI);
        assertEquals(Math.sqrt(3)/2, rotated.getX(), EPSILON);
        assertEquals(.5, rotated.getY(), EPSILON);
    }

}
