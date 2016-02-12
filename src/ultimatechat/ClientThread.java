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
    
    private BufferedReader DIStream;
    private PrintWriter DOStream;
    private XMLParser myXMLParser;
    private ConversationController myController;
    private Boolean aLive;
    private String clientName = "Okänd";
    
    
    
    public ClientThread(BufferedReader inPutStream, PrintWriter outPutStream, XMLParser inXMLParser, ConversationController inController){
        DIStream = inPutStream;
        DOStream = outPutStream;
        myXMLParser = inXMLParser;
        myController = inController;
        aLive = true;
        
        
        Thread t = new Thread(this);
        t.start();
    }
    
    public PrintWriter getOutPutStream(){
        return DOStream;
    }
    
    public String getClientName(){
        return clientName;
    }
    
    private ArrayList<String> waitforReply(String inRespons){
        
        try {
            
           
            return myXMLParser.unParseXML(inRespons);
        
           
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            ArrayList<String> errArray = new ArrayList<>();
           
                   errArray.add("Okänd");
           
           errArray.add("#"+
                Integer.toHexString(Color.RED.getRGB()).substring(2));
           errArray.add("Kunde inte parsa XML");
           return errArray;
        }
    }

    @Override
    public void run() {
        System.out.println("Inne i client run");
        StringBuilder respons =new StringBuilder();
        try {
            while(aLive){
                if(respons.length()!=0){
                    respons.append("\n");
                }
                respons.append(DIStream.readLine());
                if(DIStream.ready()){
                    respons.append("\n");
                    respons.append(DIStream.readLine());
                    
                }
                
                //respons = DIStream.readLine();
                if(!DIStream.ready()){
                myController.bounceTextMessage(respons.toString(), this);
                ArrayList<String> infoTextMessage = waitforReply(respons.toString());                                        //myXMLParser.unParseXML(respons.toString());
                System.out.println("aksldjaösdmad.amsmasdmladslmkklmad");
                System.out.println(infoTextMessage.size());
                
                if(infoTextMessage.size()==3){
                    clientName =infoTextMessage.get(0);
                    myController.chatview.setNameToClient(this);
                    myController.recieveTextMessage(infoTextMessage.get(0), Color.decode(infoTextMessage.get(1)), infoTextMessage.get(2), this);
                    System.out.println("asdasd");
                }else{
                    clientName =infoTextMessage.get(0);
                    myController.chatview.setNameToClient(this);
                    myController.recieveTextMessage(infoTextMessage.get(0), Color.decode(infoTextMessage.get(1)), "Loggar Ut!", this);
                    killClientThread(true);
                }
                respons =new StringBuilder();
                System.out.println("hänger sig efter");
                }
            }
        } catch (Exception ex) {
            
            try {
                DIStream.close();
                DOStream.close();
                System.out.println("Stänger strömmarna");
                //myController.getClients().remove(this);
            } catch (IOException ex1) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println("Trådens sista");
    }
    
    public void killClientThread(boolean iDisconnect){
        try {
            System.out.println("Inne i kilClient");
            aLive = false;
            System.out.println("Ovan inStream");
            DOStream.close();//DIStream.close();
            System.out.println("Ovan outStream");
            DIStream.close();//DOStream.close();
            System.out.println("killClientThread: "+myController.getClients().size());
            if(iDisconnect){
                myController.getClients().remove(this);
            }
            System.out.println("killClientThread: "+myController.getClients().size());
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
