package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Hiiren vedon kuuntelija.
 * @author juho
 */
public class MouseDragListener implements MouseMotionListener {
    
    private final ItemBuilder itemBuilder;
    
    /**
     *
     * @param itemBuilder
     */
    public MouseDragListener(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        itemBuilder.update(me.getX(), me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }
    
    

} 