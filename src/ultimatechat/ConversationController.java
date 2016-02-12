/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author The ZumBot 2.0
 */
public class ConversationController {
    
    UltimateChat myUltimateChat;
    ChatView chatview;
    private Sender sender;
    private String name;
    private Color color;
    private ArrayList<ClientThread> clients;
    private XMLParser myParser;
    
    public ConversationController(String inName, Color inColor,XMLParser inParser, UltimateChat inUltimatechat){
        name = inName;
        color = inColor;
        chatview = new ChatView(this); 
        myParser = inParser;
        clients = new ArrayList<>();
        myUltimateChat = inUltimatechat;
    }
    
    public void bounceTextMessage(String inXML,ClientThread inClient){
        for(ClientThread i:clients){
            if(i!=inClient){
                try {
                    i.getOutPutStream().println(inXML);
                } catch (Exception ex) {
                    Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void recieveTextMessage(String inName, Color inColor, String inText, ClientThread inClient){
        System.out.println("Före i recieve");
        chatview.addOthersText(inText, inColor, inName);

        System.out.println("Efter i recieve");
    }
    
    public void addClient(PrintWriter inOutStream, BufferedReader inInStream){
        ClientThread newClient = new ClientThread(inInStream, inOutStream, myParser, this);
        clients.add(newClient);
        chatview.addToClient(newClient);
    }
    
    public void sendConnectionRequest(String inText){
       
        try {
            clients.get(clients.size()-1).getOutPutStream().println(myParser.sendrequestToXML(inText,name));
        } catch (Exception ex) {
            Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public void sendText(String inText){
        for(ClientThread i:clients){

            try {
                System.out.println(clients.size());
                i.getOutPutStream().println(myParser.sendText(inText,name,color));
            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public String getName(){
        return name;
    }
    
    public Color getColor(){
        return color;
    }
    
    public UltimateChat getUltimateChat(){
        return myUltimateChat;
    }
    
    public void setColor(Color inColor){
        color=inColor;
    }
    
    public ArrayList<ClientThread> getClients(){
        return clients;
    }
    
    
    public void killConversation(){
        for(ClientThread i: clients){
            
            try {
                System.out.println("asdasaaa "+ myParser.disconnectXML(name,Color.RED));
                i.getOutPutStream().println(myParser.disconnectXML(name,Color.RED));
                System.out.println("Kickar client:"+i);
                i.killClientThread(false);
                //clients.remove(i);
               //myUltimateChat.conversationControllerList.remove(this);
            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
        }
    }
    
    public void killClient(ClientThread ct){
        
            
            try {
                System.out.println("asdasaaa "+ myParser.disconnectXML(name,Color.RED));
                ct.getOutPutStream().println(myParser.disconnectXML(name,Color.RED));
                System.out.println("Kickar client:"+ct);
                ct.killClientThread(false);
                //clients.remove(i);
               //myUltimateChat.conversationControllerList.remove(this);
            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
        
    }

   
    }
    
    
    

