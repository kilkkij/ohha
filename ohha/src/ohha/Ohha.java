package ohha;
import UI.UI;
import java.util.Random;
import javax.swing.SwingUtilities;
import physics.Rectangle;
import physics.Simulation;
import logic.Vector;

public class Ohha {
    
    public static void main(String[] args) {
//        scenario();
        clusterScenario();
    }
    
    public static void scenario() {
        Simulation sim = new Simulation(0.05);
        sim.addItem(new Rectangle(0., new Vector(0., 0.), 2., .3));
        sim.addItem(new Rectangle(
                1., new Vector(-1., 1.), new Vector(.1, 0.), .3, .3));
        sim.addItem(new Rectangle(1., new Vector(-.5, 1.), .3, .3));
        UI ui = new UI(sim, 400, 400, 100.);
        SwingUtilities.invokeLater(ui);
    }
    
    public static void clusterScenario() {
        Simulation sim = new Simulation(0.2);
        sim.addItem(new Rectangle(0., new Vector(0., -1.), 2., .3));
        Random random = new Random();
        for (int i=0; i<100; i++) {
            double x = 2*(random.nextDouble() - .5);
            double y = random.nextDouble() - .5;
            sim.addItem(new Rectangle(1., new Vector(x, y), .1, .1));
        }
        UI ui = new UI(sim, 400, 400, 100.);
        SwingUtilities.invokeLater(ui);
    }
}
