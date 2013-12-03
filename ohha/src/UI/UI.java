package UI;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import physics.Item;
import physics.SimulationEnvironment;

/**
 * Käyttöliittymä. Koordinoi input/output asiat.
 * Sisältää alueen, johon kappaleet piirretään.
 * @author juho
 */
public class UI extends JFrame implements Runnable {

    private Canvas canvas;
    private final SimulationEnvironment simEnv;
    private final int width;
    private final int height;
    private final double dpu;
    private final int viewMovementUnit = 10;
    private final double viewZoomUnit = .2;
    public final double rotateUnit = Math.PI/32.;
    private final ItemBuilder itemBuilder;
    
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
        this.itemBuilder = new ItemBuilder(this, simEnv);
    }

    /**
     * Muodosta käyttöliittymäympäristö.
     * 
     */
    @Override
    public void run() {
        setupFrame();
        addListeners();
        simEnv.addUI(this);
    }
    
    private void addListeners() {
        canvas.addMouseListener(new MouseClickListener(this, simEnv, itemBuilder));
        canvas.addMouseMotionListener(new MouseDragListener(itemBuilder));
        addKeyListener(new KeyboardControlListener(this, simEnv));
    }
    
    private void setupFrame() {
        setTitle("Box");
        setPreferredSize(new Dimension(width, height));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponents();
        pack();
        setVisible(true);
    }

    private void addComponents() {
        canvas = new Canvas(this, dpu);
        add(canvas);
    }
    
    public void moveViewLeft() {
        canvas.moveView(-viewMovementUnit, 0);
    }
    
    public void moveViewRight() {
        canvas.moveView(viewMovementUnit, 0);
    }
    
    public void moveViewUp() {
        canvas.moveView(0, viewMovementUnit);
    }
    
    public void moveViewDown() {
        canvas.moveView(0, -viewMovementUnit);
    }

    public void zoomOut() {
        canvas.zoom(viewZoomUnit);
    }
    
    public void zoomIn() {
        canvas.zoom(-viewZoomUnit);
    }
    
    /**
     * Päivitä ruutu.
     */
    public void update() {
        canvas.repaint();
    }

    Iterable<Item> getItems() {
        return simEnv.getItems();
    }

    Canvas getCanvas() {
        return canvas;
    }

}
