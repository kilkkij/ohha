package physics;

import logic.Vector;
import java.awt.Graphics;
import static logic.Lib.EPSILON;

public abstract class Item {
    
    protected Vector position;
    protected Vector warp;
    protected Vector velocity;
    protected Vector acceleration;
    protected double invMass;
    
    public Item(double mass, Vector pos) {
        this(mass, pos, new Vector());
    }
    
    public Item(double mass, Vector pos, Vector velocity) {
        this.position = pos;
        this.velocity = velocity;
        this.acceleration = new Vector();
        this.warp = new Vector();
        setInverseMass(mass);
    }
    
    private void setInverseMass(double mass) {
        if (mass < EPSILON) {
            invMass = 0;
        } else {
            invMass =  1./mass;
        }
    }
    
    public void step(double dt, double g) {
//        velocity.increment(acceleration.multiply(dt));
//        applyGravity(dt, g);
        position.increment(velocity.multiply(dt));
//        position.increment(warp);
//        acceleration.clear();
//        warp.clear();
    }
    
    public Vector getPos() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }

    public void applyGravity(double dt, double g) {
        if (invMass > EPSILON) {
            acceleration.setY(-g);
            velocity.increment(acceleration.multiply(dt));
        }
    }
   
    public boolean resolveCollision(Item other, double dt) {
        // tällä hetkellä vain AxisAlignedRectangle palikoita
        return resolveCollision((AxisAlignedRectangle) other, dt);
    }
    
    abstract public void draw(Graphics graphics, double dpu, int canvasWidth, 
            int canvasHeight);

    abstract public boolean resolveCollision(AxisAlignedRectangle rectangle, 
            double dt);
        
}