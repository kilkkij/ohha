package physics;

import UI.UI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Simulaatioympäristö. Hoitaa ajanhallinnan ja kutsuu simulaatiota.
 * @author juho
 */
public class SimulationEnvironment implements ActionListener, Runnable {
    
    private UI ui;
    private final Simulation sim;
    private final Timer timer;
    private long timestamp;
    private final int timestep;

    /**
     *
     * @param sim simulaatio
     * @param timestep aika-askel
     */
    public SimulationEnvironment(Simulation sim, int timestep) {
        this.sim = sim;
        this.timer = new Timer(timestep, this);
        this.timestep = timestep;
        this.timestamp = 0;
    }
    
    /**
     * Lisää käyttöliittymä.
     * @param ui
     */
    public void addUI(UI ui) {
        this.ui = ui;
    }

    /**
     * Päivitä simulaatio ja käyttöliittymä.
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
//        logTime(ae);
        sim.step(((float) timestep)/1000);
        ui.update();
    }
    
    private void logTime(ActionEvent ae) {
        long dt = ae.getWhen() - timestamp;
        System.out.println("aika-askel: " + dt + " ms");
        timestamp += dt;
    }

    /**
     * Käynnistä simulaatio.
     */
    @Override
    public void run() {
        timer.start();
    }

    public Simulation getSim() {
        return sim;
    }

} 