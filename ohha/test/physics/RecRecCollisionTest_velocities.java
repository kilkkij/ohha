
package physics;

import logic.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juho
 */
public class RecRecCollisionTest_velocities {
    
    private final Material material;
    private final Vector gravity;
    private final double dt;
    private final int iterations;
    
    public RecRecCollisionTest_velocities() {
        material = new Material(1., 1., 0.);
        gravity = new Vector(0., 0.);
        dt = .02;
        iterations = 1;
    }
    
    @Test
    public void collisionResolvedInCorrectDirections() {
        ItemRectangle rect1 = new ItemRectangle(
            new Vector(-1.0, 0.0), 0., new Vector(1., 0.), 0.,
                material, 3, 3, false);
        ItemRectangle rect2 = new ItemRectangle(
            new Vector(1.0, 0.0), 0., new Vector(-1., 0.), 0.,
                material, 3, 3, false);
        RectRectCollision collision = new RectRectCollision(rect1, rect2);
        collision.resolve(dt, gravity, iterations);
        Vector v1 = new Vector(rect1.getVelocity());
        Vector v2 = new Vector(rect2.getVelocity());
        rect1.accelerate(dt, gravity);
        rect2.accelerate(dt, gravity);
        assertTrue(rect1.getVelocity().getX() < v1.getX());
        assertTrue(rect2.getVelocity().getX() > v2.getX());
    }
    
    @Test
    public void velocityChangesMoreForLighterObject() {
        ItemRectangle rect1 = new ItemRectangle(
            new Vector(-1.0, 0.0), 0., new Vector(1., 0.), 0.,
                material, 3, 3, false);
        ItemRectangle rect2 = new ItemRectangle(
            new Vector(1.0, 0.0), 0., new Vector(-1., 0.), 0.,
                material, 3, 1, false);
        RectRectCollision collision = new RectRectCollision(rect1, rect2);
        collision.resolve(dt, gravity, iterations);
        Vector v1 = new Vector(rect1.getVelocity());
        Vector v2 = new Vector(rect2.getVelocity());
        rect1.accelerate(dt, gravity);
        rect2.accelerate(dt, gravity);
        assertTrue(Math.abs(rect1.getVelocity().getX() - v1.getX()) < 
                Math.abs(rect2.getVelocity().getX() - v2.getX()));
    }
    
}
