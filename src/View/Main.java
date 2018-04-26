/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import Math.Point;
import Math.Triangle;
import Math.Sphere;
import Math.Ray;
import Math.Vector4;

import Scene.Scene;
import Scene.Colour;
import Scene.AmbientLight;
import Scene.PointLight;
import Scene.Material;

/**
 *
 * @author htrefftz
 */
public class Main extends JPanel {
    Image image;
    private final boolean DEBUG = false;
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Create all elements in the scene
     * Could be read from a text file
     */
    public void createScene() {
        AmbientLight al = new AmbientLight(new Colour(.2, .2, .2));
        Scene.setAmbientLight(al);
        
        PointLight pl1 = new PointLight(new Point(-100, 100, 0), new Colour(1, 1, 1));
        //PointLight pl1 = new PointLight(new Point(-100, 200, -400), new Colour(1, 1, 1));
        PointLight pl2 = new PointLight(new Point(100, 100, 100), new Colour(1, 1, 1));
        //Scene.addPointLight(pl1);
        Scene.addPointLight(pl2);
        
        // A yellow reflective sphere
        double Ka = .2;        // ambient
        double Kd = .8;        // difuse
        double Ks = .7;          // specular
        int n = 32;
        Colour colors = new Colour(1, 1, 0);     // object's color
        double Ko = 0.7;          // Weight of this object's color
        double Kr = 0.3;          // Weight of the reflected color
        double Kt = 0;          // Weight of the refracted color
        Material material1 = new Material(Ka, Kd, Ks, n, colors, Ko, Kr, Kt);
        
        //Sphere sp1 = new Sphere(new Point(-25, 0, -100), 20, material1);
        Sphere sp1 = new Sphere(new Point(20, 0, -100), 20, material1);  
        Scene.addIntersectable(sp1);
        
        Colour colort = new Colour(1, 0, 1);     // object's color Triangle
        Material material2 = new Material(Ka, Kd, Ks, n, colort, Ko, Kr, Kt);
        Triangle tri = new Triangle(new Point(-110, -90, -250), new Point(90, -85, -200), new Point(90, 80, -200), material2);
        Scene.addIntersectable(tri);

    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();

        int w = size.width;
        int h = size.height;      
        
        Colour colour;
        for(int i = 0; i < image.height; i++) {
            for(int j = 0; j < image.width; j++) {
                colour = image.image[i][j];
                int red = (int) (colour.getR() * 255);
                int green = (int) (colour.getG() * 255);
                int blue = (int) (colour.getB() * 255);
                // Clamp out of range colors
                if(red > 255) red = 255;
                if(green > 255) green = 255;
                if(blue > 255) blue = 255;
                g2d.setColor(new Color(red, green, blue));
                if(DEBUG)
                    System.out.println(red + " " + green + " " + blue);
                g2d.drawLine(i, j, i, j);
            }
        }        
    }
    
    
    public static void main(String [] args) {
        JFrame frame = new JFrame("Ray Tracer 2018");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int width = 500;
        int height = 500;
        
        Main main = new Main();
        main.createScene();
        Image image = new Image(width, height);
        image.generateImage();
        main.setImage(image);

        // Draw the result
        frame.add(main);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}
