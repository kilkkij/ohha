package physics;

import logic.Vector;
import java.awt.Graphics;
import static logic.Lib.EPSILON;

public abstract class Item {
    
    protected Vector pos;
    protected Vector velocity;
    protected double invMass;
    
    public Item(double mass, Vector pos) {
        this(mass, pos, new Vector());
    }
    
    public Item(double mass, Vector pos, Vector velocity) {
        this.pos = pos;
        this.velocity = velocity;
        setInverseMass(mass);
    }
    
    private void setInverseMass(double mass) {
        if (mass < EPSILON) {
            invMass = 0;
        } else {
            invMass =  1./mass;
        }
    }
    
    public void step(double dt) {
        pos.increment(velocity.multiply(dt));
    }
    
    public Vector getPos() {
        return pos;
    }

    public void apply_gravity(double dt, double g) {
        if (invMass > EPSILON) {
            velocity.increment(new Vector(0., -g*dt));
        }
    }
   
    public boolean resolveCollision(Item other) {
        if (other instanceof Rectangle) {
            return resolveCollision((Rectangle) other);
        }
        return false;
    }
    
    abstract public void draw(Graphics graphics, double dpu, int canvasWidth, 
            int canvasHeight);

    abstract public boolean resolveCollision(Rectangle rectangle);
        
}