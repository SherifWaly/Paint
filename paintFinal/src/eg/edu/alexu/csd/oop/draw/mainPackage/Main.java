package eg.edu.alexu.csd.oop.draw.mainPackage;


import java.awt.*;

import javax.swing.UIManager;

import eg.edu.alexu.csd.oop.master.Control;
import eg.edu.alexu.csd.oop.master.Model;
import eg.edu.alexu.csd.oop.master.View;
public class Main{
 
 public  View frame;
 
 /**
  * Launch the application.
  */
 public static void main(String[] args) {
  EventQueue.invokeLater(new Runnable() {
   public void run() {
    try {
     Main window = new Main();
     window.frame.setVisible(true);
     javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
	for (int i = 0; i < installedLookAndFeels.length; i++) {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	}
      
     
    } catch (Exception e) {
     e.printStackTrace();
    }
   }
  });
 }

 public Main() throws AWTException {
  initialize();
 }

 private void initialize() throws AWTException {
  
  frame = new View();
  Model model=new Model();
  Control control=new Control(frame,model);
  control.initialize();
  
  
 }
}