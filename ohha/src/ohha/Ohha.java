package ohha;
import UI.UI;
import java.util.Random;
import physics.ItemRectangle;
import physics.Simulation;
import logic.Vector;
import physics.Item;
import physics.Material;
import physics.SimulationEnvironment;

public class Ohha {
    
    public static void main(String[] args) {
        emptyScenario();
//        collisionTest1();
//        collisionTest2();
//        fullyOverlappingTest1();
//        fullyOverlappingTest2();
//        frictionTest1();
//        frictionTest2();
//        differentFrictionItemsTest();
//        differentElasticityItemsTest();
//        staticPointTest();
//        scenario();
//        clusterScenario();
//        sinkScenario();
    }
    
    public static void emptyScenario() {
        Simulation sim = new Simulation(new Vector(0., -.2));
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 600, 600, 100.);
        ui.run();
        simEnv.run();
    }
    
    public static void fullyOverlappingTest1() {
        // ei painovoimaa
        Simulation sim = new Simulation(new Vector(0., 0.));
        Material m = new Material(1., .3, .5);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        // kaksi sisäkkäistä kappaletta
        Item item;
        item = new ItemRectangle(
                new Vector(.0, .0), 0.0, new Vector(.0, 0.), 0.,
                m, 1., 1., false);
        ui.addItem(item);
        item = new ItemRectangle(
                new Vector(.0, .0), 0.0, new Vector(.0, 0.), 0.,
                m, 1., 1., false);
        ui.addItem(item);
    }
    
    public static void fullyOverlappingTest2() {
        // ei painovoimaa
        Simulation sim = new Simulation(new Vector(0., 0.));
        Material m = new Material(1., .3, .5);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        // kaksi sisäkkäistä, toisiaan kohti liikkuvaa kappaletta
        Item item;
        item = new ItemRectangle(
                new Vector(.0, .0), 0.0, new Vector(.1, 0.), 0.,
                m, 1., 1., false);
        ui.addItem(item);
        item = new ItemRectangle(
                new Vector(.0, .0), 0.0, new Vector(-.1, 0.), 0.,
                m, 1., 1., false);
        ui.addItem(item);
    }
    
    public static void staticPointTest() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .3, .3);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., 1), -0.0, new Vector(.0, 0.), 0.,
                m, 1., 1., false));
        ui.addItem(new ItemRectangle(
                new Vector(0., -1.), Math.PI/4., new Vector(-.0, 0.), 0.,
                m, 1., 1., true));

    }
    
    public static void collisionTest1() {
        // ei painovoimaa
        Simulation sim = new Simulation(new Vector(0., 0.));
        Material m = new Material(1., .3, .3);
        // kaksi törmäävää kappaletta
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(-.7, .0), -0.0, new Vector(.0, 0.), 0.,
                m, 1., 1., false));
        ui.addItem(new ItemRectangle(
                new Vector(.7, 0.8), 0.0, new Vector(-.0, 0.), 0.1,
                m, 2., .3, false));
    }
    
    public static void collisionTest2() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .3, .3);
        // kaksi törmäävää kappaletta
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(.0, .0), -0.1, new Vector(.0, 0.), 0.,
                m, 1., 1., false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, -1.), 0.0, new Vector(-.0, 0.), 0.,
                m, 2., .3, true));
    }
    
    public static void frictionTest1() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .3, .5);
        // kaksi törmäävää kappaletta
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(-1., .0), 0.1, new Vector(.5, 0.), 0.,
                m, 1., 1., false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, -1.), 0.0, new Vector(-.0, 0.), 0.,
                m, 3., .3, true));
    }
    
    public static void frictionTest2() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m1 = new Material(1., .3, .1);
        Material m2 = new Material(1., .3, .8);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(.5, 1.0), 0.1, new Vector(0., 0.), 0.,
                m1, .5, .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(1., 1.5), 0.1, new Vector(.0, 0.), 0.,
                m2, .5, .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, -.5), 0.7, new Vector(-.0, 0.), 0.,
                m2, 4., .3, true));
    }
    
    public static void differentFrictionItemsTest() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., -.5), 0.45, new Vector(0., 0.), 0.,
                new Material(1., 0., .3), 4, .5, true));
        ui.addItem(new ItemRectangle(
                new Vector(-1., 0.), 0.45, new Vector(.0, 0.), 0.,
                new Material(1., 0., .1), 1., .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, 0.5), 0.45, new Vector(-.0, 0.), 0.,
                new Material(1., 0., .5), 1., .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(1., 1.0), 0.45, new Vector(-.0, 0.), 0.,
                new Material(1., 0., .9), 1., .5, false));
    }
    
    public static void differentElasticityItemsTest() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., -1.), 0., new Vector(0., 0.), 0.,
                new Material(1., 1., .0), 4, .5, true));
        ui.addItem(new ItemRectangle(
                new Vector(-1., 1.), 0., new Vector(.0, 0.), 0.,
                new Material(1., 1., .0), .5, .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, 1.), 0., new Vector(-.0, 0.), 0.,
                new Material(1., .5, .0), .5, .5, false));
        ui.addItem(new ItemRectangle(
                new Vector(1., 1.), 0., new Vector(-.0, 0.), 0.,
                new Material(1., 0., .0), .5, .5, false));
    }
    
    public static void scenario() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .5, .3);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., -.5), 0., new Vector(0., 0.), 0.,
                m, 2., .3, true));
        ui.addItem(new ItemRectangle(
                new Vector(-1., 0.), -.1, new Vector(.1, 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(-.5, .1), 0.05, new Vector(0., 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(.5, .1), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(.5, .6), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));
    }
    
    public static void clusterScenario() {
        Simulation sim = new Simulation(new Vector(0., -.1));
        Material m = new Material(1., .5, .3);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., -1.), 0., new Vector(0., 0.), 0.,
                m, 3., .3, true));
        addRandomItems(ui, m);
    }

    public static void sinkScenario() {
        Simulation sim = new Simulation(new Vector(0., -.5));
        Material m = new Material(1., .5, .3);
        SimulationEnvironment simEnv = new SimulationEnvironment(sim, 50);
        UI ui = new UI(simEnv, 400, 400, 100.);
        ui.run();
        simEnv.run();
        ui.addItem(new ItemRectangle(
                new Vector(0., -0.5), 0., new Vector(0., 0.), 0.,
                m, 2., .3, true));
        ui.addItem(new ItemRectangle(
                new Vector(.0, -.2), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, .3), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(.0, 1.), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));
        ui.addItem(new ItemRectangle(
                new Vector(.4, -.1), 0., new Vector(0., 0.), 0.,
                m, .3, .3, false));        
    }
    
    private static void addRandomItems(UI ui, Material m) {
        Random random = new Random();
        for (int i=0; i<30; i++) {
            double x = 2*(random.nextDouble() - .5);
            double y = 3*(random.nextDouble() - .5) + 1;
            double vx = .1*(random.nextDouble() - .5);
            ui.addItem(new ItemRectangle(
                    new Vector(x, y), 0., new Vector(vx, 0), 0., 
                    m, .1, .1, false));
        }
    }
}
