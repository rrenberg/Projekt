/*
 * ClientThread
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
 *
 * @author Rasmus Renberg and Jakob Arnoldsson
 * 
 * The class ConversationController provides control over the specific 
 * conversation. Controls all clients and visuals of the conversation.
 */
public class ConversationController {

    UltimateChat myUltimateChat;
    ChatView chatview;
    private Sender sender;
    private String name;
    private Color color;
    private ArrayList<ClientThread> clients;
    private XMLParser myParser;

    /**
     *
     * @param inName
     * @param inColor
     * @param inParser
     * @param inUltimatechat
     * 
     * Constructor which sets the parameters.
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
     *
     * @param inXML
     * @param inClient
     * 
     * Function that sends the recived message to all other clients in the
     * same conversation.
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
     *
     * @param inName
     * @param inColor
     * @param inText
     * @param inClient
     * 
     * Function that displays the recived message.
     */
    public void recieveTextMessage(String inName, Color inColor, String inText, ClientThread inClient) {
        chatview.addOthersText(inText, inColor, inName);
    }

    /**
     *
     * @param inOutStream
     * @param inInStream
     * 
     * Function that adds new Client to the conversation by creating a new
     * ClientThread object.
     */
    public void addClient(PrintWriter inOutStream, BufferedReader inInStream) {
        ClientThread newClient = new ClientThread(inInStream, inOutStream, myParser, this);
        clients.add(newClient);
        chatview.addToClient(newClient);
    }

    /**
     *
     * @param inText
     * 
     * Function that send a connectionrequest.
     */
    public void sendConnectionRequest(String inText) {

        try {
            clients.get(clients.size() - 1).getOutPutStream().println(myParser.sendrequestToXML(inText, name));
        } catch (Exception ex) {
            Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param inText
     * 
     * Function that send Text message to client.
     */
    public void sendText(String inText) {
        for (ClientThread i : clients) {
            try {
                System.out.println(clients.size());
                i.getOutPutStream().println(myParser.sendText(inText, name, color));
            } catch (Exception ex) {
                Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return UltimateChat
     */
    public UltimateChat getUltimateChat() {
        return myUltimateChat;
    }

    /**
     *
     * @param inColor
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
                Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ConversationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
