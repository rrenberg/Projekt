/*
 * ConversationController
 *
 * Version 1.0
 *
 * 16-02-2016
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
 *The class ConversationController provides control over the specific 
 * conversation. Controls all clients and visuals of the conversation.
 * 
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class ConversationController {

    UltimateChat myUltimateChat;
    ChatView chatview;
    private String name;
    private Color color;
    private ArrayList<ClientThread> clients;
    private XMLParser myParser;
    private boolean sameAsOtherClient;

    /**
     *Constructor which sets the parameters.
     * 
     * @param inName Our name
     * @param inColor Our color
     * @param inParser Our Parser
     * @param inUltimatechat Our UltimateChat
     * 
     */
    public ConversationController(String inName, Color inColor, XMLParser inParser,
            UltimateChat inUltimatechat) {
        
        name = inName;
        color = inColor;
        chatview = new ChatView(this);
        myParser = inParser;
        clients = new ArrayList<>();
        myUltimateChat = inUltimatechat;
    }

    /**
     *Function that sends the recived message to all other clients in the
     * same conversation.
     * 
     * @param inXML XML that should be passed on to the other clients
     * @param inClient Which client that sent the massage
     */
    public void bounceTextMessage(String inXML, ClientThread inClient) {
        for (ClientThread i : clients) {
            if (i != inClient) {
                try {
                    
                    i.getOutPutStream().println(inXML);
                } catch (Exception ex) {
                    Logger.getLogger(ConversationController.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     *Function that displays the recived message.
     * 
     * @param inName Clients name
     * @param inColor Clients color
     * @param inText Clients Text-Message
     * @param inClient Client that sent the message
     */
    public void recieveTextMessage(String inName, Color inColor, String inText,
            ClientThread inClient) {
        
        chatview.addOthersText(inText, inColor, inName);
    }

    /**
     *Function that adds new Client to the conversation by creating a new
     * ClientThread object.
     * 
     * @param inOutStream Client inStream (BufferedReader)
     * @param inInStream Client outStream(PrintWriter)
     */
    public void addClient(PrintWriter inOutStream, BufferedReader inInStream, String inAddress) {
        
        sameAsOtherClient = false;
        for(ClientThread c: clients){
            if(c.getAddress().equals(inAddress)){
                sameAsOtherClient=true;
                chatview.addMyText("Connection denied, same as already existing connection");
            }
        }
        if(!sameAsOtherClient){
            ClientThread newClient = new ClientThread(inInStream, inOutStream,
                myParser, this, inAddress);
        
            clients.add(newClient);
            chatview.addToClient(newClient);
        }
    }

    /**
     *Function that send a connectionrequest.
     * 
     * @param inText Our Text-message we want to send to the one we want to
     * connect to
     */
    public void sendConnectionRequest(String inText) {

        try {
            clients.get(clients.size() - 1).getOutPutStream().println(myParser.
                    sendrequestToXML(inText, name));
        } catch (Exception ex) {
            Logger.getLogger(ConversationController.class.getName()).log(Level.
                    SEVERE, null, ex);
        }

    }

    /**
     *Function that send Text message to client.
     * 
     * @param inText Our Text-message we want to send
     */
    public void sendText(String inText) {
        for (ClientThread i : clients) {
            try {
                System.out.println(clients.size());
                i.getOutPutStream().println(myParser.sendText(inText,
                        name, color));
            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @return String Returns  Our name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return color Returns Our color
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return UltimateChat Returns Our ultimateChat
     */
    public UltimateChat getUltimateChat() {
        return myUltimateChat;
    }

    /**
     *
     * @param inColor Sets Our color
     */
    public void setColor(Color inColor) {
        color = inColor;
    }

    /**
     * 
     * Function that returns clients in conversation.
     *
     * @return ArrayList Returns the arraylist with the clients in the conversation.
     */
    public ArrayList<ClientThread> getClients() {
        return clients;
    }

    /**
     *Function that kill all clients and the conversation.
     */
    public void killConversation() {
        for (ClientThread i : clients) {
            try {
                i.getOutPutStream().println(myParser.disconnectXML(name, Color.RED));
                i.killClientThread(false);

            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *Function kills client, sends disconnect tag. 
     * 
     * @param ct
     */
    public void killClient(ClientThread ct) {

        try {
            ct.getOutPutStream().println(myParser.disconnectXML(name, Color.RED));
            ct.killClientThread(false);

        } catch (Exception ex) {
            Logger.getLogger(ConversationController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

    }
    public boolean getSameAsOtherClient(){
        return sameAsOtherClient;
    }

}
