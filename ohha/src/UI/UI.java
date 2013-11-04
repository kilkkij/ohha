package UI;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import physics.Simulation;

public class UI implements Runnable {
    
    private JFrame frame;
    private final Simulation sim;
    private int width;
    private int height;
    private double dpu;
    
    public UI(Simulation sim, int width, int height, double dpu) {
        this.sim = sim;
        this.dpu = dpu;         // dots per simulation unit
        this.width = width;
        this.height = height;
    }
    
    public void log(String tuloste) {
        System.out.println(tuloste);
    }

    @Override
    public void run() {
        frame = new JFrame("Box");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponents(frame);
        frame.pack();
        frame.setVisible(true);
    }
    
    public JFrame getFrame() {
        return frame;
    }

    private void addComponents(Container frame) {
        frame.add(new Canvas(sim, dpu, width, height));
    }
    
}
