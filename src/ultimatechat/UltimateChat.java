/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.Box.createVerticalStrut;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author The ZumBot 2.0
 */
public class UltimateChat implements Runnable {
    
    MainView mainView;
    ArrayList<ConversationController> conversationControllerList;
    int port;
    String name;
    
    public UltimateChat() {
        mainView = new MainView(this);
        conversationControllerList = new ArrayList<>();
        //createDialogForNameAndPort();
        int answer= createDialogForConnectionRequest("Johan");
        //ConnectView v = new ConnectView();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UltimateChat();
    }
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void createDialogForNameAndPort(){
        
        // Create textfields and panel for the Dialog
        JTextField nameTextField = new JTextField(6);
        JTextField portTextField = new JTextField(6);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel("Name:"));
        dialogPanel.add(nameTextField);
        dialogPanel.setLayout(new FlowLayout());
        dialogPanel.add(new JLabel("Port:"));
        dialogPanel.add(portTextField);
        
        JOptionPane.showConfirmDialog(null, dialogPanel,
                "Please enter your name and port", JOptionPane.DEFAULT_OPTION);
        
        //Set name and port
        name = nameTextField.getText();
        port = Integer.valueOf(portTextField.getText());
        
        //Add to name and port to MainView
        JPanel upperPanel = new JPanel();
       
        upperPanel.add(new JTextField("Your name: "+name));
        upperPanel.add(new JTextField("Port: "+String.valueOf(port)));
        
        mainView.add(upperPanel,BorderLayout.NORTH);
        mainView.show();
    }
    public int createDialogForConnectionRequest(String name){
              // Create textfields and panel for the Dialog
        //JTextField nameTextField = new JTextField(6
        //JTextField portTextField = new JTextField(6);
        
        String[] conversations = new String[conversationControllerList.size()+1];
        //String[] conversations = new String[1];
        conversations[0]="<New conversation>";
        
        for(int i=1;i<conversationControllerList.size();i++){
            conversations[i] = mainView.getTabTitle(i-1);
        }
        
        JComboBox convDropDown = new JComboBox(conversations);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel(name+" wants to connect"));
        dialogPanel.add(createVerticalStrut(30));
        dialogPanel.add(convDropDown);
        
        
        return JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect reqeust", JOptionPane.CANCEL_OPTION);
        
       
    }
}
