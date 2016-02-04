/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.net.InetAddress;
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
 *
 * @author The ZumBot 2.0
 */
public class UltimateChat implements Runnable {
    
    MainView mainView;
    ArrayList<ConversationController> conversationControllerList;
    int port;
    String name;
    ServerSocket serverSocket;
    XMLParser xmlParser;
    
    public UltimateChat() {
        mainView = new MainView(this);
        createDialogForNameAndPort();
        xmlParser = new XMLParser();
        createNewConversationController();
        mainView.addConversation();
    
        
        Thread t = new Thread(this);
        t.start();
        
        ArrayList answer= createDialogForConnectionRequest("Kalle");
        System.out.println((int)answer.get(0));
        System.out.println(Color.BLACK);
        
        
        //conversationControllerList.add(new ConversationController("Johan",3));
        //ConnectView v = new ConnectView();
        
    }
    
    public void createNewConversationController(){
        ConversationController c = new ConversationController(name,Color.BLUE,xmlParser);
        
        conversationControllerList = new ArrayList<>();
        conversationControllerList.add(c);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UltimateChat();
        
    }
    
    @Override
    public void run() {
        System.out.println("Inne i run");
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Inne i ServerSocket");
	} catch (IOException e) {
	    System.out.println("Could not listen on port: 4444");
	    System.exit(-1);
        }
            
        while(true){
            Socket clientsocket = null;
            try{
                System.out.println("Lyssnar nu");
                clientsocket = serverSocket.accept();
                DataInputStream inStream = new DataInputStream(clientsocket.getInputStream());
                DataOutputStream outStream = new DataOutputStream(clientsocket.getOutputStream());
                
               
                String stringMessage = inStream.readUTF();
                
                //createDialogForNameAndPort();
                ArrayList answer= createDialogForConnectionRequest(xmlParser.requestParser(stringMessage));
                
                if((int)answer.get(0) == 0){
                    if((int) answer.get(1)==0){
                       createNewConversationController();
                       conversationControllerList.get(conversationControllerList.size()-1).addClient(outStream,inStream);
                      
                       mainView.addConversation();
                    }else{
                       conversationControllerList.get((int)answer.get(1)-1).addClient(outStream,inStream);
                    }
                }else if ((int)answer.get(0)==2){
                    
                }
                
                System.out.println("Inne i loopen");
            }catch(Exception ex){
                
            }
            
            
        }
           
    
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
        try {
            InetAddress IP=InetAddress.getLocalHost();
            upperPanel.add(new JTextField("Your address: "+IP.getHostAddress()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(UltimateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        upperPanel.add(new JTextField("Your name: "+name));
        upperPanel.add(new JTextField("Port: "+String.valueOf(port)));
        
        mainView.add(upperPanel,BorderLayout.NORTH);
        mainView.show();
    }
    public ArrayList createDialogForConnectionRequest(String message){
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
        dialogPanel.add(new JLabel("Message: "+message));
        dialogPanel.add(createVerticalStrut(30));
        dialogPanel.add(convDropDown);
        
        ArrayList answers = new ArrayList<>();
        answers.add(JOptionPane.showConfirmDialog(null, dialogPanel,
                "Connect reqeust", JOptionPane.CANCEL_OPTION));
        answers.add(convDropDown.getSelectedIndex());
        return answers;
        
       
    }
    
    public ConversationController getConvController(){
        return conversationControllerList.get(conversationControllerList.size()-1);
    }
}
