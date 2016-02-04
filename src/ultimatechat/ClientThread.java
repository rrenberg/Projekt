/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.Color;
import java.io.BufferedReader;
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
public class ClientThread implements Runnable{
    
    private DataInputStream DIStream;
    private DataOutputStream DOStream;
    private XMLParser myXMLParser;
    private ConversationController myController;
    
    
    
    public ClientThread(DataInputStream inPutStream, DataOutputStream outPutStream, XMLParser inXMLParser, ConversationController inController){
        DIStream = inPutStream;
        DOStream = outPutStream;
        myXMLParser = inXMLParser;
        myController = inController;
        
        
        Thread t = new Thread(this);
        t.start();
    }
    
    public DataOutputStream getOutPutStream(){
        return DOStream;
    }

    @Override
    public void run() {
        System.out.println("Inne i client run");
        String respons;
        try {
            while((respons = DIStream.readUTF()) != null){
                
                System.out.println("Hej2");
                
                
                ArrayList<String> infoTextMessage = myXMLParser.unParseXML(respons);
                System.out.println("Hänger sig här");

                myController.recieveTextMessage(infoTextMessage.get(0), Color.decode(infoTextMessage.get(1)), infoTextMessage.get(2));
                
                System.out.println("hänger sig efter");
           
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
