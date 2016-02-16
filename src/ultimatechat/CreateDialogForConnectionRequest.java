/*
 * ClientThread
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.util.ArrayList;
import static javax.swing.Box.createVerticalStrut;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The class CreateDialogForConnectionRequest provides the visual GUI when a
 * connection request is recived.
 *
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class CreateDialogForConnectionRequest {

    private ArrayList<ConversationController> conversationControllerList;
    private MainView mainView;
    private String myMessage;

    /**
     * Constructor which sets all parameters.
     *
     * @param message Recived message
     * @param inconversationControllerList ArrayList with all conversationControllers
     * @param inMainView Our mainView object
     */
    public CreateDialogForConnectionRequest(String message, 
            ArrayList<ConversationController> inconversationControllerList, 
            MainView inMainView) {
        
        conversationControllerList = inconversationControllerList;
        mainView = inMainView;
        myMessage = message;
    }

    /**
     * Function that creates a popup for a connection-request.
     *
     * @return answers Returns which anwsers to the connection-request the user gave.
     */
    public ArrayList createDialogForConnectionRequestPopup() {

        String[] conversations = new String[conversationControllerList.size() + 1];
        conversations[0] = "<New conversation>";
        
        //Adds all conversations as option.
        for (int i = 1; i <= conversationControllerList.size(); i++) {
            conversations[i] = mainView.getTabTitle(i - 1);
        }

        JComboBox convDropDown = new JComboBox(conversations);

        JPanel dialogPanel = new JPanel();

        // Add textfields to the dialog
        dialogPanel.add(new JLabel("Message: " + myMessage));
        dialogPanel.add(createVerticalStrut(30));
        dialogPanel.add(convDropDown);

        ArrayList answers = new ArrayList<>();
        answers.add(JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect reqeust", JOptionPane.CANCEL_OPTION));
        answers.add(convDropDown.getSelectedIndex());
        return answers;

    }
}
