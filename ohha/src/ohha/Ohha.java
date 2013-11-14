package ohha;
import UI.UI;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import physics.AxisAlignedRectangle;
import physics.Simulation;
import logic.Vector;
import physics.Item;
import physics.Item2;
import physics.Material;
import physics.Rectangle;
import physics.Shape;
import physics.SimulationEnvironment;

public class Ohha {
    
    public static void main(String[] args) {
        scenario();
//        clusterScenario();
//        sinkScenario();
//        standardScenario();
//        drawRectangle();
//        rotatingRectangle();
    }
    
    public static void rotatingRectangle() {
        Draw draw = new Draw();
        JFrame frame = new JFrame("Box");
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(draw);
        frame.pack();
        frame.setVisible(true);
    }
    
//    public static void drawRectangle() {
//        Simulation sim = new Simulation(new Vector(0, -0.5));
//        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
//        UI ui = new UI(simEnv, 400, 400, 100.);
//        ui.run();
//        simEnv.run();
//        Material material = new Material(1., .5, .3);
//        Shape shape= new Rectangle(1., 1.);
//        Item2 box = new Item2(new Vector(0., 0.), 0., new Vector(0., 0.),
//                material, shape);
//        sim.addItem(box);
//    }
    
//    public static void standardScenario() {
//        Simulation sim = new Simulation(new Vector(0, -0.5));
//        Material m = new Material(1., .5, .3);
//        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
//        UI ui = new UI(simEnv, 400, 400, 100.);
//        Shape wideRectangle = new Rectangle(2., .3);
//        Shape square = new Rectangle(.3, .3);
//        sim.addItem(new Item2(new Vector(0., -1.), 0., new Vector(0., 0.), 
//                m, wideRectangle));
//        sim.addItem(new Item2(new Vector(0., 0.), 0., new Vector(0., 0.), 
//                m, square));
//        ui.run();
//        simEnv.run();
//    }
    
    public static void scenario() {
        Simulation sim = new Simulation(new Vector(0., -.5));
        Material m = new Material(1., .5, .3);
        sim.addItem(new AxisAlignedRectangle(
                new Vector(0., -.5), 0., new Vector(0., 0.), 
                m, 2., .3, true));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(-1., 0.), 0., new Vector(.1, 0.), 
                m, .3, .3, false));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(-.5, .1), 0., new Vector(0., 0.), 
                m, .3, .3, false));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, .1), 0., new Vector(0., 0.), 
                m, .3, .3, false));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, .6), 0., new Vector(0., 0.), 
                m, .3, .3, false));
        sim.addItem(new AxisAlignedRectangle(
                new Vector(.5, 1.0), 0., new Vector(0., 0.), 
                m, .3, .3, false));
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
    }
    
    public static void clusterScenario() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .5, .3);
        sim.addItem(new AxisAlignedRectangle(
                new Vector(0., -1.), 0., new Vector(0., 0.), 
                m, 3., .3, true));
        addRandomItems(sim, m);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
    }
//
//    public static void sinkScenario() {
//        Simulation sim = new Simulation(0.5);
//        Material m = new Material(1., .5, .3);
//        sim.addItem(new AxisAlignedRectangle(
//                new Vector(0., 0.), new Vector(0., 0.), 
//                m, 2., .3));
//        sim.addItem(new AxisAlignedRectangle(
//                new Vector(.5, .2), new Vector(0., 0.), 
//                m, .3, .3));
//        sim.addItem(new AxisAlignedRectangle(
//                new Vector(.5, .4), new Vector(0., 0.), 
//                m, .3, .3));
//        sim.addItem(new AxisAlignedRectangle(
//                new Vector(.6, .6), new Vector(0., 0.), 
//                m, .3, .3));
//        sim.addItem(new AxisAlignedRectangle(
//                new Vector(.3, .6), new Vector(0., 0.), 
//                m, .3, .3));        
//        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
//        UI ui = new UI(simEnv, 400, 400, 100.);
//        ui.run();
//        simEnv.run();
//    }
    
    private static void addRandomItems(Simulation sim, Material m) {
        Random random = new Random();
        for (int i=0; i<30; i++) {
            double x = 2*(random.nextDouble() - .5);
            double y = 3*(random.nextDouble() - .5) + 1;
            double vx = .1*(random.nextDouble() - .5);
            sim.addItem(new AxisAlignedRectangle(
                    new Vector(x, y), 0., new Vector(vx, 0), m, .1, .1, false));
        }
    }
}
