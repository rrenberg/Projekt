/*
 * MainView
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * The class MainView creates the main frame and visual GUI that is common
 * for all conversations.
 *
 * @author Rasmus Renberg and Jakob Anroldsson
 */
public class MainView extends JFrame {

    private final UltimateChat ultimateChat;
    private JTabbedPane chooseConversationPanel;
    private ArrayList<JPanel> conversationTabList;
    private int numberOfConversations;
    private ArrayList activeConversations = new ArrayList<>();

    private JButton serverButton;

    /**
     * Constructor that creates the mainView and sets the visual GUI.
     *
     * @param inultimateChat The corresponding UltimateChate object
     */
    public MainView(UltimateChat inultimateChat) {
        super("UltimateChat");
        ultimateChat = inultimateChat;
        
        //Set frame settings
        setPreferredSize(new Dimension(1150, 700));
        setMinimumSize(new Dimension(1150, 700));

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        conversationTabList = new ArrayList();
        addWindowListener(new WindowAdapter() {
            @Override
            //If the main frame is closed.
            public void windowClosing(WindowEvent e) {
                try {
                    //Kills all conversations.
                    for (ConversationController c : 
                            ultimateChat.conversationControllerList) {
                        
                        c.killConversation();
                    }
                    
                    ultimateChat.serverSocket.close();
                    ultimateChat.setServer(false);
                    System.out.println("Closed");
                    System.exit(0);

                } catch (IOException ex) {
                    Logger.getLogger(MainView.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });

        //Create JTabbedPane
        chooseConversationPanel = new JTabbedPane();
        chooseConversationPanel.setPreferredSize(new Dimension(600, 500));

        chooseConversationPanel.setTabPlacement(JTabbedPane.TOP);

        //Add + tab
        chooseConversationPanel.addTab(null, null);

        JButton addTabButton = new JButton("    +    ");
        addTabButton.setOpaque(false); //
        addTabButton.setBorder(null);
        addTabButton.setContentAreaFilled(false);
        addTabButton.setFocusPainted(false);

        chooseConversationPanel.setTabComponentAt(chooseConversationPanel.
                getTabCount() - 1, addTabButton);
        
        //When the "+" is pressed
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ultimateChat.createNewConversationController();
                addConversation();
            }
        };

        addTabButton.addActionListener(listener);

        //add JTabbedPane to frame
        add(chooseConversationPanel);

        //Create start-conversation
        //addConversation();
        //The first tab should be the selected tab 
        serverButton = new JButton("Server: ON");
        serverButton.setBackground(Color.GREEN);
        serverButton.setOpaque(true);
        serverButton.setBorderPainted(false);
        serverButton.addActionListener(new ActionListener() {

            @Override
            //Control the event when the serverbutton is pressed.
            public void actionPerformed(ActionEvent e) {
                if (serverButton.getBackground() == Color.GREEN) {
                    
                    //Sets the color to red and turns of the server.
                    serverButton.setBackground(Color.RED);
                    serverButton.setText("Server: OFF");
                    
                    try {
                        ultimateChat.serverSocket.close();
                        ultimateChat.server = false;
                    } catch (Exception ex) {
                        Logger.getLogger(ChatView.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    
                    //Turns the color to green and turns on the server
                    serverButton.setBackground(Color.GREEN);
                    serverButton.setText("Server: ON");
                    ultimateChat.server = true;
                    Thread thread = new Thread(ultimateChat);
                    thread.start();
                }
            }

        });
        
        //Adds server button to panel and to main view.
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(serverButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Function getTabTitel returns the title of the tab.
     *
     * @param index Which index we seek the tab title of.
     * @return
     */
    public String getTabTitle(int index) {
        return chooseConversationPanel.getTitleAt(index);
    }

    /**
     *Function addConversation, creates a new conversation tab for the 
     * new conversation.
     */
    public void addConversation() {
        numberOfConversations++;
        String title = "Conversation " + String.valueOf(numberOfConversations);
        activeConversations.add("Conversation " + 
                String.valueOf(numberOfConversations));

        chooseConversationPanel.insertTab("Conversation " + 
                String.valueOf(numberOfConversations), null,
                ultimateChat.getConvController().chatview,
                null, chooseConversationPanel.getTabCount() - 1);

        //create tab panel
        final JPanel tabPanel = new JPanel();
        tabPanel.add(new JLabel(title));
        activeConversations.add(title);
        activeConversations.add(ultimateChat.getConvController());

        //Create close-button
        JButton closeButton = new JButton("   X   ");
        closeButton.setOpaque(false); //
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);

        tabPanel.add(closeButton);
        tabPanel.setOpaque(false);

        chooseConversationPanel.setTabComponentAt(chooseConversationPanel.
                getTabCount() - 2, tabPanel);

        ActionListener listener = new ActionListener() {
            //If the close "X" is pressed, kill conversation.
            public void actionPerformed(ActionEvent e) {

                ConversationController c = (ConversationController) ultimateChat.
                        getConvControllerList().get(chooseConversationPanel.
                                indexOfTabComponent(tabPanel));
                
                c.killConversation();

                ultimateChat.getConvControllerList().remove(chooseConversationPanel.
                        indexOfTabComponent(tabPanel));
                chooseConversationPanel.removeTabAt(chooseConversationPanel.
                        indexOfTabComponent(tabPanel));

            }
        };

        closeButton.addActionListener(listener);

        //Set conversationPanel to Tab
        chooseConversationPanel.setSelectedIndex(0);
    }

}
