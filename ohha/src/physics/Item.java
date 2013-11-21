package physics;

import logic.Vector;
import java.awt.Graphics;
import logic.Lib;
import static logic.Lib.EPSILON;

/**
 * Kappale, joista simulaatio koostuu.
 * @author juho
 */
public abstract class Item {
    
    protected Vector position;
    protected double angle;
    protected Vector velocity;
    protected Vector velocityIncrement;
    protected Vector warp;
    protected double invMass;
    protected double invInertia;
    protected Material material;
    
    /**
     * 
     * @param invMass massan käänteisluku
     * @param position sijaintivektori
     * @param angle kulma radiaaneissa
     * @param velocity nopeusvektori
     * @param material materiaaliolio
     */
    public Item(double invMass, Vector position, double angle,
            Vector velocity, Material material) {
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;
        this.velocityIncrement = new Vector();
        this.material = material;
        this.invMass = invMass;
        this.warp = new Vector(0., 0.);
    }
    
    /**
     * Kiihdytä esineitä
     * @param dt aika-askel
     * @param gravity painovoima
     */
    public void accelerate(double dt, Vector gravity) {
        if (invMass > EPSILON) {
            velocity.increment(velocityIncrement);
            velocity.increment(gravity.multiply(dt));
        }
    }
    
    /**
     * Tyhjennä lisättävä nopeusvektori
     */
    public void clearAcceleration() {
        velocityIncrement.clear();
    }
    
    /**
     * Siirrä esineitä nopeusvektorin määrittämään suuntaan
     * @param dt aika-askel
     * @param gravity painovoimavektori
     */
    public void move(double dt, Vector gravity) {
        position.increment(velocity.multiply(dt));
    }
   
    /**
     * 
     * @param other toinen esine
     * @param dt aika-askel
     * @param gravity painovoimavektori
     * @param iterations iteraatioiden määrä
     * @return
     */
    public boolean resolveCollision(
            Item other, double dt, Vector gravity, int iterations) {
        // tällä hetkellä vain AxisAlignedRectangle palikoita
        return resolveCollision(
                (ItemRectangle) other, dt, gravity, iterations);
    }
    
    /**
     * Siirrä kappaleita poispäin toisistaan
     */
    public void applyOverlapCorrection() {
        position.increment(warp);
        warp.clear();
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public Vector getVelocity() {
        return velocity;
    }

    abstract public boolean resolveCollision(ItemRectangle rectangle, 
            double dt, Vector gravity, int iterations);
        
}