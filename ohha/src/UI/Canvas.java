package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import physics.Item;
import physics.Simulation;

public class Canvas extends JPanel implements ActionListener {
    
    private final Simulation sim;
    private final Timer timer;
    private double dpu;
    private int width;
    private int height;
    private long timestamp;
    private int timestep;

    public Canvas(Simulation sim, double dpu, int width, int height) {
        super.setBackground(Color.WHITE);
        this.sim = sim;
        this.dpu = dpu;
        this.width = width;
        this.height = height;
        this.timestep = 60;
        timer = new Timer(timestep, this);
        timer.start();
    } 
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (Item item: sim.getItems()) {
            item.draw(graphics, dpu, width, height);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        long dt = ae.getWhen() - timestamp;
        System.out.println("aika-askel: " + dt + " ms");
        timestamp += dt;
        sim.step(((float) timestep)/1000);
        this.repaint();
    }
} 