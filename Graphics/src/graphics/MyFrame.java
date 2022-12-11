package graphics;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
/**
 *
 * @author lainati_samuele
 */
public class MyFrame extends JFrame implements ActionListener{
    
    JFrame frame=new JFrame();
    JButton myButton=new JButton ("Clicca per giocare!");
    
    MyFrame()
    {
        myButton.setBounds(100,160,200,40);
        myButton.setFocusable(false);
        myButton.addActionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.add(myButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==myButton)
        {
            frame.dispose();
            MyFrame2 my=new MyFrame2();
        }  
    }  
}