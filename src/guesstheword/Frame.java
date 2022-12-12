package guesstheword;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Frame extends JFrame implements ActionListener {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JTextArea systemLog;
    JScrollPane scroll;

    JTextArea userWrite = new JTextArea(100, 50);
    JLabel image = new JLabel(new ImageIcon("../imgs/corda.png"));
    JScrollPane scrollChat;
    JButton invia = new JButton("Invia");
    DataOutputStream output;

    Frame(String caller) {

        panel.setBorder(new TitledBorder(new EtchedBorder(), caller));
        systemLog = new JTextArea(100, 100);
        systemLog.setBounds(0, 0, 800, 800);
        systemLog.setEditable(false);
        scroll = new JScrollPane(systemLog);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll);
        panel.setSize(800, 800);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    Frame(String caller, DataOutputStream output) {

        this.output=output;
        panel.setBorder(new TitledBorder(new EtchedBorder(), caller));
        systemLog = new JTextArea(100, 50);
        systemLog.setBounds(400, 0, 800, 800);
        systemLog.setEditable(false);
        scrollChat = new JScrollPane(systemLog);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollChat);
        panel.add(userWrite);
        invia.setBounds(100, 160, 200, 40);
        invia.setFocusable(false);
        invia.addActionListener(this);
        panel.add(invia);
        panel.setSize(800, 800);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        
    }

    public void log(String received) {
        systemLog.setText(systemLog.getText() + "\n" + received);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            output.writeUTF(userWrite.getText());
            userWrite.setText("");
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
