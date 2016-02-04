/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.util.ArrayList;
import static javax.swing.Box.createVerticalStrut;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jakobarnoldsson
 */
public class CreateDialogForConnectionRequest {
    private ArrayList<ConversationController> conversationControllerList;
    private MainView mainView;
    private String myMessage;
    
    public CreateDialogForConnectionRequest(String message, ArrayList<ConversationController> inconversationControllerList, MainView inMainView){
        conversationControllerList = inconversationControllerList;
        mainView = inMainView;
        myMessage = message;
    }
    
    public ArrayList createDialogForConnectionRequestPopup(){
              // Create textfields and panel for the Dialog
        //JTextField nameTextField = new JTextField(6
        //JTextField portTextField = new JTextField(6);
        
        String[] conversations = new String[conversationControllerList.size()+1];
        //String[] conversations = new String[1];
        conversations[0]="<New conversation>";
        
        for(int i=1;i<=conversationControllerList.size();i++){

            conversations[i] = mainView.getTabTitle(i-1);
            
        }
        
        JComboBox convDropDown = new JComboBox(conversations);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel("Message: "+myMessage));
        dialogPanel.add(createVerticalStrut(30));
        dialogPanel.add(convDropDown);
        
        ArrayList answers = new ArrayList<>();
        answers.add(JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect reqeust", JOptionPane.CANCEL_OPTION));
        answers.add(convDropDown.getSelectedIndex());
        return answers;
        
       
    }
}
