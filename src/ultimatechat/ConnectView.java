/*
 * ConnectView
 *
 * Version 1.0
 *
 * 16-02-2016
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
 * The class ConnectView creats an dialogpanel for connection with other client
 * or server.
 * 
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class ConnectView 
{
    private Integer port;
    private String address;
    private String textMessage;
    private boolean connectionOk = false;  
            
    /**
     *Constructor that creats dialogpanel for connection.
     */
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
        
        //Creats dialogpane
        int ans = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect", JOptionPane.CANCEL_OPTION);
        
        if(ans==0){
         try{
                port = Integer.valueOf(portTextField.getText());
                   
                address = InetAddress.getByName(addressTextField.getText()).
                        toString().substring(1);
                
                textMessage = TextField.getText();
                connectionOk = true;

        }catch(Exception ex){
                //If some of the input where incorrect, creats a warning popup.
                JFrame tempJF = new JFrame("Some Input was incorrect");
                JOptionPane.showMessageDialog(tempJF, "Varning! Invalid input! ",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
        }
        }
    }
    
    /**
     *
     * @return boolean Returns if connection is ok
     */
    public boolean getConnectionOk(){
        return connectionOk;
    }
    
    /**
     *
     * @return Integer Returns port
     */
    public int getPort(){
        return port;
    }
    
    /**
     *
     * @return String Returns Address
     */
    public String getAddress(){
        return address;
    }
    
    /**
     *
     * @return String Returns text-message
     */
    public String getTextMessage(){
        return textMessage;
    }
}
