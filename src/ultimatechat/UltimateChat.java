/*
 * UltimateChat
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * The class UltimateChat provides a main function and inhabits all the different
 * conversationControllers and their GUI. It creates a MainView and listens for
 * new conactions. 
 *
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class UltimateChat implements Runnable {

    MainView mainView;
    ArrayList<ConversationController> conversationControllerList;
    int port;
    String name;
    ServerSocket serverSocket;
    XMLParser xmlParser;
    CreateDialogForConnectionRequest C;
    boolean server = true;

    /**
     * Constructor which creates a mainView and a pupup for name and port.
     * Creates a new conversation and start a new thread that listens for new
     * connections.
     *
     */
    public UltimateChat() {
        mainView = new MainView(this);
        createDialogForNameAndPort();
        xmlParser = new XMLParser();

        conversationControllerList = new ArrayList<ConversationController>();
        createNewConversationController();
        mainView.addConversation();

        Thread t = new Thread(this);
        t.start();

    }

    /**
     * Function createNewConversationController creates a new conversation-
     * controller and adds it to the list over conversations.
     *
     */
    public void createNewConversationController() {
        ConversationController c = new ConversationController(name, Color.BLUE, xmlParser, this);

        conversationControllerList.add(c);
    }

    /**
     * Main function that creates a new UltimateChat.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UltimateChat();

    }

    @Override
    //Run method that listens for new connections.
    public void run() {

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("Could not listen on port");
            System.exit(-1);
        }

        StringBuilder respons = new StringBuilder();
        
        //As long as Server is true.
        while (server) {
            Socket clientsocket = null;
            try {
                
                //Accetpts new connections and ctreates new startUpThread
                clientsocket = serverSocket.accept();
                BufferedReader inStream = new BufferedReader(
                        new InputStreamReader(clientsocket.getInputStream()));
                PrintWriter outStream = new PrintWriter(
                        clientsocket.getOutputStream(), true);
                InetSocketAddress isa = (InetSocketAddress) clientsocket.getRemoteSocketAddress();
                Thread thread = new Thread(new StartUpThread(inStream,
                        outStream, this, isa.getAddress().toString().substring(1)));
                thread.start();

            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Function createDialogForNaeAndPort creates an popup for entering
     * name and port for user.
     *
     */
    public void createDialogForNameAndPort() {

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
        if (nameTextField.getText().equals("")) {
            name = "UnNamed";
        } else {
            name = nameTextField.getText();
        }

        try {
            if (portTextField.getText() == null) {
                port = 4000;
            } else {

                port = Integer.valueOf(portTextField.getText());

            }

        } catch (Exception ex) {
            port = 4000;
            JFrame tempJF = new JFrame("Bad Port");
            JOptionPane.showMessageDialog(tempJF, 
                    "Varning! Invalid port choosen, you get port 4000 ",
                    "Bad Port",
                    JOptionPane.WARNING_MESSAGE);
        } finally {
            //Add to name and port to MainView
            JPanel upperPanel = new JPanel();
            try {
                InetAddress IP = InetAddress.getLocalHost();
                upperPanel.add(new JTextField("Your address: " +
                        IP.getHostAddress()));
            } catch (UnknownHostException ex) {
                Logger.getLogger(UltimateChat.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            upperPanel.add(new JTextField("Your name: " + name));
            upperPanel.add(new JTextField("Port: " + String.valueOf(port)));

            mainView.add(upperPanel, BorderLayout.NORTH);
            mainView.show();
        }
    }

    /**
     *
     * @return Returns ConversationController
     */
    public ConversationController getConvController() {
        return conversationControllerList.get(conversationControllerList.size() - 1);
    }

    /**
     *
     * @return Returns the ArrayList with ConversationsControllers
     */
    public ArrayList getConvControllerList() {
        return conversationControllerList;
    }

    /**
     *
     * @param inBoolean Sets The value of server i.e. if server is on.
     */
    public void setServer(boolean inBoolean) {
        server = inBoolean;
    }
}
