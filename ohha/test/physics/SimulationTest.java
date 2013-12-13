
package physics;

import static logic.Lib.EPSILON;
import logic.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juho
 */
public class SimulationTest {
    
    public SimulationTest() {

    }
    
    @Test
    public void movesObjects() {
        Simulation sim = new Simulation(new Vector(0., 0.));
        ItemRectangle item = new ItemRectangle(
                new Vector(0., 0.), 0., new Vector(1., 0.), 0.,
                new Material(1., .5, .3), 1., 1., true);
        sim.addItem(item);
        Vector oldPos = new Vector(item.getPosition());
        sim.step(1.);
        Vector newPos = item.getPosition();
        Vector expectedChange = new Vector(1., 0.);
        assertEquals(expectedChange.getX(), newPos.substract(oldPos).getX(), 
                EPSILON);
    }
    
}
