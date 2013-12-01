package physics;

import logic.Vector;

/**
 * Projektio, joka on apuluokka monikulmioiden "tahkojen" suuntaisen 
 * päällekkäisyyden analysointiin.
 * @author juho
 */
public class Projection {
    
    public final double min;
    public final double max;
    public final Vector minVertex;
    public final Vector maxVertex;

    public Projection(double min, double max, 
            Vector minVertex, Vector maxVertex) {
        this.min = min;
        this.max = max;
        this.minVertex = minVertex;
        this.maxVertex = maxVertex;
    }
    
    public double overlapAlongNormal(Projection other) {
        return Math.min(other.max - min, max - other.min);
    }

}