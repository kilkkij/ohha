package physics;

import UI.UI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SimulationEnvironment implements ActionListener, Runnable {
    
    private UI ui;
    private final Simulation sim;
    private final Timer timer;
    private long timestamp;
    private final int timestep;

    public SimulationEnvironment(Simulation sim, int timestep) {
        this.sim = sim;
        this.timer = new Timer(timestep, this);
        this.timestep = timestep;
        this.timestamp = 0;
    }
    
    public void addUI(UI ui) {
        this.ui = ui;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        logTime(ae);
        sim.step(((float) timestep)/1000);
        ui.update();
    }
    
    public void logTime(ActionEvent ae) {
        long dt = ae.getWhen() - timestamp;
        ui.log("aika-askel: " + dt + " ms");
        timestamp += dt;
    }

    @Override
    public void run() {
        timer.start();
    }

    public Iterable<Item> getItems() {
        return sim.getItems();
    }
    
} 