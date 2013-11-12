package logic;

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
     *
     * Euklidinen etäisyys.
     * @param other
     * @return
     */
    public double distance(Vector other) {
        return Math.sqrt(
            Math.pow(getX() - other.getX(), 2) + 
            Math.pow(getY() - other.getY(), 2));
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
     * true jos komponentit ovat samat.
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