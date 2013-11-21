package physics;
import logic.Vector;
import java.util.ArrayList;
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
    private ArrayList<Vector> vertices;
    private ArrayList<Vector> normals;
    
    /**
     * Kutsuu yläluokan metodia.
     * @param position
     * @param angle
     * @param velocity
     * @param material
     * @param width
     * @param height
     * @param static_ jos true, kappale ei reagoi törmäyksiin
     */
    public ItemRectangle(Vector position, double angle, Vector velocity,
             Material material, double width, double height, boolean static_) {
        super(0., position, angle, velocity, material);
        this.width = width;
        this.height = height;
        this.invMass = invMass(static_, width, height, material.density);
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
        // käännä ensin
        applyRotation(vertices, angle);
        // sitten siirrä paikalleen
        applyTranslation(vertices, pos);
        //
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

    /**
     * Siirrä.
     * @param vectors iteroitavat vektorit
     * @param increment vektori, joka lisätään kaikkiin
     */
    public static void applyTranslation(
            Iterable<Vector> vectors, Vector increment) {
        for (Vector vector: vectors) {
            vector.increment(increment);
        }
    }
    
    public static double invMass(boolean static_, 
            double width, double height, double density) {
        if (static_) {
            return 0.;
        } else {
            return 1./width*height*density;
        }
    }
    
    public static double invInertia(boolean static_, double mass, 
            double width, double height) {
        if (static_) {
            return 0;
        } else {
            return mass/12.*(width*width + height*height);
        }
    }
    
    /**
     * 
     * @param other
     * @return true, jos palikat törmäävät.
     */
    public boolean collidesWith(ItemRectangle other) {
        // käydään läpi kaikki palikan janat
        // jos normaalin suunnassa kappaleet eivät leikkaa, palautetaan false
        for (Vector normal: normals) {
            if (overlapAlongNormal(normal, other) < 0) {
                return false;
            }
        }
        // käydään läpi kaikki toisen palikan janat
        for (Vector normal: other.normals) {
            if (other.overlapAlongNormal(normal, this) < 0) {
                return false;
            }
        }
        return true;
    }
    
    private double overlapAlongNormal(Vector normal, ItemRectangle other) {
        Projection proj = projection(normal);
        Projection otherProj = other.projection(normal);
        return Math.min(otherProj.max - proj.min, proj.max - otherProj.min);
    }
    
    private Projection projection(Vector normal) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        Vector minVertex = new Vector();
        Vector maxVertex = new Vector();
        for (Vector vertex: getVertices()) {
            double dot = vertex.dot(normal);
            if (dot < min) {
                min = dot;
                minVertex = vertex;
            } if (dot > max) {
                max = dot;
                maxVertex = vertex;
            }
        }
        return new Projection(min, max, minVertex, maxVertex);
    }
    
    @Override
    public boolean resolveCollision(ItemRectangle other, double dt,
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
        other.velocityIncrement.increment(
                normal.multiply(-impulse*other.invMass));
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

    /**
     * @return the vertices
     */
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

}