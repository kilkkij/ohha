package ohha;
import UI.UI;
import java.util.Random;
import javax.swing.SwingUtilities;
import physics.AxisAlignedRectangle;
import physics.Simulation;
import logic.Vector;
import physics.Material;
import physics.SimulationEnvironment;

public class Ohha {
    
    public static void main(String[] args) {
        scenario();
//        clusterScenario();
//        sinkScenario();
    }
    
    public static void scenario() {
        Simulation sim = new Simulation(0.5);
        Material m = new Material(1., .5, .3);
        sim.addItem(new AxisAlignedRectangle(
                new Vector(0., 0.), new Vector(0., 0.), 
                m, 2., .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(-1., 1.), new Vector(.1, 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(-.5, 1.1), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, 1.1), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, 1.6), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, 2.0), new Vector(0., 0.), 
                m, .3, .3));
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
    }
    
    public static void clusterScenario() {
        Simulation sim = new Simulation(0.1);
        Material m = new Material(1., .5, .3);
        sim.addItem(new AxisAlignedRectangle(
                new Vector(0., -1.), new Vector(0., 0.), 
                m, 3., .3));
        addRandomItems(sim, m);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
    }

    public static void sinkScenario() {
        Simulation sim = new Simulation(0.5);
        Material m = new Material(1., .5, .3);
        sim.addItem(new AxisAlignedRectangle(
                new Vector(0., 0.), new Vector(0., 0.), 
                m, 2., .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, .2), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, .4), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.6, .6), new Vector(0., 0.), 
                m, .3, .3));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.3, .6), new Vector(0., 0.), 
                m, .3, .3));        
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
    }
    
    private static void addRandomItems(Simulation sim, Material m) {
        Random random = new Random();
        for (int i=0; i<30; i++) {
            double x = 2*(random.nextDouble() - .5);
            double y = 3*(random.nextDouble() - .5) + 1;
            double vx = .1*(random.nextDouble() - .5);
            sim.addItem(new AxisAlignedRectangle(
                    new Vector(x, y), new Vector(vx, 0), m, .1, .1));
        }
    }
}
