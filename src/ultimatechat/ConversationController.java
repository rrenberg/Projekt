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
import java.util.ArrayList;

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
    
    public ConversationController(String inName, Color inColor){
        name = inName;
        color = inColor;
        chatview = new ChatView(this); 
    }
    
    public void updateChatView(){
        
    }
    
    public void addClient(DataOutputStream inOutStream, DataInputStream inInStream){
        clients.add(new ClientThread(inInStream, inOutStream));
    }
    
    private void KillclientThread(ClientThread inClient){
        
    }

   
    }
    
    
    

