package unused;

import unused.Shape;

public class Rectangle extends Shape {
    
    public final double width;
    public final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    } 

    @Override
    public double calculateMass(double density) {
        return width*height*density;
    }

} 