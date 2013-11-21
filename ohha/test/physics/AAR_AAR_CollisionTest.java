
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
    
    Material m;
    ItemRectangle rectangle;
    ItemRectangle mass1;
    ItemRectangle mass2;
    Vector normal;
    double impulse;
    
    public AAR_AAR_CollisionTest() {
        m = new Material(1., .5, .3);
        rectangle = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                m, 2, 1, false);
        mass1 = new ItemRectangle(
            new Vector(0, 0), 0., new Vector(1., 1.), 
                m, 2, 2, false);
        mass2 = new ItemRectangle(
            new Vector(0, 0), 0., new Vector(-1., -1.), 
                m, 2, 3, false);
        normal = new Vector(1, 0);
    }
    
    @Test
    public void overlapCorrect() {
        ItemRectangle other = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                m, 1, 1, false);
        Vector relPos = new Vector(1, .5);
        Vector overlap = rectangle.calculateOverlap(other, relPos);
        Vector expected = new Vector(.5, .5);
        assertEquals(expected, overlap);
    }
    
    @Test
    public void normalCorrect() {
        Vector n = rectangle.normal(
                new Vector(1., 2.), new Vector(1, 1));
        Vector expected = new Vector(1, 0);
        assertEquals(expected, n);
        n = rectangle.normal(
                new Vector(1, 1), new Vector(-1, -1));
        assertTrue(
                (n.equals(new Vector(-1, 0))) ||
                (n.equals(new Vector(0, -1)))
        );
    }
    
    @Test
    public void impulseInCorrectDirection() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        mass1.applyImpulse(normal, 1., mass2, .1);
        assertTrue(mass1.getVelocity().getX() > v1a.getX());
        assertTrue(mass2.getVelocity().getX() < v2a.getX());
    }

    @Test
    public void impulseInCorrectDirection2() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        Vector n = new Vector(0, -1);
        mass1.applyImpulse(n, 1., mass2, .1);
        assertTrue(mass1.getVelocity().getY() < v1a.getY());
        assertTrue(mass2.getVelocity().getY() > v2a.getY());
    }
    
    @Test
    public void velocityChangesMoreForLighterObject() {
        Vector v1a = new Vector(mass1.getVelocity());
        Vector v2a = new Vector(mass2.getVelocity());
        mass1.applyImpulse(normal, 1., mass2, .1);
        double dv1 = mass1.getVelocity().getX() - v1a.getX();
        double dv2 = mass2.getVelocity().getX() - v2a.getX();
        assertTrue(Math.abs(dv1) > Math.abs(dv2));
    }
    
}
