/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.FlowLayout;
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
            
            
            
    public ConnectView(){
        // Create textfields and panel for the Dialog
        JTextField portTextField = new JTextField(6);
        JTextField addressTextField = new JTextField(6);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel("IP:"));
        dialogPanel.add(addressTextField);
        dialogPanel.setLayout(new FlowLayout());
        dialogPanel.add(new JLabel("Port:"));
        dialogPanel.add(portTextField);
        
        JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect", JOptionPane.CANCEL_OPTION);
        port = Integer.valueOf(portTextField.getText());
        address = addressTextField.getText();
        
    }
    
    public int getPort(){
        return port;
    }
    
    public String getAddress(){
        return address;
    }
}
