package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import logic.Vector;
import physics.AxisAlignedRectangle;
import physics.Item;
import physics.Item2;
import physics.Rectangle;

public class Canvas extends JPanel {
    
    private final UI ui;
    private final double dpu;
    private final int width;
    private final int height;

    public Canvas(UI ui, double dpu, int width, int height) {
        super.setBackground(Color.WHITE);
        this.ui = ui;
        this.dpu = dpu;
        this.width = width;
        this.height = height;
    } 
    
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
        if (item instanceof AxisAlignedRectangle) {
            drawRectangle(graphics, item);
        }
    }
    
    public void drawRectangle(Graphics2D graphics, Item item) {
        Vector position = item.getPosition();
        AxisAlignedRectangle rectangle = (AxisAlignedRectangle) item;
        double angle = item.getAngle();
        int x = toCanvasCoordinatesX(position.getX() - rectangle.width/2);
        int y = toCanvasCoordinatesY(position.getY() + rectangle.height/2);
        int dx = (int) (rectangle.width*dpu);
        int dy = (int) (rectangle.height*dpu);
//        graphics.fillRect(x, y, dx, dy);
        graphics.translate(width/2, height/2);
        graphics.rotate(angle);
        graphics.translate(-width/2, -height/2);
        graphics.drawRect(x, y, dx, dy);
    }
    
    public int toCanvasCoordinatesX(double x) {
        return width/2 + (int) (dpu*x);
    }

    public int toCanvasCoordinatesY(double y) {
        return height/2 - (int) (dpu*y);
    }
} 