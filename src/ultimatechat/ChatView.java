/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 *
 * @author The ZumBot 2.0
 */
public class ChatView extends JPanel implements ActionListener {
    
    private JPanel userPanel;
    private JTextArea sendTextArea;
    private JTextArea showTextAreayou;
    //private JTextArea showTextAreaothers;
    private JTextPane myText;
    private JScrollPane showTextArea;
    private JButton sendFileButton;
    private JButton sendTextButton;
    private JComboBox selectEncryption;
    private JButton selectColor;
    private JButton connectButton;
    private ConversationController controller;
    //private String[] myText = new String[25];
    //private String[] othersText = new String[25];
    private JPanel chatPanel;
    
    public ChatView(ConversationController inController){
        
        //Sätter text arrayerna
        //for(Integer i=0; i < myText.length;i++){
        //    myText[i] = "\n";
       // }
        
        //for(Integer i=0; i < othersText.length;i++){
        //    othersText[i] = "\n";
        //}
  
        //Sätter alla fält
        controller = inController;
        sendTextArea = new JTextArea("<Wirte your message here>",5,40);
        showTextAreayou = new JTextArea(25,40);
        //showTextAreaothers = new JTextArea(25,40);
        chatPanel = new JPanel(new BorderLayout());
        //chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));
        showTextArea = new JScrollPane(chatPanel);
        
       
        myText = new JTextPane();
        chatPanel.add(myText);
        
        sendFileButton = new JButton("SendFile");
        sendFileButton.addActionListener(this);
        
        
        sendTextButton = new JButton("SendText");
        sendTextButton.addActionListener(this);
        
        selectEncryption = new JComboBox();
        selectColor = new JButton("Color");
        selectColor.addActionListener(this);
        
        
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        
        //controller = incontroller;
        
        sendTextArea.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                sendTextArea.setText("");
            }});
        
        
        showTextArea.setPreferredSize(new Dimension(950,500));
        //showTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sendTextArea.setLineWrap(true);
        //showTextAreayou.setLineWrap(true);
        //showTextAreaothers.setLineWrap(true);
        
        
        JPanel sendtextPanel = new JPanel();
        JPanel underButtonsPanel = new JPanel();
        JPanel ButtonPanel = new JPanel();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        //chatPanel.setLayout(new GridLayout(1,2));
        //chatPanel.add(showTextAreayou);
        //chatPanel.add(showTextAreaothers);
        
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 10;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(showTextArea,c);
        
        sendtextPanel.add(sendTextArea);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 13;
        c.gridwidth = 6;
        c.gridheight = 4;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        this.add(sendTextArea,c);
        
        
        ButtonPanel.setLayout(new GridLayout(3,1,5,0));
        ButtonPanel.add(sendTextButton);
        ButtonPanel.add(sendFileButton);
        ButtonPanel.add(connectButton);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 9;
        c.gridy = 13;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sendTextButton,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 9;
        c.gridy = 14;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sendFileButton,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 9;
        c.gridy = 15;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(connectButton,c);
        
        underButtonsPanel.setLayout(new GridLayout(1,2,0,5));
        //underButtonsPanel.setPreferredSize(new Dimension(400,30));
        underButtonsPanel.add(selectEncryption);
        underButtonsPanel.add(selectColor);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 18;
        c.gridwidth = 1;
        this.add(selectColor,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 18;
        c.gridwidth = 1;
        this.add(selectEncryption ,c);
       // this.add(chatPanel);
        //this.add(sendtextPanel);
        //this.add(ButtonPanel);
        //this.add(underButtonsPanel);
        //this.setPreferredSize(new Dimension(600,600));
        
        //frame.add(this);
        //frame.pack();
        
        /*showTextAreayou.setForeground(Color.red);
        showTextAreaothers.setForeground(Color.BLUE);
        
        showTextAreayou.setEditable(false);
        showTextAreaothers.setEditable(false);
        showTextAreayou.append("Johan: Hej");
        showTextAreaothers.append("\n"+"Erik: Hej");
        showTextAreayou.append("\n"+"\n"+"Johan: Vad gör du?");
        */
        
        
        this.addMyText("11111Hej Detta är min text: hasdhkjsaasäfjlsajfgösahgökhsaökhgöksahgökhsaköghsaköghkasöhgökashköghaskghsaökghköashgkösahgkögfjlasgfjgsadjkfgkjagfjlasghfljgaslfgaslfgljsagfjlsagljfgaslfgjlasgflj");
        
        
        chatPanel.setVisible(true);
        sendtextPanel.setVisible(true);
        ButtonPanel.setVisible(true);
        underButtonsPanel.setVisible(true);
        this.setVisible(true);
        
        //frame.setVisible(true);
        
        
    }
    
    public void addMyText(String inText){
        
        StyledDocument doc = myText.getStyledDocument();
        Style style = myText.addStyle("Hej", null);
        StyleConstants.setForeground(style, controller.getColor());
        
        try{
            doc.insertString(doc.getLength(), inText+"\n", style);
        }catch(BadLocationException e){
            
        }
       
        showTextArea.setViewportView(chatPanel);
    }
    
    public void addOthersText(String inoText, Color inColor, String inName){
        
        StyledDocument doc = myText.getStyledDocument();
        Style style = myText.addStyle("Hej", null);
        StyleConstants.setForeground(style, inColor);
        System.out.println("i others");
 
        try{
            doc.insertString(doc.getLength(), inName+ ": "+inoText+"\n", style);
        }catch(BadLocationException e){
            
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==connectButton){
            ConnectView conView = new ConnectView();
            System.out.println("Tjohej");
            try {
                Socket clientsocket = new Socket(conView.getAddress(),conView.getPort());
                DataInputStream inStream = new DataInputStream(clientsocket.getInputStream());
                DataOutputStream outStream = new DataOutputStream(clientsocket.getOutputStream());
                
                
                controller.addClient(outStream, inStream);
                System.out.println(conView.getTextMessage());
                controller.sendConnectionRequest(conView.getTextMessage());
            } catch (IOException ex) {
                Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(e.getSource() == sendTextButton){
            String myTextmessage = sendTextArea.getText();
            String textToBeAdded = controller.getName()+": "+myTextmessage;
            addMyText(textToBeAdded);
            
            controller.sendText(myTextmessage);
            
            sendTextArea.setText("");
            
        }
        
        if(e.getSource()== selectColor){
            Color c = JColorChooser.showDialog(null,"Choose a Color", controller.getColor());
            if(c!=null){
                controller.setColor(c);
            }
        }
        
        if(e.getSource()==sendFileButton){
            CreateDialogForConnectionRequest C = new CreateDialogForConnectionRequest("Hej", controller.getUltimateChat().getConvControllerList(), controller.getUltimateChat().mainView);
            ArrayList answer= C.createDialogForConnectionRequestPopup();
        }
    }
    
}
