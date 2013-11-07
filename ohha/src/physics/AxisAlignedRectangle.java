package physics;

import logic.Vector;
import java.awt.Graphics;

public class AxisAlignedRectangle extends Item {
    
    private final double width;
    private final double height;

    public AxisAlignedRectangle(double mass, Vector pos, 
            double width, double height) {
        super(mass, pos);
        this.width = width;
        this.height = height;
    }
    
    public AxisAlignedRectangle(double mass, Vector pos, Vector speed,
            double width, double height) {
        super(mass, pos, speed);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean resolveCollision(AxisAlignedRectangle other, double dt) {
        Vector relPos = position.substract(other.position);
        Vector overlap = calculateOverlap(other, relPos);
        if (!(overlap.getX() > 0 && overlap.getY() > 0)) {
            // ei törmäystä
            return false;
        }
        Vector normal = calculateNormal(overlap, relPos);
        sinkCorrection(other, overlap, normal);
        Vector relVel = velocity.substract(other.velocity);
        double relVelNormalProjection = relVel.dot(normal);
        if (relVelNormalProjection > 0) {
            // Kappaleet erkanevat toisistaan.
            // Ei siis käsitellä törmäyksenä.
            return false;
        }
        double impulse = calculateImpulse(relVelNormalProjection, other);
        applyImpulse(normal, impulse, other, dt);
        return true;
    }
    
    public Vector calculateNormal(Vector overlap, Vector relPos) {
        if (overlap.getX() < overlap.getY()) {
            return new Vector(Math.copySign(1, relPos.getX()), 0);
        }
        return new Vector(0, Math.copySign(1, relPos.getY()));
    }
    
    /**
     * Laskee vektorin (dx, dy) missä
     * dx on x-akselin suuntainen kappaleiden jakama pituus ja
     * dy on y-akselin suuntainen kappaleiden jakama pituus
     * kappaleeseen päin (pois päin toisesta).
     * @param other
     * @param relPos
     * @return
     */
    public Vector calculateOverlap(AxisAlignedRectangle other, Vector relPos) {
        double x_overlap = (width + other.width)/2 - Math.abs(relPos.getX());
        double y_overlap = (height + other.height)/2 - Math.abs(relPos.getY());
        return new Vector(x_overlap, y_overlap);
    }

    public double calculateImpulse(double relVelNormalProjection, Item other) {
        double elasticity = calculateElasticity(relVelNormalProjection);
        return -(1. + elasticity)*relVelNormalProjection/
                (invMass + other.invMass);
    }
    
    public double calculateElasticity(double relVelNormalProjection) {
        final double elasticityLimit = 0.2;
        if (Math.abs(relVelNormalProjection) > elasticityLimit) {
            return .5;
        } else {
            return .0;
        }
    }
    
    public void applyImpulse(Vector normal, double impulse, Item other, 
            double dt) {
        // liikemäärä muuttuu normaalin suuntaan
        velocity.increment(normal.multiply(impulse*invMass));
        // toisen kappaleen liikkeen muutos on vastakkainen
        other.velocity.increment(normal.multiply(-impulse*other.invMass));
    }
    
    private void sinkCorrection(Item other, Vector overlap, Vector normal) {
        double sinkCorrectFraction = .5;
        double slop = 0.;
        double penetration = -overlap.dot(normal);
        Vector correction = normal.multiply(
                Math.max(penetration - slop, 0)/
                (invMass + other.invMass)*sinkCorrectFraction);
        position.increment(correction.multiply(invMass));
        other.position.increment(correction.multiply(-other.invMass));
    }
    
    @Override
    public void draw(Graphics graphics, double dpu, int canvasWidth, 
            int canvasHeight) {
        int x = toCanvasCoordinatesX(position.getX(), dpu, canvasWidth);
        int y = toCanvasCoordinatesY(position.getY(), dpu, canvasHeight);
        int dx = (int) (width*dpu);
        int dy = (int) (height*dpu);
//        graphics.fillRect(x, y, dx, dy);
        graphics.drawRect(x, y, dx, dy);
    }
    
    public int toCanvasCoordinatesX(double x, double dpu, int canvasWidth) {
        return canvasWidth/2 + (int) (dpu*(x - width/2));
    }

    public int toCanvasCoordinatesY(double y, double dpu, int canvasHeight) {
        return canvasHeight/2 - (int) (dpu*(y + height/2));
    }

}