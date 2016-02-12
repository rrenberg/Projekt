/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author rasmus
 */
public class StartUpThread implements Runnable {

    BufferedReader inStream;
    UltimateChat ultimateChat;
    PrintWriter outStream;
    CreateDialogForConnectionRequest C;

    public StartUpThread(BufferedReader inInStream, PrintWriter inOutStream, UltimateChat inUltimateChat) {
        inStream = inInStream;
        ultimateChat = inUltimateChat;
        outStream = inOutStream;
        
    }

    @Override
    public void run() {
        StringBuilder respons = new StringBuilder();
        try {
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 3 * 1000 && !inStream.ready()) {

            }
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
                
                System.out.println("Respons:" + respons.toString());
                System.out.println(respons.toString().length() == 0);
                //String stringMessage = inStream.readLine();

                //createDialogForNameAndPort();
                if (!inStream.ready() && !respons.toString().equals("null")) {
                    System.out.println("Request"+ultimateChat.xmlParser.checkIfRequest(respons.toString()));
                    if (respons.toString().length() == 0 || !ultimateChat.xmlParser.checkIfRequest(respons.toString())) {
                        C = new CreateDialogForConnectionRequest("E-klient vill ansluta", ultimateChat.conversationControllerList, ultimateChat.mainView);
                    } else {
                        C = new CreateDialogForConnectionRequest(ultimateChat.xmlParser.requestParser(respons.toString()), ultimateChat.conversationControllerList, ultimateChat.mainView);
                    }
                    System.out.println(respons.toString());
                    System.out.println(respons.toString().length());
                    //CreateDialogForConnectionRequest C= new CreateDialogForConnectionRequest(xmlParser.requestParser(respons.toString()), conversationControllerList, mainView);
                    ArrayList answer = C.createDialogForConnectionRequestPopup();

                    if ((int) answer.get(0) == 0) {
                        if ((int) answer.get(1) == 0) {
                            ultimateChat.createNewConversationController();
                            ultimateChat.conversationControllerList.get(ultimateChat.conversationControllerList.size() - 1).addClient(outStream, inStream);

                            ultimateChat.mainView.addConversation();
                        } else {
                            ultimateChat.conversationControllerList.get((int) answer.get(1) - 1).addClient(outStream, inStream);
                        }
                    } else if ((int) answer.get(0) == 2) {
                        if (respons.toString().length() == 0 || !ultimateChat.xmlParser.checkIfRequest(respons.toString())) {
                            outStream.println(ultimateChat.xmlParser.sendText("Connection denied",ultimateChat.name,Color.RED));
                            outStream.println(ultimateChat.xmlParser.disconnectXML(ultimateChat.name,Color.RED));
                            inStream.close();
                            outStream.close();
                        } else {
                            outStream.println(ultimateChat.xmlParser.sendText("Connection denied",ultimateChat.name,Color.RED));
                            outStream.println(ultimateChat.xmlParser.disconnectXML(ultimateChat.name,Color.RED));
                            inStream.close();
                            outStream.close();
                        }   
                    }

                    System.out.println("Inne i loopen");
                    respons = new StringBuilder();
                }
            }else{
                C = new CreateDialogForConnectionRequest("E-klient vill ansluta", ultimateChat.conversationControllerList, ultimateChat.mainView);
            ArrayList answer = C.createDialogForConnectionRequestPopup();

                    if ((int) answer.get(0) == 0) {
                        if ((int) answer.get(1) == 0) {
                            ultimateChat.createNewConversationController();
                            ultimateChat.conversationControllerList.get(ultimateChat.conversationControllerList.size() - 1).addClient(outStream, inStream);

                            ultimateChat.mainView.addConversation();
                        } else {
                            ultimateChat.conversationControllerList.get((int) answer.get(1) - 1).addClient(outStream, inStream);
                        }
                    } else if ((int) answer.get(0) == 2) {

                    }
            
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
