package physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import logic.Vector;

/**
 * Simulaatio. Askel askeleelta organisoi kappaleiden väliset törmäykset,
 * niiden siirtämiset, pyörittämisen ja painovoiman. Sisältää Kappaleet 
 * järjestetyssä tietorakenteessa.
 * @author juho
 */
public class Simulation {
    
    private final List<Item> items;
    private final Vector gravity;
    private final int iterationCount = 1;

    /**
     * 
     * @param gravity painovoimavektori
     */
    public Simulation(Vector gravity) {
        // Tämä ei liene paras tietorakenne, mutta se tekee parien löytämisen 
        // helpoksi.
        items = new ArrayList<>();
        //
        this.gravity = gravity;
    }
    
    /**
     * Aja askel simulaatiota.
     * @param dt aika-askeleen pituus sekunneissa
     */
    public void step(double dt) {
        resolveCollisions(dt);
        applyCorrections();
        applyImpulses(dt);
        applyMovement(dt);
        clearAccelerations();
    }

    private void resolveCollisions(double dt) {
        // laske törmäyksiä monta kertaa aika-askeleen sisällä
        for (int k = 0; k < iterationCount; k++) {
            // looppi siten, että kaikkia mahdollisia pareja kutsutaan
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
            item.move(dt);
            item.turn(dt);
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
     * Poista viitteet kaikista kappaleista.
     */
    public void clear() {
        items.clear();
    }

    
    /**
     * Lisää esineen.
     * Ei tarkista onko esine jo lisätty.
     * @param item 
     */
    public void addItem(Item item) {
        items.add(item);
    }
    
    /**
     * Poistaa esineen.
     * @param itemToRemove
     */
    public void remove(Item itemToRemove) {
        // käy läpi kaikki esineet ja etsi poistettava
        for (Iterator<Item> it = items.iterator(); it.hasNext();) {
            Item item = it.next();
            if (item.equals(itemToRemove)) {
                it.remove();
                // esineen tulisi esiintyä vain kerran
                return;
            }
        }
    }

} 