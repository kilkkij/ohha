package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseDragListener implements MouseMotionListener {
    
    private final ItemBuilder itemBuilder;
    
    public MouseDragListener(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        itemBuilder.drag(me.getX(), me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }
    
    

} 