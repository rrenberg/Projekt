/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author The ZumBot 2.0
 */
public class ConversationController {
    
    ChatView chatview;
    private Sender sender;
    private String name;
    private Color color;
    private ArrayList<ClientThread> clients;
    private XMLParser myParser;
    
    public ConversationController(String inName, Color inColor,XMLParser inParser){
        name = inName;
        color = inColor;
        chatview = new ChatView(this); 
        myParser = inParser;
        clients = new ArrayList<>();
    }
    
    public void updateChatView(){
        
    }
    
    public void addClient(DataOutputStream inOutStream, DataInputStream inInStream){
        clients.add(new ClientThread(inInStream, inOutStream));
    }
    
    public void sendConnectionRequest(String inText){
        try {
            System.out.println(myParser);
            clients.get(clients.size()-1).getOutPutStream().writeUTF(myParser.sendrequestToXML(inText));
        } catch (IOException ex) {
            Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void KillclientThread(ClientThread inClient){
        
    }

   
    }
    
    
    

