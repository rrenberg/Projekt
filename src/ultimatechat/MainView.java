/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author The ZumBot 2.0
 */
public class MainView extends JFrame {

    private UltimateChat ultimateChat;
    private JTabbedPane chooseConversationPanel;
    private ArrayList<JPanel> conversationTabList;
    private int numberOfConversations;

    public MainView(UltimateChat ultimateChat) {
        super("UltimateChat");
        //Set frame settings
        setPreferredSize(new Dimension(1000, 1000));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        conversationTabList = new ArrayList();

        
        //Create JTabbedPane
        chooseConversationPanel = new JTabbedPane();
        chooseConversationPanel.setPreferredSize(new Dimension(900, 900));

        chooseConversationPanel.setTabPlacement(JTabbedPane.TOP);

        //Add + tab
        chooseConversationPanel.addTab(null, null);

        JButton addTabButton = new JButton("    +    ");
        addTabButton.setOpaque(false); //
        addTabButton.setBorder(null);
        addTabButton.setContentAreaFilled(false);
        addTabButton.setFocusPainted(false);

        chooseConversationPanel.setTabComponentAt(chooseConversationPanel.getTabCount()-1, addTabButton);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addConversation();
            }
        };

        addTabButton.addActionListener(listener);

        //add JTabbedPane to frame
        add(chooseConversationPanel);
        
        //Create start-conversation
        addConversation();
        
        //The first tab should be the selected tab 
        chooseConversationPanel.setSelectedIndex(0);

        pack();
        setVisible(true);
    }

    private void addConversation() {
        numberOfConversations++;
        String title = "Conversation " + String.valueOf(numberOfConversations);
        
        chooseConversationPanel.insertTab(null, null, new JPanel(), null, chooseConversationPanel.getTabCount() - 1);
        
        //create tab panel
        JPanel tabPanel = new JPanel();
        tabPanel.add(new JLabel(title));
        
        //Create close-button
        JButton closeButton = new JButton("   X   ");
        closeButton.setOpaque(false); //
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);

        tabPanel.add(closeButton);
        
        chooseConversationPanel.setTabComponentAt(chooseConversationPanel.getTabCount() - 2, tabPanel);
        
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseConversationPanel.removeTabAt(chooseConversationPanel.indexOfTabComponent(tabPanel));
            }
        };
        
        closeButton.addActionListener(listener);
        
        //Set conversationPanel to Tab
        
    }

}
