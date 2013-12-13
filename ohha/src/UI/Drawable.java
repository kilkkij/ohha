package UI;

import java.awt.Graphics2D;

/**
 * Selkeyttävä rajapinta. Tämän tulee toteuttaa kaikki oliot jotka voidaan 
 * piirtää.
 * @author juho
 */
public interface Drawable {
    
    public void draw(Graphics2D graphics);
    
}