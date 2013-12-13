package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Hiiren painallusten kuuntelija.
 * @author juho
 */
public class MouseClickListener implements MouseListener {
    private final UI userInterface;
    
    ItemBuilder itemBuilder;

    public MouseClickListener(UI userInterface, ItemBuilder itemBuilder) {
        this.userInterface = userInterface;
        this.itemBuilder = itemBuilder;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        userInterface.getCanvas().applySelectionOnPoint(me.getPoint());
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        itemBuilder.finishBuilding(me.getX(), me.getY());
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

} 