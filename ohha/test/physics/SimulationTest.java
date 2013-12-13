
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
    
    Simulation sim;
    
    public SimulationTest() {
        sim = new Simulation(new Vector(0., 0.));
        Material m = new Material(1., .5, .3);
        sim.addItem(new ItemRectangle(
                new Vector(0., 0.), 0., new Vector(1., 0.), 0.,
                m, 1., 1., true));
    }
    
    @Test
    public void movesObjects() {
        Vector oldPos = new Vector(
                sim.getItems().iterator().next().getPosition());
        sim.step(1.);
        Vector newPos = sim.getItems().iterator().next().getPosition();
        Vector expectedChange = new Vector(1., 0.);
        assertEquals(expectedChange.getX(), newPos.substract(oldPos).getX(), 
                EPSILON);
    }
    
}
