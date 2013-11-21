package UI;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import physics.Item;
import physics.SimulationEnvironment;

/**
 * Käyttöliittymä. Sisältää JFramen, ja alueen, johon kappaleet piirretään.
 * @author juho
 */
public class UI implements Runnable {

    private Canvas canvas;
    private JFrame frame;
    private final SimulationEnvironment simEnv;
    private final int width;
    private final int height;
    private final double dpu;
    
    /**
     *
     * @param simEnv simulaatioympäristöolio
     * @param width käyttöliittymän oletusleveys
     * @param height käyttöliittymän oletuskorkeus
     * @param dpu pikseleitä fysiikan yksikköä kohden
     */
    public UI(SimulationEnvironment simEnv, int width, int height, double dpu) {
        this.dpu = dpu;         // dots per simulation unit
        this.width = width;
        this.height = height;
        this.simEnv = simEnv;
    }

    /**
     * Muodosta käyttöliittymäympäristö.
     * 
     */
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
    
    /**
     * Päivitä ruutu.
     */
    public void update() {
        canvas.repaint();
    }

    private void addComponents(Container frame) {
        this.canvas = new Canvas(this, dpu, width, height);
        frame.add(canvas);
    }

    Iterable<Item> getItems() {
        return simEnv.getItems();
    }
    
}
