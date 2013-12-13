package UI;

import java.awt.Point;
import physics.Item;

public abstract class GraphicsItem implements Drawable {
    
    protected final Canvas canvas;
    protected boolean selected;

    public GraphicsItem(Canvas canvas) {
        this.canvas = canvas;
        this.selected = true;
    } 
    
    public abstract void update(Item item);
    
    /**
     * Valinta päälle tai pois.
     */
    public void toggleSelection() {
        selected = !selected;
    }
    
    /**
     * Poista valinta kappaleelta.
     */
    public void unSelect() {
        selected = false;
    }

    /**
     * 
     * @return valittu kappale.
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Onko piste kappaleen alueella?
     * @param point
     * @return
     */
    public abstract boolean contains(Point point);

} 