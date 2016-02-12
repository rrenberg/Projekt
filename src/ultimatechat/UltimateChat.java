/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.Box.createVerticalStrut;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author The ZumBot 2.0
 */
public class UltimateChat implements Runnable {
    
    MainView mainView;
    ArrayList<ConversationController> conversationControllerList;
    int port;
    String name;
    ServerSocket serverSocket;
    XMLParser xmlParser;
    CreateDialogForConnectionRequest C;
    boolean server=true;
    
    public UltimateChat() {
        mainView = new MainView(this);
        createDialogForNameAndPort();
        xmlParser = new XMLParser();
        //System.out.println("ooooo:" +xmlParser.unParseXML("<message sender=\""+"Rasmus"+"\">"+"<text color=\"#"+
        //        Integer.toHexString(Color.red.getRGB()).substring(2)+"\" ostsort=\"cheddar\">"+"<disconnect/><kursiv> ajskhd </kursiv> ,jashdlhaksd <fetstill> ajskhd </fetstill> jag är en apa"+
        //        "</text></message>").size());
        conversationControllerList = new ArrayList<ConversationController>();
        createNewConversationController();
        mainView.addConversation();
    
        
        Thread t = new Thread(this);
        t.start();
        
        
        
        //conversationControllerList.add(new ConversationController("Johan",3));
        //ConnectView v = new ConnectView();
        
    }
    
    public void createNewConversationController(){
        ConversationController c = new ConversationController(name,Color.BLUE,xmlParser,this);
        
        conversationControllerList.add(c);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UltimateChat();
        
    }
    
    @Override
    public void run() {
        System.out.println("Inne i run");
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Inne i ServerSocket");
	} catch (IOException e) {
	    System.out.println("Could not listen on port: 4444");
	    System.exit(-1);
        }
        
        StringBuilder respons =new StringBuilder();
        while(server){
            Socket clientsocket = null;
            try{
                System.out.println("Lyssnar nu");
                clientsocket = serverSocket.accept();
                BufferedReader inStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                PrintWriter outStream = new PrintWriter(clientsocket.getOutputStream(), true);
                
                System.out.println("Respons är:");
                System.out.println(inStream.ready());
                Thread thread = new Thread(new StartUpThread(inStream, outStream,this));
                thread.start();
                //System.out.println(inStream.readLine());
                
              /*  respons.append(inStream.readLine());
                
                while(inStream.ready()){
                    if(respons.length()!=0){
                        respons.append("\n");
                    }
                
                    respons.append(inStream.readLine());
                    if(inStream.ready()){
                        respons.append("\n");
                        respons.append(inStream.readLine());

                    }
                }
                
                System.out.println("Respons:"+respons.toString());
                System.out.println(respons.toString().length()==0);
                //String stringMessage = inStream.readLine();
                
                //createDialogForNameAndPort();
                if(!inStream.ready() && !respons.toString().equals("null")){
                   
                    if(respons.toString().length()==0){
                         C= new CreateDialogForConnectionRequest("E-klient vill ansluta", conversationControllerList, mainView);
                    }else{
                         C= new CreateDialogForConnectionRequest(xmlParser.requestParser(respons.toString()), conversationControllerList, mainView);
                    }
                    System.out.println(respons.toString());
                    System.out.println(respons.toString().length());
                    //CreateDialogForConnectionRequest C= new CreateDialogForConnectionRequest(xmlParser.requestParser(respons.toString()), conversationControllerList, mainView);
                    ArrayList answer= C.createDialogForConnectionRequestPopup();

                    if((int)answer.get(0) == 0){
                        if((int) answer.get(1)==0){
                           createNewConversationController();
                           conversationControllerList.get(conversationControllerList.size()-1).addClient(outStream,inStream);

                           mainView.addConversation();
                        }else{
                           conversationControllerList.get((int)answer.get(1)-1).addClient(outStream,inStream);
                        }
                    }else if ((int)answer.get(0)==2){

                    }

                    System.out.println("Inne i loopen");
                    respons =new StringBuilder();
                    }*/
            }catch(Exception ex){
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
           
    
    }
    
    public void createDialogForNameAndPort(){
        
        // Create textfields and panel for the Dialog
        JTextField nameTextField = new JTextField(6);
        JTextField portTextField = new JTextField(6);
        
        JPanel dialogPanel = new JPanel();
        
        // Add textfields to the dialog
        dialogPanel.add(new JLabel("Name:"));
        dialogPanel.add(nameTextField);
        dialogPanel.setLayout(new FlowLayout());
        dialogPanel.add(new JLabel("Port:"));
        dialogPanel.add(portTextField);
        
        JOptionPane.showConfirmDialog(null, dialogPanel,
                "Please enter your name and port", JOptionPane.DEFAULT_OPTION);
        
        //Set name and port
        if(nameTextField.getText().equals("")){
            name = "UnNamed";
        }else{
            name = nameTextField.getText();
        }
        
        try{
            if(portTextField.getText() == null){
                port = 4000;
            }else{

                port = Integer.valueOf(portTextField.getText());

            }
        
        }catch(Exception ex){
                port = 4000;
                JFrame tempJF = new JFrame("Bad Port");
                JOptionPane.showMessageDialog(tempJF, "Varning! Invalid port choosen, you get port 4000 ",
                        "Bad Port",
                        JOptionPane.WARNING_MESSAGE);
        }finally{
        //Add to name and port to MainView
        JPanel upperPanel = new JPanel();
        try {
            InetAddress IP=InetAddress.getLocalHost();
            upperPanel.add(new JTextField("Your address: "+IP.getHostAddress()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(UltimateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        upperPanel.add(new JTextField("Your name: "+name));
        upperPanel.add(new JTextField("Port: "+String.valueOf(port)));
        
        mainView.add(upperPanel,BorderLayout.NORTH);
        mainView.show();
    }
    }
    
    public ConversationController getConvController(){
        return conversationControllerList.get(conversationControllerList.size()-1);
    }
    
    public ArrayList getConvControllerList(){
        return conversationControllerList;
    }
    
    public void setServer(boolean inBoolean){
        server=inBoolean;
    }
}
