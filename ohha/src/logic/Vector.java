package logic;

/**
 * Vektori. Tavanomaisia matemaattisia operaatioita.
 * @author juho
 */
public class Vector { 
    
    private double x;
    private double y;
    
    /**
     * Tyhjä vektori.
     */
    public Vector() {
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Vektori, jolla on määrätyt komponentit.
     * @param x
     * @param y
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Kopio.
     * @param other
     */
    public Vector(Vector other) {
        this.x = other.getX();
        this.y = other.getY();
    }
    
    /**
     * Lisää vektoriin toinen vektori.
     * @param other
     */
    public void increment(Vector other) {
        x += other.getX();
        y += other.getY();
    }
    
    /**
     * Kerro vektorin komponentteja.
     * @param factor
     */
    public void applyMultiplication(double factor) {
        x *= factor;
        y *= factor;
    }
    
    /**
     * Käännä vektoria kulman verran.
     * @param angle
     */
    public void applyRotation(double angle) {
        double newX = x*Math.cos(angle) - y*Math.sin(angle);
        y = x*Math.sin(angle) + y*Math.cos(angle);
        x = newX;
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
     * Ristitulo
     * @param zComponent toisen vektorin z-komponentti
     * @return
     */
    public Vector cross(double zComponent) {
        return new Vector(-y*zComponent, x*zComponent);
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
    
    /**
     * Vektorin itseisarvon neliö.
     * @return
     */
    public double square() {
        return x*x + y*y;
    }
        
    /**
     * Kahden vektorin summa.
     * @param other
     * @return
     */
    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }
    
    /**
     * Kahden vektorin erotus. Toinen vektori vähennetään kyseisestä.
     * @param other
     * @return
     */
    public Vector substract(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }
    
    /**
     * Kahden vektorin tulo.
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
    
    public void set(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
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