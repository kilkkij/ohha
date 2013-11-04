package physics;

import logic.Vector;
import java.awt.Graphics;

public class Rectangle extends Item {
    
    private final double width;
    private final double height;

    public Rectangle(double mass, Vector pos, double width, double height) {
        super(mass, pos);
        this.width = width;
        this.height = height;
    }
    
    public Rectangle(double mass, Vector pos, Vector speed,
            double width, double height) {
        super(mass, pos, speed);
        this.width = width;
        this.height = height;
    }
    
    public boolean collidesWith(Rectangle other) {
        Vector diff = pos.substract(other.pos);
        return
            Math.abs(diff.getX()) < (width + other.width)/2 &
            Math.abs(diff.getY()) < (height + other.height)/2;
    }
    
    @Override
    public boolean resolveCollision(Rectangle other) {
        if (!collidesWith(other)) {
            return false;
        }
        Vector relVel = velocity.substract(other.velocity);
        Vector relPos = pos.substract(other.pos);
        Vector normal;
        double penetration;
        double x_overlap = (width + other.width)/2 - Math.abs(relPos.getX());
        double y_overlap = (height + other.height)/2 - Math.abs(relPos.getY());
        if (x_overlap < y_overlap) {
            normal = new Vector(Math.copySign(1, relPos.getX()), 0);
            penetration = x_overlap;
        } else {
            normal = new Vector(0, Math.copySign(1, relPos.getY()));
            penetration = y_overlap;
        }
        double relVelNormalProjection = relVel.dot(normal);
        if (relVelNormalProjection > 0) {
            // Kappaleet erkanevat toisistaan.
            // Ei siis käsitellä törmäyksenä.
            return false;
        }
        // elastinen törmäys
//        System.out.println("törmäys: ");
//        System.out.println("    relPos: " + relPos);
//        System.out.println("    width: " + width);
//        System.out.println("    other width: " + other.width);
//        System.out.println("    x_overlap: " + x_overlap);
//        System.out.println("    y_overlap: " + y_overlap);
//        System.out.println("    nopeus ennen: " + velocity);
        double impulse = -(1. + .5)*relVelNormalProjection/(invMass + other.invMass);
        velocity.increment(normal.multiply(impulse*invMass));
        other.velocity.increment(normal.multiply(-impulse*other.invMass));
        // estä kappaleiden toisiinsa valuminen
        sinkCorrection(other, penetration, normal);
//        System.out.println("    nopeus jälkeen: " + velocity);
//        System.out.println("    normaali: " + normal);
//        System.out.println("    impulssi: " + impulse);
        return true;
    }
    
    private void sinkCorrection(Item other, double penetration, 
            Vector normal) {
        double sinkCorrectFraction = .2;
        Vector correction = normal.multiply(
                penetration/(invMass + other.invMass)*sinkCorrectFraction);
        pos.increment(correction.multiply(invMass));
        other.pos.increment(correction.multiply(-other.invMass));
    }
    
    @Override
    public void draw(Graphics graphics, double dpu, int canvasWidth, 
            int canvasHeight) {
        int x = toCanvasCoordinatesX(pos.getX(), dpu, canvasWidth);
        int y = toCanvasCoordinatesY(pos.getY(), dpu, canvasHeight);
        int dx = (int) (width*dpu);
        int dy = (int) (height*dpu);
        graphics.fillRect(x, y, dx, dy);
    }
    
    public int toCanvasCoordinatesX(double x, double dpu, int canvasWidth) {
        return canvasWidth/2 + (int) (dpu*(x - width/2));
    }

    public int toCanvasCoordinatesY(double y, double dpu, int canvasHeight) {
        return canvasHeight/2 - (int) (dpu*(y + height/2));
    }

}