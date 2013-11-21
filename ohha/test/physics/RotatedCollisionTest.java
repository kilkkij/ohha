
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
public class RotatedCollisionTest {
    
    Material material;
    
    public RotatedCollisionTest() {
        material = new Material(1., .5, .3);
    }
    
    @Test
    public void alignedCollisionFalse1() {
        // kappaleet rinnakkain erillään
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(2, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        assertFalse(A.collidesWith(B));
    }
    
    @Test
    public void alignedCollisionFalse2() {
        // kappaleet päällekkäin erillään
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(0, 2), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        assertFalse(A.collidesWith(B));
    }
    
    @Test
    public void alignedCollisionTrue1() {
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(0.5, -0.5), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        assertTrue(A.collidesWith(B));
    }

    @Test
    public void alignedCollisionTrue2() {
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(-0.5, 0.5), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        assertTrue(A.collidesWith(B));
    }
    
    @Test
    public void rotatedCollisionTrue1() {
        // törmäys vain vinouden vuoksi
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(1.1, 0), Math.PI*.25, new Vector(0., 0.), 
                material, 1, 1, false);
        assertTrue(A.collidesWith(B));
    }
    
    @Test
    public void rotatedCollisionFalse1() {
        // yksi neliö kahdeksasosakäännöksen vinossa
        // toinen kappale ylävasemmalla suorassa
        ItemRectangle A = new ItemRectangle(
                new Vector(0, 0), -Math.PI*.25, new Vector(0., 0.), 
                material, 1, 1, false);
        ItemRectangle B = new ItemRectangle(
                new Vector(-.9, -.9), 0., new Vector(0., 0.), 
                material, 1, 1, false);
        assertFalse(A.collidesWith(B));
    }
    
}
