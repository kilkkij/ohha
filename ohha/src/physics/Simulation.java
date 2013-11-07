package physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation {
    
    private final List<Item> items;
    private final double g;

    public Simulation(double g) {
        items = new ArrayList<>();
        this.g = g;
    }
    
    public void step(double dt) {
        resolveCollisions(dt);
//        applyMovement(dt);
    }

    private void resolveCollisions(double dt) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i+1; j < items.size(); j++) {
//                System.out.println("" + i + ", " + j);
                items.get(i).resolveCollision(items.get(j), dt);
            }
            items.get(i).applyGravity(dt, g);
            items.get(i).step(dt, g);
        }
    }

    private void applyMovement(double dt) {
        for (Item item: items) {
            item.step(dt, g);
        }
    }
    
    /**
     * Lisää esineen.
     * Ei tarkista onko esine jo lisätty.
     * @param item 
     */
    public void addItem(Item item) {
        items.add(item);
    }

    public Iterable<Item> getItems() {
        return items;
    }

} 