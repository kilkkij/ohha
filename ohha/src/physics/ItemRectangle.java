package physics;
import logic.Vector;
import java.util.ArrayList;

/**
 * Suorakaide.
 * @author juho
 */
public class ItemRectangle extends Item {
    
    public final double WIDTH;
    public final double HEIGHT;
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
        this.WIDTH = width;
        this.HEIGHT = height;
        this.invMass = invMass(static_, width, height, material.density);
        this.invMoment = invMoment(static_, width, height, material.density);
        this.vertices = vertices(position, width, height, angle);
        this.normals = normals(position, width, height, angle);
    }
    
    /**
     * Normaalivektorit parametrejä vastaavalle nelikulmiolle (pyöritettynä)
     * @param pos
     * @param width
     * @param height
     * @param angle
     * @return
     */
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
    
    /**
     * Suhteelliset kulmien sijainnit parametrejä vastaavalle nelikulmiolle.
     * @param pos
     * @param width
     * @param height
     * @param angle
     * @return
     */
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
    
    /**
     * Käänteinen massa
     * @param static_
     * @param width
     * @param height
     * @param density
     * @return
     */
    public static double invMass(boolean static_, 
            double width, double height, double density) {
        if (static_) {
            return 0.;
        } else {
            return 1./(width*height*density);
        }
    }
    
    @Override
    public double invMass() {
        return invMass(false, WIDTH, HEIGHT, material.density);
    }
    
    /**
     *
     * @param static_
     * @param width
     * @param height
     * @param density
     * @return
     */
    public static double invMoment(boolean static_, 
            double width, double height, double density) {
        if (static_) {
            return 0;
        } else {
            double moment = width*height*density/12*
                    (width*width + height*height);
            return 1./moment;
        }
    }
    
    @Override
    public double invMoment() {
        return invMoment(false, WIDTH, HEIGHT, material.density);
    }
    
    private void applyRotation(double rotationAngle) {
        applyRotation(getVertices(), rotationAngle);
        applyRotation(normals, rotationAngle);
    }
    
    @Override
    public void rotate(double rotationAngle) {
        super.rotate(rotationAngle);
        applyRotation(rotationAngle);
    }
    
    @Override
    public boolean resolveCollision(ItemRectangle other, 
            double dt, Vector gravity, int iterations) {
        RectRectCollision collision = new RectRectCollision(this, other);
        return collision.resolve(dt, gravity, iterations);
    }
    
    /**
     *
     * @param normal
     * @return
     */
    public Projection projection(Vector normal) {
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
    
    /**
     *
     * @param overlap
     * @param relPos
     * @return
     */
    public Vector normal(Vector overlap, Vector relPos) {
        if (overlap.getX() < overlap.getY()) {
            return new Vector(Math.copySign(1, relPos.getX()), 0);
        }
        return new Vector(0, Math.copySign(1, relPos.getY()));
    }
    
    public ArrayList<Vector> getNormals() {
        return normals;
    }

    public ArrayList<Vector> getVertices() {
        return vertices;
    }

}