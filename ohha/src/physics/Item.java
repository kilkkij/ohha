package physics;

import logic.Vector;
import static logic.Lib.EPSILON;

/**
 * Kappale, joista simulaatio koostuu.
 * @author juho
 */
public abstract class Item {
    
    protected Vector position;
    protected double angle;
    protected Vector velocity;
    protected double angularVelocity;
    protected Vector velocityIncrement;
    protected double angularVelocityIncrement;
    protected Vector warp;
    protected double invMass;
    protected double invMoment;
    protected Material material;
    
    /**
     * 
     * @param position sijaintivektori
     * @param angle kulma radiaaneissa
     * @param velocity nopeusvektori
     * @param angularVelocity
     * @param material materiaaliolio
     */
    public Item(Vector position, double angle,
            Vector velocity, double angularVelocity, Material material) {
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;
        this.velocityIncrement = new Vector();
        this.angularVelocity = angularVelocity;
        this.angularVelocityIncrement = 0;
        this.material = material;
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
            angularVelocity += angularVelocityIncrement;
        }
    }
    
    /**
     * Tyhjennä lisättävä nopeusvektori
     */
    public void clearAcceleration() {
        velocityIncrement.clear();
        angularVelocityIncrement = 0;
    }
    
    /**
     * Siirrä esineitä nopeusvektorin määrittämään suuntaan
     * @param dt aika-askel
     */
    public void move(double dt) {
        position.increment(velocity.multiply(dt));
//        System.out.println("position of " + invMass + ": " + position);
    }
    
    /**
     * Käännä kappaletta
     * @param dt aika-askel
     */
    public void turn(double dt) {
        rotate(angularVelocity*dt);
    }
    
    public void rotate(double rotationAngle) {
        angle += rotationAngle;
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
        // tällä hetkellä vain laatikoita
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
    
    public void applyImpulse(Vector point, Vector I) {
        Vector relativeCollisionPoint = point.substract(position);
        velocityIncrement.increment(I.multiply(invMass));
        angularVelocityIncrement += 
                invMoment*relativeCollisionPoint.cross(I);
    }
    
    /**
     *
     * @param correction
     */
    public void overlapCorrection(Vector correction) {
        if (!isStatic()) {
            warp.increment(correction.multiply(invMass));
        }
    }
    
    public boolean isStatic() {
        return invMass < EPSILON;
    }
    
    public void setStatic() {
        invMass = 0;
        invMoment = 0;
        velocity.clear();
        velocityIncrement.clear();
        angularVelocity = 0;
        angularVelocityIncrement = 0.;
        warp.clear();
    }
    
    public void setFree() {
        invMass = invMass();
        invMoment = invMoment();
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
    
    abstract public double invMass();
    
    abstract public double invMoment();
    
    abstract public boolean resolveCollision(ItemRectangle rectangle, 
            double dt, Vector gravity, int iterations);

}