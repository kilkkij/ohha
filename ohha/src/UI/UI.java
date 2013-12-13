package UI;

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
    private final ItemBuilder itemBuilder;
    private boolean building;

    /**
     * Jos kappaletta pyöritetään, se pyörii tämän verran (radiaaneissa).
     */
    public final double rotateUnit = Math.PI/32.;
    
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
        this.itemBuilder = new ItemBuilder(this);
        this.building = false;
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
        canvas.addMouseListener(new MouseClickListener(this, itemBuilder));
        canvas.addMouseMotionListener(new MouseDragListener(itemBuilder));
        addKeyListener(new KeyboardControlListener(this));
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
    
    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void moveViewLeft() {
        canvas.moveView(-viewMovementUnit, 0);
    }
    
    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void moveViewRight() {
        canvas.moveView(viewMovementUnit, 0);
    }
    
    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void moveViewUp() {
        canvas.moveView(0, viewMovementUnit);
    }
    
    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void moveViewDown() {
        canvas.moveView(0, -viewMovementUnit);
    }

    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void zoomOut() {
        canvas.zoom(viewZoomUnit);
    }
    
    /**
     * Visuaalisen ympäristön hallintaa.
     */
    public void zoomIn() {
        canvas.zoom(-viewZoomUnit);
    }
    
    /**
     * Päivitä ruutu.
     */
    public void update() {
        canvas.repaint();
    }

    Canvas getCanvas() {
        return canvas;
    }
    
    SimulationEnvironment getSimEnv() {
        return simEnv;
    }
    
    /**
     * Aloittaa uuden palikan rakentaminen. Tätä kutsutaan, jotta piirrettävä
     * kappale näkyisi ruudulla.
     * @param rectangle
     */
    public void startBuilding(RectangleInProgress rectangle) {
        building = true;
        canvas.setUnfinishedItem(rectangle);
    }
    
    /**
     * Lopeettaa uuden kappaleen näyttäminen.
     */
    public void stopBuilding() {
        building = false;
    }
    
    /**
     * Ollaanko piirtämässä kappaletta?
     * @return
     */
    public boolean isBuilding() {
        return building;
    }
    
    /**
     * Lisää kappaleen. Lisää myös kappaleen simulaatioon.
     * @param item
     */
    public void addItem(Item item) {
        canvas.addItem(item);
        simEnv.getSim().addItem(item);
    }

}
