package physics;

import java.util.ArrayList;
import java.util.List;
import logic.Vector;

public class Simulation {
    
    private final List<Item> items;
    private final Vector gravity;
    private final int iterationCount = 1;

    public Simulation(Vector gravity) {
        items = new ArrayList<>();
        this.gravity = gravity;
    }
    
    public void step(double dt) {
        resolveCollisions(dt);
        applyMovement(dt);
        applyCorrections();
        applyImpulses(dt);
        clearAccelerations();
    }

    private void resolveCollisions(double dt) {
        for (int k = 0; k < iterationCount; k++) {
            for (int i = 0; i < items.size(); i++) {
                for (int j = i+1; j < items.size(); j++) {
                    items.get(i).resolveCollision(
                            items.get(j), dt, gravity, iterationCount);
                }
            }
        }
    }
    
    private void applyImpulses(double dt) {
        for (Item item: items) {
            item.accelerate(dt, gravity);
        }
    }

    private void applyMovement(double dt) {
        for (Item item: items) {
            item.move(dt, gravity);
        }
    }
    
    private void applyCorrections() {
        for (Item item: items) {
            item.applyOverlapCorrection();
        }
    }
    
    private void clearAccelerations() {
        for (Item item: items) {
            item.clearAcceleration();
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