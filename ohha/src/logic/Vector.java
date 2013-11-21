package logic;

/**
 * Vektori. Tavanomaisia matemaattisia operaatioita.
 * @author juho
 */
public class Vector { 
    
    private double x;
    private double y;
    
    public Vector() {
        this.x = 0;
        this.y = 0;
    }
    
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector(Vector other) {
        this.x = other.getX();
        this.y = other.getY();
    }
    
    public void increment(Vector other) {
        x += other.getX();
        y += other.getY();
    }
    
    /**
     * Aseta vektorin komponentit nolliksi.
     */
    public void clear() {
        x = 0.;
        y = 0.;
    }
    
    /**
     *
     * Pistetulo.
     * @param other
     * @return
     */
    public double dot(Vector other) {
        return x*other.x + y*other.y;
    }
    
    /**
     * Ristitulo. 
     * ("z-komponentin" suuruus)
     * @param other
     * @return
     */
    public double cross(Vector other) {
        return x*other.y - y*other.x;
    }

    /**
     *
     * Euklidinen etäisyys.
     * @param other
     * @return
     */
    public double distance(Vector other) {
        return Math.sqrt(
            Math.pow(x - other.x, 2) + 
            Math.pow(y - other.y, 2));
    }
    
    public double square() {
        return x*x + y*y;
    }
        
    /**
     * 
     * @param other
     * @return
     */
    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }
    
    /**
     *
     * @param other
     * @return
     */
    public Vector substract(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }
    
    /**
     *
     * @param factor
     * @return
     */
    public Vector multiply(double factor) {
        return new Vector(factor*x, factor*y);
    }
    
    public Vector rotate(double angle) {
        return new Vector(
                x*Math.cos(angle) - y*Math.sin(angle),
                x*Math.sin(angle) + y*Math.cos(angle));
    }
    
    public void applyRotation(double angle) {
        double newX = x*Math.cos(angle) - y*Math.sin(angle);
        y = x*Math.sin(angle) + y*Math.cos(angle);
        x = newX;
    }
    
    /**
     * 
     * Vektorin peilikuva y-akselin suunnassa.
     * @return
     */
    public Vector flipY() {
        return new Vector(x, -y);
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    
    /**
     *
     * true jos molemmat komponentit ovat samat.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector other = (Vector) obj;
        return x == other.x && y == other.y;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ 
                (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ 
                (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
    
} 