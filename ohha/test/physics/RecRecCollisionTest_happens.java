
package physics;

import logic.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juho
 */
public class RecRecCollisionTest_happens {
    
    private final Material material;
    private final Vector gravity;
    private final double dt;
    private final int iterations;
    
    public RecRecCollisionTest_happens() {
        material = new Material(1., 1., 0.);
        gravity = new Vector(0., 0.);
        dt = .02;
        iterations = 1;
    }
    
    @Test
    public void alignedCollisionFalse1() {
        // kappaleet rinnakkain erillään
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(2, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertFalse(collision.resolve(dt, gravity, iterations));
    }
    
    @Test
    public void alignedCollisionFalse2() {
        // kappaleet päällekkäin erillään
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(0, 2), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertFalse(collision.resolve(dt, gravity, iterations));
    }
    
    @Test
    public void alignedCollisionTrue1() {
        // kappaleet osittain päällekkäin
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(0.5, -0.5), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertTrue(collision.resolve(dt, gravity, iterations));
    }

    @Test
    public void alignedCollisionTrue2() {
        // kappaleet toisella tapaa päällekkäin
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(-0.5, 0.5), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertTrue(collision.resolve(dt, gravity, iterations));
    }
    
    @Test
    public void rotatedCollisionTrue1() {
        // törmäys vain kulman vuoksi
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(1.1, 0), Math.PI*.25, new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertTrue(collision.resolve(dt, gravity, iterations));
    }
    
    @Test
    public void rotatedCollisionFalse1() {
        // yksi neliö kahdeksasosakäännöksen vinossa
        // toinen kappale ylävasemmalla suorassa
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), -Math.PI*.25, new Vector(0., 0.), 0.,
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(-.9, -.9), 0., new Vector(0., 0.), 0.,
                material, 1, 1, false);
        RectRectCollision collision = new RectRectCollision(A, B);
        assertFalse(collision.resolve(dt, gravity, iterations));
    }
    
    @Test
    public void normalCorrect() {
        ItemRectangle rectangle = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 0.,
                material, 2, 1, false);
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
    
}
