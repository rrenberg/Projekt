/*
 * ClientThread
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.awt.Color;
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
 * The class ClientThread implements runnable and provieds a thread for every
 * connection and waits for messages from the other client.
 */
public class ClientThread implements Runnable {

    private BufferedReader DIStream;
    private PrintWriter DOStream;
    private XMLParser myXMLParser;
    private ConversationController myController;
    private Boolean aLive;
    private String clientName = "Okänd";

    /**
     *
     * @param inPutStream
     * @param outPutStream
     * @param inXMLParser
     * @param inController
     *
     * Constructor that sets parameters and starts thread.
     */
    public ClientThread(BufferedReader inPutStream, PrintWriter outPutStream,
            XMLParser inXMLParser, ConversationController inController) {
        
        DIStream = inPutStream;
        DOStream = outPutStream;
        myXMLParser = inXMLParser;
        myController = inController;
        aLive = true;

        Thread t = new Thread(this);
        t.start();
    }

    /**
     *
     * @return PrintWriter
     */
    public PrintWriter getOutPutStream() {
        return DOStream;
    }

    /**
     *
     * @return String
     */
    public String getClientName() {
        return clientName;
    }

    //Waits for reply, and if the message could not be parsed
    // an exception is thrown and "Okänd: Kunde inte parsa XML" is
    //displayd for the user.
    private ArrayList<String> waitforReply(String inRespons) {

        try {

            return myXMLParser.unParseXML(inRespons);

        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            ArrayList<String> errArray = new ArrayList<>();

            errArray.add("Okänd");
            errArray.add("#"
                    + Integer.toHexString(Color.RED.getRGB()).substring(2));
            errArray.add("Kunde inte parsa XML");
            return errArray;
        }
    }

    @Override
    //Run function that reads messages from the specific client, display them
    // and sends them to all other clients in the same conversation.
    public void run() {
        
        StringBuilder respons = new StringBuilder();
        try {
            while (aLive) {
                
                //Reads message
                if (respons.length() != 0) {
                    respons.append("\n");
                }
                respons.append(DIStream.readLine());
                if (DIStream.ready()) {
                    respons.append("\n");
                    respons.append(DIStream.readLine());

                }

                //When the message is recived
                if (!DIStream.ready()) {
                    
                    //Send to all others and then check if it could be parsed.
                    myController.bounceTextMessage(respons.toString(), this);
                    ArrayList<String> infoTextMessage = waitforReply(respons.toString());                                        //myXMLParser.unParseXML(respons.toString());
                    
                    //If it could be parsed and user still online.
                    if (infoTextMessage.size() == 3) {
                        
                        clientName = infoTextMessage.get(0);
                        myController.chatview.setNameToClient(this);
                        
                        myController.recieveTextMessage(infoTextMessage.get(0),
                                Color.decode(infoTextMessage.get(1)),
                                infoTextMessage.get(2), this);
                    
                    //If client disconnects.
                    } else {
                        clientName = infoTextMessage.get(0);
                        myController.chatview.setNameToClient(this);
                        myController.chatview.removeFromClient(this);
                        myController.recieveTextMessage(infoTextMessage.get(0),
                                Color.decode(infoTextMessage.get(1)), "Loggar Ut!", this);
                        killClientThread(true);
                    }
                    respons = new StringBuilder();
                    
                }
            }
            
        } catch (Exception ex) {
            
            try {
                DIStream.close();
                DOStream.close();
               
            } catch (IOException ex1) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
    }

    /**
     *
     * @param iDisconnect
     * 
     * Function killClientThread, kills the thread and disconnects.
     */
    public void killClientThread(boolean iDisconnect) {
        try {
            
            aLive = false;           
            DOStream.close();
            DIStream.close();
            
            if (iDisconnect) {
                myController.getClients().remove(this);
            }

        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
