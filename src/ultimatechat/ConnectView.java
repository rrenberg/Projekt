/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.FlowLayout;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author The ZumBot 2.0
 */
public class ConnectView 
{
    private Integer port;
    private String address;
    private String textMessage;
    private boolean connectionOk = false;  
            
            
    public ConnectView(){
        // Create textfields and panel for the Dialog
        JTextField portTextField = new JTextField(6);
        JTextField addressTextField = new JTextField(6);
        JTextField TextField = new JTextField(15);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel("IP:"));
        dialogPanel.add(addressTextField);
        dialogPanel.setLayout(new FlowLayout());
        dialogPanel.add(new JLabel("Port:"));
        dialogPanel.add(portTextField);
        dialogPanel.add(new JLabel("Message:"));
        dialogPanel.add(TextField);
        
        int ans = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect", JOptionPane.CANCEL_OPTION);
        if(ans==0){
         try{
                port = Integer.valueOf(portTextField.getText());
                   
                address = InetAddress.getByName(addressTextField.getText()).toString().substring(1);
                System.out.println(InetAddress.getByName(addressTextField.getText()).toString());
                textMessage = TextField.getText();
                connectionOk = true;

        }catch(Exception ex){
                JFrame tempJF = new JFrame("Some Input was incorrect");
                JOptionPane.showMessageDialog(tempJF, "Varning! Invalid input! ",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
        }
        }
    }
    
    public boolean getConnectionOk(){
        return connectionOk;
    }
    
    public int getPort(){
        return port;
    }
    
    public String getAddress(){
        return address;
    }
    
    public String getTextMessage(){
        return textMessage;
    }
}
