package physics;
import logic.Vector;
import java.awt.Graphics;
import logic.Lib;

public class AxisAlignedRectangle extends Item {
    
    public final double width;
    public final double height;
    
    public AxisAlignedRectangle(Vector position, double angle, Vector velocity,
             Material material, double width, double height, boolean static_) {
        super(0., position, angle, velocity, material);
        this.width = width;
        this.height = height;
        this.invMass = invMass(static_);
    }
    
    public double invMass(boolean static_) {
        if (static_) {
            return 0.;
        } else {
            return 1./width*height*material.density;
        }
    }
    
    @Override
    public boolean resolveCollision(AxisAlignedRectangle other, double dt,
            Vector gravity, int iterations) {
        
        // suhteellinen sijainti toisesta kappaleesta
        Vector relPos = position.substract(other.position);
        
        // vektori, joka ilmaisee suorakulmioiden jakaman alueen
        Vector overlap = calculateOverlap(other, relPos);
        
        // jätetään tähän, jos kappaleet eivät törmää
        if (!(overlap.getX() > 0 && overlap.getY() > 0)) {
            return false;
        }
        
        // törmäyksen normaali
        Vector normal = normal(overlap, relPos);
        
        // siirretään myöhemmin kappaleita poispäin toisistaan
        overlapCorrection(other, overlap, normal);
        
        // suhteellinen nopeus
        Vector relVel = velocity.substract(other.velocity);
        
        // suhteellisen nopeuden projektio normaalin suuntaan
        double relVelNormalProjection = relVel.dot(normal);
        
        // Jos kappaleet erkanevat toisistaan, ei käsitellä törmäyksenä.
        if (relVelNormalProjection > 0) {
            return false;
        }
        
        // impulssin käsittely
        double elasticity = calculateElasticity(
                relVel, relVelNormalProjection, other, dt, gravity);
        double impulse = calculateImpulse(
                elasticity, relVelNormalProjection, other);
        applyImpulse(normal, impulse, other, dt);
        
        return true;
    }
    
    public Vector normal(Vector overlap, Vector relPos) {
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

    public double calculateImpulse(
            double elasticity, double relVelNormalProjection, Item other) {
        return -(1. + elasticity)*relVelNormalProjection/
                (invMass + other.invMass);
    }
    
    public double calculateElasticity(
            Vector relVel, double relVelNormalProjection, Item other, 
            double dt, Vector gravity) {
        if (relVel.square() > gravity.multiply(dt).square() - Lib.EPSILON) {
            final double elasticityLimit = 0.2;
            if (Math.abs(relVelNormalProjection) > elasticityLimit) {
                return Math.min(material.elasticity, other.material.elasticity);
            }
        }
        return 0.;
    }
    
    public void applyImpulse(Vector normal, double impulse, Item other, 
            double dt) {
        // liikemäärä muuttuu normaalin suuntaan
        velocityIncrement.increment(normal.multiply(impulse*invMass));
        // toisen kappaleen liikkeen muutos on vastakkainen
        other.velocityIncrement.increment(normal.multiply(-impulse*other.invMass));
    }
    
    private void overlapCorrection(Item other, Vector overlap, Vector normal) {
        double sinkCorrectFraction = .5;
        double slop = 0.;
        double penetration = -overlap.dot(normal);
        Vector correction = normal.multiply(
                Math.max(penetration - slop, 0)/
                (invMass + other.invMass)*sinkCorrectFraction);
        warp.increment(correction.multiply(invMass));
        other.warp.increment(correction.multiply(-other.invMass));
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