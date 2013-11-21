package unused;

import unused.CollisionRectangleRectangle;
import logic.Vector;
import physics.Material;

public class Item2 {
    
    private Vector position;
    private double angle;
    private Vector velocity;
    private Vector acceleration;
    public final Material material;
    public final Shape shape;
    public final double mass;

    public Item2(Vector position, double angle, Vector velocity, 
            Material material, Shape shape) {
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;
        this.material = material;
        this.shape = shape;
        mass = shape.calculateMass(material.density);
    }
    
    public void accelerate(double dt, Vector gravity) {
        velocity.add(acceleration.multiply(.5*dt));
        velocity.add(gravity);
    }
    
    public void move(double dt, Vector gravity) {
        position.add(velocity.multiply(dt));
        accelerate(dt, gravity);
    }

    public void resolveCollision(Item2 other, double dt) {
        if (other.shape instanceof Rectangle) {
            CollisionRectangleRectangle collision = 
                    new CollisionRectangleRectangle(this, other);
        }
    }
    
    public void clearAcceleration() {
        acceleration.clear();
    }

    /**
     * @return the position
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @return the velocity
     */
    public Vector getVelocity() {
        return velocity;
    }
    
    public Vector getAcceleration() {
        return acceleration;
    }

} 