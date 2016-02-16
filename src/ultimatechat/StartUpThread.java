/*
 * StartUpThread
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * The Class StartUpThread implements Runneble and provides a temporary Thread
 * and connection during the first phase of the conversation.
 *
 * @author Rasmus Rehberg and Jakob Arnoldsson
 */
public class StartUpThread implements Runnable {

    BufferedReader inStream;
    UltimateChat ultimateChat;
    PrintWriter outStream;
    CreateDialogForConnectionRequest C;

    /**
     *Constructor which set the parameters
     * 
     * @param inInStream
     * @param inOutStream
     * @param inUltimateChat
     */
    public StartUpThread(BufferedReader inInStream, PrintWriter inOutStream, UltimateChat inUltimateChat) {
        inStream = inInStream;
        ultimateChat = inUltimateChat;
        outStream = inOutStream;

    }

    @Override
    /**
     * Run method of this thread. Waits on the first message or maximum of 3
     * seconds, then creates a connectionView and lets the user determine if a
     * conversation should start or not.
     */
    public void run() {
        StringBuilder respons = new StringBuilder();

        try {

            //Waits for first message or a maximum of 3 seconds.
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 3 * 1000
                    && !inStream.ready()) {

            }

            //Reads the first message.
            if (inStream.ready()) {

                respons.append(inStream.readLine());

                while (inStream.ready()) {
                    if (respons.length() != 0) {
                        respons.append("\n");
                    }

                    respons.append(inStream.readLine());
                    if (inStream.ready()) {
                        respons.append("\n");
                        respons.append(inStream.readLine());

                    }
                }

                //Creats connectionview and waits for answer. If no, disconnects
                //otherwise creats new conversation.
                if (!inStream.ready() && !respons.toString().equals("null")) {
                    if (respons.toString().length() == 0
                            || !ultimateChat.xmlParser.checkIfRequest(respons.toString())) {

                        C = new CreateDialogForConnectionRequest("E-klient vill ansluta",
                                ultimateChat.conversationControllerList, ultimateChat.mainView);

                    } else {
                        C = new CreateDialogForConnectionRequest(ultimateChat.xmlParser.requestParser(
                                respons.toString()), ultimateChat.conversationControllerList,
                                ultimateChat.mainView);
                    }
                    
                    //Creats popup for connection
                    ArrayList answer = C.createDialogForConnectionRequestPopup();
                    
                    //Creats new conversation if user accepts.
                    if ((int) answer.get(0) == 0) {
                        if ((int) answer.get(1) == 0) {
                            
                            ultimateChat.createNewConversationController();
                            ultimateChat.conversationControllerList.
                                    get(ultimateChat.conversationControllerList.size() - 1).
                                    addClient(outStream, inStream);

                            ultimateChat.mainView.addConversation();
                            
                        } else {
                            //Adds client to old conversation.
                            ultimateChat.conversationControllerList.
                                    get((int) answer.get(1) - 1).
                                    addClient(outStream, inStream);
                            
                        }
                        
                    //If user denies conversation request.
                    }else if ((int) answer.get(0) == 2){

                        outStream.println(ultimateChat.xmlParser.
                                sendText("Connection denied", ultimateChat.name, Color.RED));
                        outStream.println(ultimateChat.xmlParser.
                                disconnectXML(ultimateChat.name, Color.RED));
                        outStream.close();
                        inStream.close();

                    }

                    respons = new StringBuilder();
                }
                
            //If we waited 3 seconds, say E-klient want to connect.
            } else {
                C = new CreateDialogForConnectionRequest("E-klient vill ansluta",
                        ultimateChat.conversationControllerList, ultimateChat.mainView);
                ArrayList answer = C.createDialogForConnectionRequestPopup();

                if ((int) answer.get(0) == 0) {
                    if ((int) answer.get(1) == 0) {
                        
                        ultimateChat.createNewConversationController();
                        ultimateChat.conversationControllerList.get(ultimateChat.
                                conversationControllerList.size() - 1).
                                addClient(outStream, inStream);

                        ultimateChat.mainView.addConversation();
                        
                    } else {
                        
                        ultimateChat.conversationControllerList.
                                get((int) answer.get(1) - 1).
                                addClient(outStream, inStream);
                    }
                    
                } else if ((int) answer.get(0) == 2) {
                    outStream.println(ultimateChat.xmlParser.
                            sendText("Connection denied", ultimateChat.name, Color.RED));
                    outStream.println(ultimateChat.xmlParser.
                            disconnectXML(ultimateChat.name, Color.RED));
                    outStream.close();
                    inStream.close();
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
