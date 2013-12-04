package physics;

import logic.Lib;
import logic.Vector;

public class RectRectCollision {
    
    public final ItemRectangle A;
    public final ItemRectangle B;
    private ItemRectangle pointItem;
    private ItemRectangle normalItem;
    private Vector point;
    private Vector normal;
    private Vector tangent;
    private double overlap;
    private double relVelNormalProjection;
    private double relVelTangentProjection;
    private Vector totalRelativeVelocity;
    private Vector relVel;
    private double elasticity;
    private double friction;
    private Vector impulse;
    
    public RectRectCollision(ItemRectangle A, ItemRectangle B) {
        this.A = A;
        this.B = B;
    }
    
    public boolean resolve(double dt, Vector gravity, int iterations) {
        if (!happens()) {
            return false;
        }
//        System.out.println("relVel " + relVel);
//        System.out.println("normal " + normal);
//        System.out.println("relVelNormalProjection " + relVelNormalProjection);
        if (relVelNormalProjection > 0) {
//            System.out.println("------collision aborted------");
            return false;
        }
        calculateTangent();
//        System.out.println("tangent " + tangent);
//        System.out.println("relVelTangentProjection " + relVelTangentProjection);
        calculateElasticity(dt, gravity);
        friction = normalItem.material.collisionFriction(pointItem.material);
        calculateImpulses();
        applyImpulse(impulse, pointItem);
        applyImpulse(impulse.multiply(-1), normalItem);
        overlapCorrection();
//        System.out.println("impulse " + impulse);
//        System.out.println("------collision resolved------");
        return true;
    }
    
    private boolean happens() {
        // pidetään kirjaa pienimmän päällekkäisyyden akselista
        overlap = Double.MAX_VALUE;
        boolean AB = collisionAlongNormals(A, B);
        if (!AB) {
            return false;
        }
        boolean BA = collisionAlongNormals(B, A);
        if (!BA) {
            return false;
        }
        // jos ollaan täällä asti, kappaleet ovat toistensa sisällä
        // ja törmäysolio on nyt päivitetty
        // siirretään törmäyspiste normaalia pitkin:
//        point.increment(normal.multiply(overlap));
        return true;
    }
    
    private boolean collisionAlongNormals(ItemRectangle normalItem, 
            ItemRectangle pointItem) {
        // käydään läpi kaikki palikan janat
        // jos normaalin suunnassa kappaleet eivät leikkaa, palautetaan false
        for (Vector n: normalItem.getNormals()) {
            Projection normalProj = normalItem.projection(n);
            Projection pointProj = pointItem.projection(n);
            double o = normalProj.overlapAlongNormal(pointProj);
            // jos ei törmäystä kyseisen normaalin suuntaan --> ei törmäystä
            if (o < 0) {
                return false;
            } 
            // tallennetaan pienimmän päällekkäisyyden törmäystiedot
            else if (o < overlap) {
                calculateCollision(normalItem, pointItem, 
                        normalProj, pointProj, n, o);
            }
        }
        return true;
    }
    
    private void calculateCollision(
            ItemRectangle nItem, ItemRectangle pItem,
            Projection normalProj, Projection pointProj, 
            Vector n, double o) {
        calculateProjections(normalProj, pointProj, n);
        normalItem = nItem;
        pointItem = pItem;
        overlap = o;
        calculateVelocities();
    }
    
    private void calculateProjections(
            Projection normalProj, Projection pointProj, 
            Vector n) {
        double minOverlap = pointOverlap(normalProj.min, normalProj.max, 
                pointProj.min);
        double maxOverlap = pointOverlap(normalProj.min, normalProj.max, 
                pointProj.max);
        if (minOverlap > maxOverlap) {
            point = pointProj.minVertex;
            normal = n.multiply(1);
        } else {
            point = pointProj.maxVertex;
            normal = n.multiply(-1);
        }
    }
    
    private void calculateTangent() {
        tangent = normal.cross(1);
        relVelTangentProjection = totalRelativeVelocity.dot(tangent);
        double flip = Math.copySign(1, relVelTangentProjection);
        tangent.applyMultiplication(flip);
        relVelTangentProjection *= flip;
    }
    
    private void calculateVelocities() {
        relVel = pointItem.velocity.substract(normalItem.velocity);
        totalRelativeVelocity = relVel.add(
                rotationalVelocity(pointItem, point).substract(
                rotationalVelocity(normalItem, point)));
        relVelNormalProjection = normal.dot(totalRelativeVelocity);
    }
    
    private Vector rotationalVelocity(Item item, Vector point) {
        return point.substract(item.position).cross(item.angularVelocity);
    }
    
    private double pointOverlap(double min, double max, double x) {
        return Math.min(max - x, x - min);
    }
    
    private void calculateElasticity(double dt, Vector gravity) {
        // Jos kappaleiden välinen nopeus on painovoiman aiheuttaman luokkaa,
        // ei elastista törmäystä kiitos.
        if (relVel.square() > 2*gravity.multiply(dt).square() - Lib.EPSILON) {
            elasticity = A.material.collisionElasticity(B.material);
        } else {
            elasticity = 0.;
        }
    }
    
    private void calculateImpulses() {
        double normalImpulse = -(1. + elasticity)*relVelNormalProjection/
                (A.invMass + B.invMass + 
                momentumTerm(A, normal) + momentumTerm(B, normal));
        double tangentImpulse = -(1. + elasticity)*relVelTangentProjection/
                (A.invMass + B.invMass +
                momentumTerm(A, tangent) + momentumTerm(B, tangent));
        if (-tangentImpulse > normalImpulse*friction) {
            tangentImpulse = -normalImpulse*friction;
        }
        impulse = tangent.multiply(tangentImpulse).add(
                normal.multiply(normalImpulse));
    }
    
    private double momentumTerm(Item item, Vector vector) {
        Vector relPos = point.substract(item.position);
        return item.invMoment*Math.pow(relPos.cross(vector), 2);
    }
    
    private void applyImpulse(Vector I, Item item) {
        Vector relativeCollisionPoint = point.substract(item.position);
        item.velocityIncrement.increment(I.multiply(item.invMass));
        item.angularVelocityIncrement += 
                item.invMoment*relativeCollisionPoint.cross(I);
    }
    
    private void overlapCorrection() {
        double sinkCorrectFraction = .5;
        double slop = 0.0;
        Vector correction = normal.multiply(
                Math.max(overlap - slop, 0)/
                (A.invMass + B.invMass)*sinkCorrectFraction);
        overlapCorrection(pointItem, correction);
        overlapCorrection(normalItem, correction.multiply(-1));
    }
    
    private void overlapCorrection(ItemRectangle item, Vector correction) {
        if (!item.static_()) {
            item.warp.increment(correction.multiply(item.invMass));
        }
    }

    public Vector getNormal() {
        return normal;
    }

    public double getOverlap() {
        return overlap;
    }

    public double getRelVelNormalProjection() {
        return relVelNormalProjection;
    }

    public Vector getRelVel() {
        return relVel;
    }

    public Vector getPoint() {
        return point;
    }

} 