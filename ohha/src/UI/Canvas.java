package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import physics.ItemRectangle;
import physics.Item;

/**
 * Piirtoalusta. Sisältää kappaleiden piirtometodit ja muuttaa
 * simulaatiokoordinaatit näytön koordinaateiksi.
 * @author juho
 */
public class Canvas extends JPanel {
    
    /**
     * Valittujen kappaleiden indikaattoriväri.
     */
    public final Color CHOSEN_COLOR = Color.RED;

    /**
     * Staattisten kappaleiden väri.
     */
    public final Color STATIC_COLOR = Color.GRAY;

    /**
     * Tavallisten kappaleiden väri.
     */
    public final Color ITEM_COLOR = Color.BLACK;

    /**
     * Valittujen kappaleiden reunan paksuus (pikseliä
     */
    public final float BORDER_THICKNESS = 2;

    /**
     * Vähintään tämänpaksuisena näkyy kaikki kappaleet aina (pikseleissä).
     */
    public final double MINIMUM_THICKNESS = 2; 
    
    private final UI userInterface;
    private double dpu;
    private int centerX;
    private int centerY;
    private Drawable unfinishedItem;
    private final Map<Item,GraphicsItem> itemMap;

    /**
     * 
     * @param ui käyttöliittymäolio
     * @param dpu "dots per unit"
     */
    public Canvas(UI ui, double dpu) {
        super.setBackground(Color.WHITE);
        this.userInterface = ui;
        this.dpu = dpu;
        this.centerX = 0;
        this.centerY = 0;
        itemMap = new HashMap<>();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        drawSimulationItems(g2D);
        drawItemInProgress(g2D);
    }
    
    private void drawItemInProgress(Graphics2D graphics) {
        if (userInterface.isBuilding()) {
            unfinishedItem.draw(graphics);
        }
    }
    
    private void drawSimulationItems(Graphics2D graphics) {
        for (Item item: itemMap.keySet()) {
            if (item instanceof ItemRectangle) {
                itemMap.get(item).update((ItemRectangle) item);
                itemMap.get(item).draw(graphics);
            }
        }
    }
    
    /**
     * Lisää kappale piirrettävien joukkoon.
     * @param item
     */
    public void addItem(Item item) {
        if (item instanceof ItemRectangle) {
            itemMap.put(item, new GraphicsRectangle((ItemRectangle) item, this));
        }
    }

    /**
     * Vaihda kappaleiden valintastatusta. Jos valitun pisteen alla on
     * kappaleita, vaihdetaan alla olevien kappaleiden valintastatus. Jos alla
     * ei ole mitään, poistetaan kaikkien valinnat.
     * @param point 
     */
    public void applySelectionOnPoint(Point point) {
        boolean somethingOnPoint = false;
        for (Item item: itemMap.keySet()) {
            if (itemMap.get(item).contains(point)) {
                itemMap.get(item).toggleSelection();
                somethingOnPoint = true;
            }
        }
        if (!somethingOnPoint) {
            for (Item item: itemMap.keySet()) {
                itemMap.get(item).unSelect();
            }
        }
    }
    
    /**
     * Lukitse valitut kappaleet. Jos kaikki
     * valitut kappaleet ovat staattisia, kaikki päästetään vapaaksi.
     */
    public void toggleSelectedStatic() {
        for (Item item: itemMap.keySet()) {
            if (itemMap.get(item).isSelected()) {
                if (!item.isStatic()) {
                    setSelectedItemsStatic();
                    return;
                }
            }
        }
        setSelectedItemsFree();
    }
    
    private void setSelectedItemsStatic() {
        for (Item item: itemMap.keySet()) {
            if (itemMap.get(item).isSelected()) {
                item.setStatic();
            }
        }
    }
    
    private void setSelectedItemsFree() {
        for (Item item: itemMap.keySet()) {
            if (itemMap.get(item).isSelected()) {
                item.setFree();
            }
        }
    }
    
    /**
     *
     * @param angle
     */
    public void conditionalRotate(double angle) {
        for (Item item: itemMap.keySet()) {
            if (itemMap.get(item).isSelected()) {
                item.rotate(angle);
            }
        }
    }

    /**
     * Poistaa esineitä simulaatiosta ja näytöltä. Jos ei ole mitään valittu,
     * poistaa kaiken.
     */
    public void removeItems() {
        boolean atLeastOneChosen = false;
        for (Iterator<Item> it = itemMap.keySet().iterator(); it.hasNext();) {
            Item item = it.next();
            if (itemMap.get(item).isSelected()) {
                userInterface.getSimEnv().getSim().removeItem(item);
                it.remove();
                atLeastOneChosen = true;
            }
        }
        if (!atLeastOneChosen) {
            userInterface.getSimEnv().getSim().clear();
            itemMap.clear();
        }
    }
    
    /**
     * Näytön koordinaatistoon.
     * @param x
     * @return
     */
    public double toCanvasCoordinatesX(double x) {
        return getWidth()/2 + dpu*x + centerX;
    }

    /**
     * Näytön koordinaatistoon.
     * @param y
     * @return
     */
    public double toCanvasCoordinatesY(double y) {
        return getHeight()/2 - dpu*y - centerY;
    }
    
    /**
     * Fysiikan kokoyksikkö näytön kokoyksiköksi. Metodi antaa vähintään
     * minimikoon.
     * @param dimension
     * @return
     */
    public double toCanvasDimension(double dimension) {
        double graphicsDimension = dimension*dpu;
        if (graphicsDimension < MINIMUM_THICKNESS) {
            return MINIMUM_THICKNESS;
        }
        return graphicsDimension;
    }
    
    /**
     * Näytön koordinaatistosta "tavalliseen" koordinaatistoon, jossa fysiikka
     * tapahtuu.
     * @param x
     * @return
     */
    public double toPhysicsCoordinatesX(double x) {
        return (x - getWidth()/2 - centerX)/dpu;
    }
    
    /**
     * Näytön koordinaatistosta "tavalliseen" koordinaatistoon, jossa fysiikka
     * tapahtuu.
     * @param y
     * @return
     */
    public double toPhysicsCoordinatesY(double y) {
        return (getHeight()/2 - y - centerY)/dpu;
    }

    /**
     * Siirrä näkökenttää.
     * @param x Pikseliä.
     * @param y Pikseliä.
     */
    public void moveView(int x, int y) {
        centerX -= x;
        centerY -= y;
    }

    /**
     * Suurenna tai pienennä kuvaa.
     * @param zoomPortion Zoomin määrä desimaalilukuna 
     * (esim. 1 = kaksinkertainen zoom)
     */
    public void zoom(double zoomPortion) {
        // Zoom ulos ja zoom sisään käsitellään vähän eri tavalla.
        // Idea on se että kun zoomataan ulos ja sisään saman verran, 
        // mikään ei muutu.
        double zoomfactor;
        if (zoomPortion > 0) {
            zoomfactor = 1. - zoomPortion;
        } else {
            zoomfactor = 1./(1. + zoomPortion);
        }
        dpu *= zoomfactor;
        centerX *= zoomfactor;
        centerY *= zoomfactor;
    }
    
    public void setUnfinishedItem(Drawable unfinishedItem) {
        this.unfinishedItem = unfinishedItem;
    }

} 