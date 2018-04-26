/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import Scene.Material;
import Scene.Colour;
import Scene.Shader;

/**
 *
 * @author jcaladh
 */
public class Triangle implements Intersectable {
    Point a, b, c;
    Material material;
    /**
     * Constructor
     * @param center Center of the Sphere
     * @param radius Radius of the Sphere
     * @param material Material of the Sphere
     */
    public Triangle(Point a, Point b, Point c, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.material = material;
    }
    
    /**
     * Intersect a Sphere with a ray. Returns the t value(s) for the ray at the solution(s)
     * (if any).
     * @param ray Ray on intersect the Sphere with
     * @return Solutions. May be 0, 1 or 2 solutions
     */
    public Solutions intersect(Ray ray) {
    	ThreeByThreeSystem equ = new ThreeByThreeSystem(
    			new double[][] {
    		{ray.u.getX(), this.a.getX()-this.b.getX(), this.a.getX()-this.c.getX()},
    		{ray.u.getY(), this.a.getY()-this.b.getY(), this.a.getY()-this.c.getY()},
    		{ray.u.getZ(), this.a.getZ()-this.b.getZ(), this.a.getZ()-this.c.getZ()}}, 
    			new double[] {this.a.getX()-ray.p0.getX(),
    					this.a.getY()-ray.p0.getY(),
    					this.a.getZ()-ray.p0.getZ()});
    	
    	double [] results = equ.computeSystem();
    	
    	double S = results[0];
    	double A = 1 - results[1] - results[2];
    	double B = results[1];
    	double C = results[2];

    	// if((A >0 && A < 1) && (B > 0 && B < 1) && (C > 0 && C < 1)){
    	if((A >=0 && A <= 1) && (B >= 0 && B <= 1) && (C >= 0 && C <= 1)){
    		return new Solutions(1, S, 0);
    	}else {
    		return new Solutions(0, 0, 0);
    	}
    }
    
    /**
     * Returns the normal of the sphere at point p.
     * Point p is assumed to be at the surface of the sphere
     * @param p point at the surface
     * @return normal at point p
     */
    public Vector4 computeNormal(Point p) {
        Vector4 v1 = new Vector4(a.getX(),a.getY(),a.getZ(),b.getX(),b.getY(),b.getZ());
        Vector4 v2 = new Vector4(a.getX(),a.getY(),a.getZ(),c.getX(),c.getY(),c.getZ());
        Vector4 normal = Vector4.crossProduct(v1,v2);
        //normal.normalize();
        return normal;
    }
    
    /**
     * Call the shader to determine the color a the point of intersection
     * determined by the ray and parameter minT
     * @param ray ray that determines the point
     * @param minT value of parameter t
     * @return Colour determined by the shader for this point
     */
    public Colour callShader(Ray ray, double minT) {
        Point point = ray.evaluate(minT);
        Vector4 v1 = new Vector4(a.getX(),a.getY(),a.getZ(),b.getX(),b.getY(),b.getZ());
        Vector4 v2 = new Vector4(a.getX(),a.getY(),a.getZ(),c.getX(),c.getY(),c.getZ());
        Vector4 normal = Vector4.crossProduct(v1,v2);
        normal.normalize();
        return Shader.computeColor(point, normal, material);
    }

    public Material getMaterial() {
        return material;
    }    
    
    @Override
    public String toString() {
        return "Sphere{" + "center=" + a + ", radius=" + a + '}';
    }
    
    /**
     * Test main program
     * @param args 
     */
    public static void main(String [] args) {
        Material m = new Material();
        Triangle triangle = new Triangle(new Point(10, 10, -200d), new Point(10, -200d, 10), new Point(-200d, 10, 10), m);
        Ray ray1 = new Ray(new Point(0, 0, 0), new Point(0, 0, -10));
        System.out.println(triangle.intersect(ray1));
        Ray ray2 = new Ray(new Point(10, 0, 0), new Point(10, 0, -10));
        System.out.println(triangle.intersect(ray2));
        Ray ray3 = new Ray(new Point(5, 0, 0), new Point(5, 0, -10));
        System.out.println(triangle.intersect(ray3));
        Point p = new Point(10, -200d, 10);
        System.out.println(triangle.computeNormal(p));
    }
}
