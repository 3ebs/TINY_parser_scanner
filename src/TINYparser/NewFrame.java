/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TINYparser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
//import static parser.parserGUI.im;

/**
 *
 * @author INTEL
 */
public class NewFrame extends JFrame {
    public static ImagePanel im;
    private BufferedImage image;
    public NewFrame()
    {
        
        im = new ImagePanel();
        this.add(im);
        setSize(900,720);
        setLocation(300,100);
        try {
        image = ImageIO.read(new File("out.jpg"));
        im.setImage(image);}
        catch(IOException ex){
            System.out.println("image can't be loaded");
        }
       im.setSize(400,600);//400, 500
       im.setVisible(true);
       im.repaint();
       this.repaint();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
}