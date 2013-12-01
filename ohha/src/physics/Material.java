package physics;

/**
 * Luokka, joka sisältää tiedot kappaleen materiaalista.
 * @author juho
 */
public class Material {

    public final double density;
    public final double elasticity;
    public final double friction;
    
    /**
     *
     * @param density
     * @param elasticity
     * @param friction
     */
    public Material(double density, double elasticity, double friction) {
        this.density = density;
        this.elasticity = elasticity;
        this.friction = friction;
    }
    
    /**
     * Hatusta heitetty kaava, jolla keksitään elastisuus 
     * kahden kappaleen välisessä törmäyksessä.
     * @param otherElasticity
     * @return
     */
    public double collisionElasticity(double otherElasticity) {
        return Math.min(elasticity, otherElasticity);
    }
    
}
