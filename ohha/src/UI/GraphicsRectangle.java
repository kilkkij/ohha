package UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import logic.Vector;
import physics.Item;
import physics.ItemRectangle;

/**
 * Simulaation suorakaide-oliota vastaava graafinen olio. Piirtää itsensä.
 * Tietää, onko valittu.
 * @author juho
 */
public class GraphicsRectangle extends GraphicsItem implements Drawable {
    
    private double angle;
    private Color color;
    private final Path2D path;

    /**
     *
     * @param item
     * @param canvas
     */
    public GraphicsRectangle(ItemRectangle item, Canvas canvas) {
        super(canvas);
        path = new Path2D.Double();
    }
    
    /**
     * Päivitä graafisen olion parametrit simulaatio-oliota vastaaviksi.
     * Kutsutaan ennen piirtämistä.
     * @param item
     */
    @Override
    public void update(Item item) {
        Vector position = item.getPosition();
        ItemRectangle itemRectangle = (ItemRectangle) item;
        
        // kulma
        angle = item.getAngle();
        
        // paikka ja koko
        double newWidth = canvas.toCanvasDimension(itemRectangle.WIDTH);
        double newHeight = canvas.toCanvasDimension(itemRectangle.HEIGHT);
        double newX = canvas.toCanvasCoordinatesX(position.getX()) - newWidth/2;
        double newY = canvas.toCanvasCoordinatesY(position.getY()) - newHeight/2;
        Rectangle r = new Rectangle();
        r.x = (int) newX;
        r.y = (int) newY;
        r.width = (int) newWidth;
        r.height = (int) newHeight;
        
        // muodosta "polku"
        path.reset();
        path.append(r, false);
        
        // käännä polkua
        turn(path, newX, newY, newWidth, newHeight, angle);
        
        // määritä väri
        if (item.isStatic()) {
            color = canvas.STATIC_COLOR;
        } else {
            color = canvas.ITEM_COLOR;
        }
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        
        // maalaa kappale
        graphics.setColor(color);
        graphics.fill(path);
        
        // piirrä ääriviivat
        if (selected) {
            drawBorder(graphics, path, canvas.CHOSEN_COLOR);
        }
    }
    
    private void drawBorder(Graphics2D graphics, Shape shape, Color color) {
        graphics.setColor(color);
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(canvas.BORDER_THICKNESS));
        graphics.draw(shape);
        graphics.setStroke(oldStroke);
    }
    
    private void turn(Path2D path, double x, double y, double xSize, 
            double ySize, double angle) {
        AffineTransform t = new AffineTransform();
        t.translate(x + xSize/2, y + ySize/2);
        t.rotate(-angle);
        t.translate(-x - xSize/2, -y - ySize/2);
        path.transform(t);
    }
    
    @Override
    public boolean contains(Point point) {
        return path.contains(point);
    }
    
} 