package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
import logic.Vector;
import physics.ItemRectangle;
import physics.Item;

/**
 * Piirtoalusta. Sisältää kappaleiden piirtometodit ja muuttaa
 * simulaatiokoordinaatit näytön koordinaateiksi.
 * @author juho
 */
public class Canvas extends JPanel {
    
    private final UI ui;
    private final double dpu;
    private final int width;
    private final int height;

    /**
     * 
     * @param ui käyttöliittymäolio
     * @param dpu "dots per unit"
     * @param width leveys (jolla lasketaan kappaleiden sijainnit)
     * @param height korkeus (jolla lasketaan kappaleiden sijainnit)
     */
    public Canvas(UI ui, double dpu, int width, int height) {
        super.setBackground(Color.WHITE);
        this.ui = ui;
        this.dpu = dpu;
        this.width = width;
        this.height = height;
    }

    /**
     * Piirtää käyttöliittymältä pyydetyt komponentit.
     * Piirtometodi riippuu komponentin luokasta.
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        drawItems(ui.getItems(), g2D);

    }
    
    private void drawItems(Iterable<Item> items, Graphics2D graphics) {
        for (Item item: ui.getItems()) {
            drawItem(item, graphics);
        }
    }
    
    private void drawItem(Item item, Graphics2D graphics) {
        if (item instanceof ItemRectangle) {
            drawRectangle(graphics, item);
        }
    }
    
    public void drawRectangle(Graphics2D graphics, Item item) {
        
        //
        Vector position = item.getPosition();
        ItemRectangle rectangle = (ItemRectangle) item;
        double angle = item.getAngle();
        
        // sijainti ja koko ruudulla
        double x = toCanvasCoordinatesX(position.getX() - rectangle.width/2);
        double y = toCanvasCoordinatesY(position.getY() + rectangle.height/2);
        double dx = rectangle.width*dpu;
        double dy = rectangle.height*dpu;
        
        // 
        Rectangle r = new Rectangle(
            (int) x, (int) y,
            (int) dx, (int) dy);
        
        // käännä
        Path2D.Double path = new Path2D.Double();
        path.append(r, false);
        AffineTransform t = new AffineTransform();
        t.translate(x + dx/2, y + dy/2);
        t.rotate(-angle);
        t.translate(-x - dx/2, -y - dy/2);
        path.transform(t);
        
        // piirrä
        graphics.draw(path);
        
        // käsittele kierto
//        graphics.rotate(-angle, x, y);
//        graphics.rotate(angle, x, y);
//        graphics.translate(width/2, height/2);
//        graphics.rotate(-angle);
//        graphics.translate(-width/2, -height/2);
        
        // piirrä
//        graphics.drawRect(x, y, dx, dy);
//        graphics.fillRect(x, y, dx, dy);
    }
    
    private double toCanvasCoordinatesX(double x) {
        return width/2 + (dpu*x);
    }

    private double toCanvasCoordinatesY(double y) {
        return height/2 - (dpu*y);
    }
} 