package physics;
import logic.Vector;
import java.util.ArrayList;
import logic.RectRectCollision;
import logic.Lib;
import logic.Edge;
import logic.Projection;

/**
 * Suorakaide. Määrittelee muodon ja törmäykset toisten kappaleiden kanssa.
 * @author juho
 */
public class ItemRectangle extends Item {
    
    public final double width;
    public final double height;
    private final ArrayList<Vector> vertices;
    private final ArrayList<Vector> normals;
    
    /**
     * Kutsuu yläluokan metodia.
     * @param position
     * @param angle
     * @param velocity
     * @param angularVelocity
     * @param material
     * @param width
     * @param height
     * @param static_ jos true, kappale ei reagoi törmäyksiin
     */
    public ItemRectangle(Vector position, double angle, Vector velocity, 
            double angularVelocity,
            Material material, double width, double height, boolean static_) {
        super(position, angle, velocity, angularVelocity, material);
        this.width = width;
        this.height = height;
        this.invMass = invMass(static_, width, height, material.density);
        this.invMoment = invInertia(static_, width, height, material.density);
        this.vertices = vertices(position, width, height, angle);
        this.normals = normals(position, width, height, angle);
    }
    
    public static ArrayList<Edge> edges(double width, double height) {
        //
        ArrayList<Edge> edges = new ArrayList<>();
        // kansi
        edges.add(new Edge(new Vector(0., height/2), new Vector(0., width)));
        // pohja
        edges.add(new Edge(new Vector(0., -height/2), new Vector(0., -width)));
        // vasen
        edges.add(new Edge(new Vector(width/2., 0.), new Vector(-height, 0.)));
        // oikea
        edges.add(new Edge(new Vector(width/2., 0.), new Vector(height, 0.)));
        //
        return edges;
    }
    
    public static ArrayList<Vector> normals(Vector pos, 
            double width, double height, double angle) {
        //
        ArrayList<Vector> normals = new ArrayList<>();
        // kansi
        normals.add(new Vector(0., 1));
        // pohja
        normals.add(new Vector(0., -1));
        // vasen
        normals.add(new Vector(-1, 0.));
        // oikea
        normals.add(new Vector(1, 0.));
        // käännä 
        applyRotation(normals, angle);
        
        return normals;
    }
    
    public static ArrayList<Vector> vertices(
            Vector pos, double width, double height, double angle) {
        //
        ArrayList<Vector> vertices = new ArrayList<>();
        // yläoikea
        vertices.add(new Vector(width/2, height/2));
        // ylävasen
        vertices.add(new Vector(-width/2, height/2));
        // alavasen
        vertices.add(new Vector(-width/2, -height/2));
        // alaoikea
        vertices.add(new Vector(width/2, -height/2));
        // käännä
        applyRotation(vertices, angle);
        
        return vertices;
    }
    
    /**
     * Käännä.
     * @param vectors Iteroitavat vektorit
     * @param angle Käännettävä kulma (radiaaneissa)
     */
    public static void applyRotation(Iterable<Vector> vectors, double angle) {
        for (Vector vector: vectors) {
            vector.applyRotation(angle);
        }
    }
    
    public static double invMass(boolean static_, 
            double width, double height, double density) {
        if (static_) {
            return 0.;
        } else {
            return 1./(width*height*density);
        }
    }
    
    public static double invInertia(boolean static_, 
            double width, double height, double density) {
        if (static_) {
            return 0;
        } else {
            double inertia = width*height*density/12*
                    (width*width + height*height);
            return 1./inertia;
        }
    }
    
    @Override
    public void turn(double dt) {
        super.turn(dt);
        applyRotation(vertices, angularVelocity*dt);
        applyRotation(normals, angularVelocity*dt);
    }
    
    @Override
    public boolean resolveCollision(ItemRectangle other, 
            double dt, Vector gravity, int iterations) {
        RectRectCollision collision = new RectRectCollision(this, other);
        if (!collisionHappens(collision)) {
            return false;
        }
//        overlapCorrection(other, collision.getNormal(), collision.getOverlap());
        Vector relVel = velocity.substract(other.velocity);
        Vector rotVel = collision.getPoint().substract(position).
                cross(angularVelocity);
        Vector otherRotVel = collision.getPoint().substract(other.position).
                cross(other.angularVelocity);
        Vector totVel = relVel.add(rotVel.substract(otherRotVel));
        System.out.println("totvel " + totVel);
        double relVelNormalProjection = collision.getNormal().dot(totVel);
        if (relVelNormalProjection > 0) {
            System.out.println("relVelNormalProjection " + relVelNormalProjection);
            System.out.println("normal " + collision.getNormal());
            System.out.println("relVel " + relVel);
            return false;
        }
        double elasticity = calculateElasticity(
                relVel, relVelNormalProjection, other, dt, gravity);
        double impulse = calculateRotatingImpulse(
                elasticity, relVelNormalProjection, other, collision);
        applyRotatingImpulse(collision, impulse, this);
        applyRotatingImpulse(collision, -impulse, other);
        System.out.println("----collision resolved-----");
        return true;
    }
    
    private boolean collisionHappens(RectRectCollision collision) {
        // pidetään kirjaa pienimmän päällekkäisyyden akselista
        collision.setOverlap(Double.MAX_VALUE);
        boolean AB = collisionAlongNormals(collision, collision.A, collision.B);
        if (!AB) {
            return false;
        }
        boolean BA = collisionAlongNormals(collision, collision.B, collision.A);
        if (!BA) {
            return false;
        }
        // jos ollaan täällä asti, kappaleet ovat toistensa sisällä
        // ja törmäysolio on nyt päivitetty
        // määrittele törmäyspiste:
        System.out.println("overlap at " + collision.getPoint());
        System.out.println("normal " + collision.getNormal());
        collision.getPoint().increment(
                collision.getNormal().multiply(collision.getOverlap()));
        return true;
    }
    
    private boolean collisionAlongNormals(RectRectCollision collision, 
            ItemRectangle A, ItemRectangle B) {
        // käydään läpi kaikki palikan janat
        // jos normaalin suunnassa kappaleet eivät leikkaa, palautetaan false
        for (Vector normal: A.normals) {
            Projection projA = A.projection(normal);
            Projection projB = B.projection(normal);
            double overlap = overlapAlongNormal(projA, projB);
            // jos ei törmäystä kyseisen normaalin suuntaan --> ei törmäystä
            if (overlap < 0) {
                return false;
            } 
            // tallennetaan pienimmän päällekkäisyyden törmäystiedot
            else if (overlap < collision.getOverlap()) {
                setCollision(collision, projA, projB, overlap, normal);
            }
        }
        return true;
    }
    
    private void setCollision(RectRectCollision collision, Projection A, 
            Projection B, double overlap, Vector normal) {
        double minOverlap = pointOverlap(A.min, A.max, B.min);
        double maxOverlap = pointOverlap(A.min, A.max, B.max);
        if (minOverlap > maxOverlap) {
            collision.setPoint(B.minVertex);
            collision.setNormal(normal.multiply(1));
        } else {
            collision.setPoint(B.maxVertex);
            collision.setNormal(normal.multiply(1));
        }
        collision.setOverlap(overlap);
    }
    
    private double overlapAlongNormal(Projection A, Projection B) {
        return Math.min(B.max - A.min, A.max - B.min);
    }
    
    private double pointOverlap(double min, double max, double x) {
        return Math.min(max - x, x - min);
    }
    
    private Projection projection(Vector normal) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        Vector minVertex = null;
        Vector maxVertex = null;
        // etsi minimi ja maksimi normaalivektorin suunnassa
        // ja sitä vastaavat monikulmion kulmat
        for (Vector vertex: getVertices()) {
            Vector absVertex = vertex.add(position);
            double dot = absVertex.dot(normal);
            if (dot < min) {
                min = dot;
                minVertex = absVertex;
            } if (dot > max) {
                max = dot;
                maxVertex = absVertex;
            }
        }
        return new Projection(min, max, minVertex, maxVertex);
    }
    
    private double calculateRotatingImpulse(double elasticity, 
            double relVelNormalProjection, Item other, RectRectCollision collision) {
        return -(1. + elasticity)*relVelNormalProjection/
                (invMass + other.invMass + 
                momentumTerm(this, collision) + 
                momentumTerm(other, collision));
    }
    
    private double momentumTerm(Item item, RectRectCollision collision) {
        Vector relPos = collision.getPoint().substract(item.position);
        Vector normal = collision.getNormal();
        return item.invMoment*Math.pow(relPos.cross(normal), 2);
    }
    
    private void applyRotatingImpulse(RectRectCollision collision, 
            double impulse, Item item) {
        Vector normal = collision.getNormal();
        Vector relativeCollisionPoint = 
                collision.getPoint().substract(item.position);
        item.velocityIncrement.increment(
                normal.multiply(impulse*item.invMass));
//        System.out.println("relColCross " + relativeCollisionPoint.cross(normal));
        item.angularVelocityIncrement += 
                item.invMoment*relativeCollisionPoint.cross(normal)*impulse;
//        System.out.println("rel col point " + relativeCollisionPoint);
    }
    
//    @Override
    public boolean resolveCollision_old(ItemRectangle other, double dt,
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
        overlapCorrection(other, normal, -overlap.dot(normal));
        
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
    public Vector calculateOverlap(ItemRectangle other, Vector relPos) {
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
        // Jos kappaleiden välinen nopeus on painovoiman aiheuttaman luokkaa,
        // ei elastista törmäystä kiitos.
        if (relVel.square() > gravity.multiply(dt).square() - Lib.EPSILON) {
            // hatusta heitetty kaava, jolla keksitään elastisuus 
            // kahden kappaleen välisessä törmäyksessä
            return Math.min(material.elasticity, other.material.elasticity);
        }
        return 0.;
    }
    
    public void applyImpulse(Vector normal, double impulse, Item other, 
            double dt) {
        // liikemäärä muuttuu normaalin suuntaan
        velocityIncrement.increment(normal.multiply(impulse*invMass));
        // toisen kappaleen liikkeen muutos on vastakkainen
        other.velocityIncrement.increment(
                normal.multiply(-impulse*other.invMass));
    }
    
    private void overlapCorrection(Item other, Vector normal, 
            double penetration) {
        double sinkCorrectFraction = .5;
        double slop = 0.;
        Vector correction = normal.multiply(
                Math.max(penetration - slop, 0)/
                (invMass + other.invMass)*sinkCorrectFraction);
        warp.increment(correction.multiply(invMass));
        other.warp.increment(correction.multiply(-other.invMass));
    }

    /**
     * @return the vertices
     */
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

}