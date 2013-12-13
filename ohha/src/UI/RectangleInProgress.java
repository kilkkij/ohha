package UI;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import logic.Vector;
import physics.ItemRectangle;
import physics.Material;

/**
 * Suorakaide, joka on rakenteilla.
 * @author juho
 */
public class RectangleInProgress extends Rectangle implements Drawable {
    
    private final int minimumDimension = 4;
    private final Canvas canvas;
    private final int startX;
    private final int startY;

    /**
     *
     * @param canvas
     * @param startX
     * @param startY
     */
    public RectangleInProgress(Canvas canvas, int startX, int startY) {
        super();
        this.canvas = canvas;
        this.startX = startX;
        this.startY = startY;
    }
    
    /**
     *
     * @param cornerX
     * @param cornerY
     */
    public void updateSize(int cornerX, int cornerY) {
        width = dimensionOfReasonableProportion((cornerX - startX)*2);
        height = dimensionOfReasonableProportion((cornerY - startY)*2);
        x = startX - width/2;
        y = startY - height/2;
    }
    
    private int dimensionOfReasonableProportion(int dimension) {
        dimension = Math.abs(dimension);
        if (dimension < minimumDimension) {
            return minimumDimension;
        } else {
            return dimension;
        }
    }
    
    /**
     * Muodosta keskeneräisestä graafisesta oliosta simulaatio-olio.
     * @param material
     * @return
     */
    public ItemRectangle toSimulationItem(Material material) {
        double X1 = canvas.toPhysicsCoordinatesX(x);
        double X2 = canvas.toPhysicsCoordinatesX(x + width);
        double Y1 = canvas.toPhysicsCoordinatesY(y); 
        double Y2 = canvas.toPhysicsCoordinatesY(y + height);
        double physicsCenterX = (X1 + X2)/2;
        double physicsCenterY = (Y1 + Y2)/2;
        double physicsWidth = Math.abs(X1 - X2);
        double physicsHeight = Math.abs(Y1 - Y2);
        return new ItemRectangle(new Vector(physicsCenterX, physicsCenterY), 0., 
                new Vector(0., 0.), 0., material, physicsWidth, physicsHeight, 
                true);
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(canvas.CHOSEN_COLOR);
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(canvas.BORDER_THICKNESS));
        graphics.draw(this);
        graphics.setStroke(oldStroke);
    }

} 