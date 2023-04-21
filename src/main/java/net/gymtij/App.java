package net.gymtij;

import javax.swing.JFrame;
import javax.swing.*;

import net.gymtij.form.MainForm;


public class App extends JFrame {
		
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm();
            }
        });
    }
}
