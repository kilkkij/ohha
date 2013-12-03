package UI;

import logic.Vector;
import physics.ItemRectangle;
import physics.Material;
import physics.SimulationEnvironment;

public class ItemBuilder {
    
    private int startX;
    private int startY;
    private boolean building;
    private final UI userInterface;
    private final SimulationEnvironment simEnv;
    private final Material material;
    
    public ItemBuilder(UI userInterface, SimulationEnvironment simEnv) {
        this.building = false;
        this.userInterface = userInterface;
        this.simEnv = simEnv;
        this.material = new Material(1., .3, .3);
    } 

    public void drag(int x, int y) {
        if (!building) {
            startBuilding(x, y);
            building = true;
        }
    }

    private void startBuilding(int x, int y) {
        building = true;
        startX = x;
        startY = y;
    }

    public void finishBuilding(int finishX, int finishY) {
        if (!building) {
            return;
        }
        building = false;
        Canvas canvas = userInterface.getCanvas();
        double x = canvas.toPhysicsCoordinatesX(startX);
        double cornerX = canvas.toPhysicsCoordinatesX(finishX);
        double y = canvas.toPhysicsCoordinatesY(startY); 
        double cornerY = canvas.toPhysicsCoordinatesY(finishY);
        double width = Math.abs(x - cornerX)*2;
        double height = Math.abs(y - cornerY)*2;
        ItemRectangle item = new ItemRectangle(
                new Vector(x, y), 0., new Vector(0, 0), 0., 
                material, width, height, true);
        item.CHOSEN = true;
        simEnv.getSim().addItem(item);
        
    }

} 