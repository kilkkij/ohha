package physics;

import logic.Vector;

public class CollisionRectangleRectangle {
    
    Item2 A;
    Item2 B;
    Rectangle Arect;
    Rectangle Brect;
    Vector relPos;
    Vector overlap;
    Vector normal;
    Vector relVel;
    double elasticity;
    
    public CollisionRectangleRectangle(Item2 A, Item2 B) {
        Arect = (Rectangle) A.shape;
        Brect = (Rectangle) B.shape;
    }
    
    public void resolveCollision(double dt) {
        
        // suhteellinen sijainti toisesta kappaleesta
        relPos = A.getPosition().substract(B.getPosition());
        
        // vektori, joka ilmaisee suorakulmioiden jakaman alueen
        calculateOverlap();
        
        // jätetään tähän, jos kappaleet eivät törmää
        if (!(overlap.getX() > 0 && overlap.getY() > 0)) {
            return;
        }
        
        // törmäyksen normaali
        calculateNormal(overlap, relPos);
        
        // suhteellinen nopeus
        relVel = A.getVelocity().substract(B.getVelocity());
        
        // nopeuden projektio normaalin suuntaan
        double relVelNormalProjection = relVel.dot(normal);
        
        // Jos kappaleet erkanevat toisistaan, ei käsitellä törmäyksenä.
        if (relVelNormalProjection > 0) {
            return;
        }
        
        // impulssin käsittely
        double impulse = calculateImpulse(relVelNormalProjection);
        applyImpulse(normal, impulse, dt);
    }
    
    public void calculateNormal(Vector overlap, Vector relPos) {
        if (overlap.getX() < overlap.getY()) {
            normal = new Vector(Math.copySign(1, relPos.getX()), 0);
        }
        normal = new Vector(0, Math.copySign(1, relPos.getY()));
    }
    
    public void calculateOverlap() {
        double x_overlap = (Arect.width + Arect.width)/2 - 
                Math.abs(relPos.getX());
        double y_overlap = (Brect.height + Brect.height)/2 - 
                Math.abs(relPos.getY());
        relPos = new Vector(x_overlap, y_overlap);
    }
    
    public double calculateImpulse(double relVelNormalProjection) {
        elasticity = calculateElasticity(relVelNormalProjection);
        return -(1. + elasticity)*relVelNormalProjection/
                (1./A.mass + 1./B.mass);
    }
    
    public double calculateElasticity(double relVelNormalProjection) {
        final double eLimitVelocity = 0.2;
        if (Math.abs(relVelNormalProjection) > eLimitVelocity) {
            return Math.min(A.material.elasticity, B.material.elasticity);
        } else {
            return .0;
        }
    }
    
    public void applyImpulse(Vector normal, double impulse, double dt) {
        // liikemäärä muuttuu normaalin suuntaan
        A.getAcceleration().increment(normal.multiply(impulse/A.mass));
        // toisen kappaleen liikkeen muutos on vastakkainen
        B.getAcceleration().increment(normal.multiply(-impulse/B.mass));
    }

    public void sinkCorrection() {
        double sinkCorrectFraction = .5;
        double slop = 0.02;
        double penetration = -overlap.dot(normal);
        Vector correction = normal.multiply(
                Math.max(penetration - slop, 0)/
                (1./A.mass + 1./B.mass)*sinkCorrectFraction);
        A.getPosition().increment(correction.multiply(1./A.mass));
        B.getPosition().increment(correction.multiply(-1./B.mass));
    }
    
} 