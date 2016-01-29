/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author The ZumBot 2.0
 */
public class ChatView extends JPanel {
    
    private JPanel userPanel;
    private JTextArea sendTextArea;
    private JTextArea showTextAreayou;
    private JTextArea showTextAreaothers;
    private JButton sendFileButton;
    private JButton sendTextButton;
    private JComboBox selectEncryption;
    private JButton selectColor;
    private JButton connectButton;
    private ConversationController controller;
    
    public ChatView(){
        
        //Test ram för testande av klassen
        JFrame frame = new JFrame("TestFrame");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(800,700);
        
  
        //Sätter alla fält
        sendTextArea = new JTextArea("<Wirte your message here>",5,40);
        showTextAreayou = new JTextArea(15,25);
        showTextAreaothers = new JTextArea(15,25);
        sendFileButton = new JButton("SendFile");
        sendTextButton = new JButton("SendText");
        selectEncryption = new JComboBox();
        selectColor = new JButton("Color");
        connectButton = new JButton("Connect");
        //controller = incontroller;
       
        sendTextArea.setLineWrap(true);
        showTextAreayou.setLineWrap(true);
        showTextAreaothers.setLineWrap(true);
        
        JPanel chatPanel = new JPanel();
        JPanel sendtextPanel = new JPanel();
        JPanel underButtonsPanel = new JPanel();
        JPanel ButtonPanel = new JPanel();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        chatPanel.setLayout(new GridLayout(1,2));
        chatPanel.add(showTextAreayou);
        chatPanel.add(showTextAreaothers);
        
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 10;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(chatPanel,c);
        
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
        
        frame.add(this);
        frame.pack();
        
        chatPanel.setVisible(true);
        sendtextPanel.setVisible(true);
        ButtonPanel.setVisible(true);
        underButtonsPanel.setVisible(true);
        this.setVisible(true);
        
        frame.setVisible(true);
        
        
    }
    
}
