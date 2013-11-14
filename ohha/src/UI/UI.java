package UI;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import physics.Item2;
import physics.Item;
import physics.SimulationEnvironment;

public class UI implements Runnable {

    private Canvas canvas;
    private JFrame frame;
    private final SimulationEnvironment simEnv;
    private int width;
    private int height;
    private double dpu;
    
    public UI(SimulationEnvironment simEnv, int width, int height, double dpu) {
        this.dpu = dpu;         // dots per simulation unit
        this.width = width;
        this.height = height;
        this.simEnv = simEnv;
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
        simEnv.addUI(this);
    }
    
    public void update() {
        canvas.repaint();
    }
    
    public JFrame getFrame() {
        return frame;
    }

    private void addComponents(Container frame) {
        this.canvas = new Canvas(this, dpu, width, height);
        frame.add(canvas);
    }

    Iterable<Item> getItems() {
        return simEnv.getItems();
    }
    
}
