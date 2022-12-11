package graphics;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 *
 * @author lainati_samuele
 */
public class MyFrame2 extends JFrame{
    
    JFrame frame=new JFrame();
    JLabel label = new JLabel("Benvenuto");
    
    MyFrame2()
    {
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        
        label.setBounds(0,0,100,50);
        
        frame.add(label);
        
        label.setFont(new Font("Comic Sans",Font.BOLD,20));
    }
}