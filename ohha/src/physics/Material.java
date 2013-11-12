package physics;

public class Material {

    public final double density;
    public final double elasticity;
    public final double friction;
    
    public Material(double density, double elasticity, double friction) {
        this.density = density;
        this.elasticity = elasticity;
        this.friction = friction;
    }
    
}
