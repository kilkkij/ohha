package physics;

import logic.Vector;
import java.awt.Graphics;
import logic.Lib;
import static logic.Lib.EPSILON;

public abstract class Item {
    
    protected Vector position;
    protected double angle;
    protected Vector velocity;
    protected Vector velocityIncrement;
    protected Vector warp;
    protected double invMass;
    protected Material material;
    
    public Item(double mass, Vector position, double angle,
            Vector velocity, Material material) {
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;
        this.velocityIncrement = new Vector();
        this.material = material;
        this.invMass = invMass;
        this.warp = new Vector(0., 0.);
    }
    
    public void accelerate(double dt, Vector gravity) {
        if (invMass > EPSILON) {
            velocity.increment(velocityIncrement);
            velocity.increment(gravity.multiply(dt));
        }
    }
    
    public void clearAcceleration() {
        velocityIncrement.clear();
    }
    
    public void move(double dt, Vector gravity) {
        position.increment(velocity.multiply(dt));
    }
   
    public boolean resolveCollision(
            Item other, double dt, Vector gravity, int iterations) {
        // tällä hetkellä vain AxisAlignedRectangle palikoita
        return resolveCollision(
                (AxisAlignedRectangle) other, dt, gravity, iterations);
    }
    
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

    abstract public void draw(Graphics graphics, double dpu, int canvasWidth, 
            int canvasHeight);

    abstract public boolean resolveCollision(AxisAlignedRectangle rectangle, 
            double dt, Vector gravity, int iterations);
        
}