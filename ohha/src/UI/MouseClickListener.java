package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import logic.Vector;
import physics.Item;
import physics.ItemRectangle;
import physics.SimulationEnvironment;

public class MouseClickListener implements MouseListener {
    private final UI userInterface;
    
    private final SimulationEnvironment simEnv;
    ItemBuilder itemBuilder;

    public MouseClickListener(UI userInterface,
            SimulationEnvironment simEnv, ItemBuilder itemBuilder) {
        this.userInterface = userInterface;
        this.simEnv = simEnv;
        this.itemBuilder = itemBuilder;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        double x = userInterface.getCanvas().toPhysicsCoordinatesX(me.getX());
        double y = userInterface.getCanvas().toPhysicsCoordinatesY(me.getY());
        boolean somethingWasClicked = false;
        for (Item item: simEnv.getItems()) {
            if (item instanceof ItemRectangle) {
                if (pointWithin(x, y, (ItemRectangle) item)) {
                    somethingWasClicked = true;
                    if (userInterface.isSelected(item)) {
                        userInterface.unSelect(item);
                    } else {
                        userInterface.select(item);
                    }
                }
            }
        }
        if (!somethingWasClicked) {
            for (Item item: simEnv.getItems()) {
                userInterface.unSelect(item);
            }
        }
    }
    
    /**
     * Tarkistetaan onko piste kulmion sisällä. 
     * Algoritmi W. Randolph Franklin:in mukaan.
     * @param x
     * @param y
     * @param item suorakaide
     * @return
     */
    public boolean pointWithin(double x, double y, ItemRectangle item) {
        boolean result = false;
        ArrayList<Vector> vertices = item.getVertices();
        double xPos = item.getPosition().getX();
        double yPos = item.getPosition().getY();
        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
            double vix = vertices.get(i).getX() + xPos;
            double viy = vertices.get(i).getY() + yPos;
            double vjx = vertices.get(j).getX() + xPos;
            double vjy = vertices.get(j).getY() + yPos;
            if ((viy > y) != (vjy > y) &&
                    (x < (vjx - vix) * 
                    (y - viy) / 
                    (vjy - viy) + 
                    vix)) {
               result = !result;
            }
        }
        return result;
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