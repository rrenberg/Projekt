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
import java.io.PrintWriter;
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
    private Boolean aLive;
    
    
    
    public ClientThread(DataInputStream inPutStream, DataOutputStream outPutStream, XMLParser inXMLParser, ConversationController inController){
        DIStream = inPutStream;
        DOStream = outPutStream;
        myXMLParser = inXMLParser;
        myController = inController;
        aLive = true;
        
        
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
            while(aLive){
                
                respons = DIStream.readUTF();
                myController.bounceTextMessage(respons, this);
                ArrayList<String> infoTextMessage = myXMLParser.unParseXML(respons);
                System.out.println("aksldjaösdmad.amsmasdmladslmkklmad");
                System.out.println(infoTextMessage.size());
                
                if(infoTextMessage.size()==3){
                    myController.recieveTextMessage(infoTextMessage.get(0), Color.decode(infoTextMessage.get(1)), infoTextMessage.get(2), this);
                    System.out.println("asdasd");
                }else{
                    myController.recieveTextMessage(infoTextMessage.get(0), Color.decode(infoTextMessage.get(1)), "Loggar Ut!", this);
                    killClientThread();
                }
                
                System.out.println("hänger sig efter");
           
            }
        } catch (Exception ex) {
            try {
                DIStream.close();
                DOStream.close();
                //myController.getClients().remove(this);
            } catch (IOException ex1) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println("Trådens sista");
    }
    
    public void killClientThread(){
        try {
            aLive = false;
            DIStream.close();
            DOStream.close();
            System.out.println("killClientThread: "+myController.getClients().size());
            //myController.getClients().remove(this);
            System.out.println("killClientThread: "+myController.getClients().size());
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
