package UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
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
    
    private final UI userInterface;
    private double dpu;
    private int centerX;
    private int centerY;
    private final Color chosenColor = Color.RED;
    private final Color staticColor = Color.GRAY;
    private final Color itemColor = Color.BLACK;
    private final float borderThickness = 2;

    /**
     * 
     * @param ui käyttöliittymäolio
     * @param dpu "dots per unit"
     */
    public Canvas(UI ui, double dpu) {
        super.setBackground(Color.WHITE);
        this.userInterface = ui;
        this.dpu = dpu;
        centerX = 0;
        centerY = 0;
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
        drawItems(g2D);
    }
    
    private void drawItems(Graphics2D graphics) {
        for (Item item: userInterface.getItems()) {
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
        
        // Suorakulmio
        Rectangle r = new Rectangle(
            (int) x, (int) y,
            (int) dx, (int) dy);
        
        // määrittele väri
        if (item.static_()) {
            graphics.setColor(staticColor);
        } else {
            graphics.setColor(itemColor);
        }
        
        // käännä
        Path2D.Double path = new Path2D.Double();
        path.append(r, false);
        AffineTransform t = new AffineTransform();
        t.translate(x + dx/2, y + dy/2);
        t.rotate(-angle);
        t.translate(-x - dx/2, -y - dy/2);
        path.transform(t);
        
        // piirrä
        graphics.fill(path);
        
        // ääriviivat
        if (item.CHOSEN) {
            graphics.setColor(chosenColor);
            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(borderThickness));
            graphics.draw(path);
            graphics.setStroke(oldStroke);
        } 
        
    }
    
    private double toCanvasCoordinatesX(double x) {
        return getWidth()/2 + dpu*x + centerX;
    }

    private double toCanvasCoordinatesY(double y) {
        return getHeight()/2 - dpu*y - centerY;
    }
    
    public double toPhysicsCoordinatesX(double x) {
        return (x - getWidth()/2 - centerX)/dpu;
    }
    
    public double toPhysicsCoordinatesY(double y) {
        return (getHeight()/2 - y - centerY)/dpu;
    }

    public void moveView(int x, int y) {
        centerX -= x;
        centerY -= y;
    }

    public void zoom(double d) {
        // zoom ulos ja zoom sisään käsitellään vähän eri tavalla
        double zoomfactor;
        if (d > 0) {
            zoomfactor = 1. - d;
        } else {
            zoomfactor = 1./(1. + d);
        }
        dpu *= zoomfactor;
        centerX *= zoomfactor;
        centerY *= zoomfactor;
    }
} 