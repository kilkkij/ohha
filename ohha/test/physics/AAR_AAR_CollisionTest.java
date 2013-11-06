
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
public class AAR_AAR_CollisionTest {
    
    AxisAlignedRectangle rectangle;
    AxisAlignedRectangle mass1;
    AxisAlignedRectangle mass2;
    Vector normal;
    double impulse;
    
    public AAR_AAR_CollisionTest() {
        rectangle = new AxisAlignedRectangle(0, new Vector(0, 0), 2, 1);
        mass1 = new AxisAlignedRectangle(
            1, new Vector(0, 0), 2, 2);
        mass2 = new AxisAlignedRectangle(
            2, new Vector(0, 0), 2, 2);
        normal = new Vector(1, 0);
        impulse = 1.;
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
    public void laskeeSisennyksen() {
        AxisAlignedRectangle other = new AxisAlignedRectangle(
                0, new Vector(0, 0), 1, 1);
        Vector relPos = new Vector(1, .5);
        Vector overlap = rectangle.calculateOverlap(other, relPos);
        Vector expected = new Vector(.5, .5);
        assertEquals(expected, overlap);
    }
    
    @Test
    public void laskeeNormaalin() {
        Vector normal = rectangle.calculateNormal(
                new Vector(1., 2.), new Vector(1, 1));
        Vector expected = new Vector(1, 0);
        assertEquals(expected, normal);
        normal = rectangle.calculateNormal(
                new Vector(1, 1), new Vector(-1, -1));
        assertTrue(
                (normal.equals(new Vector(-1, 0))) ||
                (normal.equals(new Vector(0, -1)))
        );
    }
    
    @Test
    public void impulssiOikeaanSuuntaan() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        mass1.applyImpulse(normal, impulse, mass2);
        assertTrue(mass1.getVelocity().getX() > v1a.getX());
        assertTrue(mass2.getVelocity().getX() < v2a.getX());
    }

    @Test
    public void impulssiOikeaanSuuntaan2() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        Vector normal = new Vector(0, -1);
        mass1.applyImpulse(normal, impulse, mass2);
        assertTrue(mass1.getVelocity().getY() < v1a.getY());
        assertTrue(mass2.getVelocity().getY() > v2a.getY());
    }
    
    @Test
    public void NopeusMuuttuuEnemmanKevyelle() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        mass1.applyImpulse(normal, impulse, mass2);
        double dv1 = mass1.getVelocity().getX() - v1a.getX();
        double dv2 = mass2.getVelocity().getX() - v2a.getX();
        assertTrue(Math.abs(dv1) > Math.abs(dv2));
    }
    
}
