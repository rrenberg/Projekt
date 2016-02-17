/*
 * ChatView
 *
 * Version 1.0
 *
 * 16-02-2016
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
 * The class ChatView extends JPanel and implements ActionListener. The class
 * provides the visual GUI to the specific conversation.
 *
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class ChatView extends JPanel implements ActionListener {

    private ConversationController controller;

    private JTextArea sendTextArea;

    private JTextPane showTextPane;
    private JScrollPane scrollPaneShowText;

    private JButton sendFileButton;
    private JButton sendTextButton;
    private JComboBox selectEncryption;
    private JButton selectColor;
    private JButton connectButton;

    private JPanel showTextPanel;
    private JPanel disconnectPanel;
    private JComboBox clientDropDown;
    private JButton disconnectButton;

    private ArrayList<ClientThread> clientArrayList = new ArrayList<>();

    /**
     * The constructor of ChatView. Creates all nessecary panels and buttons
     * etc.
     *
     * @param inController The ConversationController connected to this
     * ChatView.
     */
    public ChatView(ConversationController inController) {

        controller = inController;

        // First do the area where text is shown
        showTextPanel = new JPanel(new GridBagLayout());
        showTextPanel.setBackground(Color.WHITE);

        showTextPane = new JTextPane();

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        showTextPanel.add(showTextPane, c);

        scrollPaneShowText = new JScrollPane();
        scrollPaneShowText.setViewportView(showTextPanel);
        scrollPaneShowText.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneShowText.setBackground(Color.WHITE);

        scrollPaneShowText.getViewport().setBackground(Color.WHITE);

        scrollPaneShowText.setPreferredSize(new Dimension(600, 300));

        // Create the buttons 
        sendFileButton = new JButton("SendFile");
        sendFileButton.addActionListener(this);

        sendTextButton = new JButton("SendText");
        sendTextButton.addActionListener(this);

        selectEncryption = new JComboBox();
        selectEncryption.addItem("<Encryption>");

        selectColor = new JButton("Color");
        selectColor.addActionListener(this);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);

        // The area where you send text
        JPanel sendtextPanel = new JPanel(new BorderLayout());

        sendTextArea = new JTextArea("<Write your message here>", 5, 72);
        sendTextArea.setLineWrap(true);

        sendtextPanel.add(sendTextArea, BorderLayout.LINE_START);
        sendtextPanel.setBackground(Color.WHITE);

        JScrollPane scrollSendText = new JScrollPane();
        scrollSendText.setViewportView(sendtextPanel);

        sendTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendTextArea.setText("");
            }
        });

        // The disconnect panel
        disconnectPanel = new JPanel(new GridLayout(3, 0));
        disconnectPanel.add((new JLabel("Disconnect From: ")));
        disconnectPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        clientDropDown = new JComboBox();
        clientDropDown.addItem("...........");
        clientDropDown.setPreferredSize(new Dimension(100, 10));
        clientDropDown.setMaximumSize(clientDropDown.getPreferredSize());
        clientArrayList.add(null);
        disconnectPanel.add(clientDropDown);
        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(this);
        disconnectPanel.add(disconnectButton);

        // Finally add to this panel
        setLayout(new GridBagLayout());

        GridBagConstraints d = new GridBagConstraints();

        d.fill = GridBagConstraints.HORIZONTAL;
        d.gridx = 0;
        d.gridy = 0;
        d.gridwidth = 10;
        d.gridheight = 10;
        d.anchor = GridBagConstraints.NORTHWEST;
        add(scrollPaneShowText, d);

        d.fill = GridBagConstraints.HORIZONTAL;
        d.gridx = 0;
        d.gridy = 11;
        d.gridwidth = 6;
        d.gridheight = 4;
        d.anchor = GridBagConstraints.LINE_START;
        add(scrollSendText, d);

        d.fill = GridBagConstraints.BOTH;
        d.gridx = 12;
        d.gridy = 0;
        d.gridwidth = 4;
        d.gridheight = 1;
        d.anchor = GridBagConstraints.LINE_END;
        add(disconnectPanel, d);

        d.fill = GridBagConstraints.VERTICAL;
        d.gridx = 7;
        d.gridy = 11;
        d.gridwidth = 1;
        d.gridheight = 1;
        d.anchor = GridBagConstraints.LINE_START;
        add(sendTextButton, d);

        d.fill = GridBagConstraints.VERTICAL;
        d.gridx = 7;
        d.gridy = 12;
        d.gridwidth = 1;
        d.gridheight = 1;
        d.anchor = GridBagConstraints.LINE_START;
        add(sendFileButton, d);

        d.fill = GridBagConstraints.HORIZONTAL;
        d.gridx = 7;
        d.gridy = 13;
        d.gridwidth = 1;
        d.gridheight = 1;
        add(connectButton, d);

        d.fill = GridBagConstraints.VERTICAL;
        d.gridx = 0;
        d.gridy = 16;
        d.gridwidth = 1;
        d.gridheight = 1;
        add(selectColor, d);

        d.fill = GridBagConstraints.VERTICAL;
        d.gridx = 2;
        d.gridy = 16;
        d.gridwidth = 1;
        d.gridheight = 1;
        add(selectEncryption, d);

        setVisible(true);
    }

    /**
     * Adds text with the user's name and color to showTextPane in ChatView.
     *
     * @param inText The text that should be added.
     */
    public void addMyText(String inText) {

        StyledDocument doc = showTextPane.getStyledDocument();
        Style style = showTextPane.addStyle(null, null);
        StyleConstants.setForeground(style, controller.getColor());
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);

        try {
            doc.insertString(doc.getLength(), inText + "\n", style);
        } catch (BadLocationException e) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, e);
        }

        scrollPaneShowText.setViewportView(showTextPanel);
        JScrollBar scrollbar = scrollPaneShowText.getVerticalScrollBar();
        scrollbar.setValue(scrollbar.getMaximum());
    }

    /**
     * Adds text with the name of inName and the color of inColor to
     * showTextPane in ChatView.
     *
     * @param inoText The text that will be added.
     * @param inColor The color in which the text will be written.
     * @param inName The name of the writer.
     */
    public void addOthersText(String inoText, Color inColor, String inName) {

        StyledDocument doc = showTextPane.getStyledDocument();
        Style style = showTextPane.addStyle(null, null);
        StyleConstants.setForeground(style, inColor);

        try {
            doc.insertString(doc.getLength(), inName + ": " + inoText + "\n", style);
        } catch (BadLocationException e) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, e);
        }
        JScrollBar scrollbar = scrollPaneShowText.getVerticalScrollBar();
        scrollbar.setValue(scrollbar.getMaximum());

    }

    /**
     * Adds a client to the clientDropDown.
     *
     * @param ct The client that is added.
     */
    public void addToClient(ClientThread ct) {
        clientDropDown.addItem(ct.getClientName());
        clientArrayList.add(ct);
    }

    /**
     * Sets the correct name to the client in clientDropDown. Is called every
     * time a client sends a message.
     *
     * @param ct The client which name is to be set.
     */
    public void setNameToClient(ClientThread ct) {
        for (int i = 0; i < clientArrayList.size(); i++) {
            if (ct == clientArrayList.get(i)) {

                clientDropDown.addItem(ct.getClientName());
                clientDropDown.removeItem(clientDropDown.getItemAt(i));

                ClientThread clientToBeMoved = clientArrayList.get(i);

                clientArrayList.remove(i);
                clientArrayList.add(clientToBeMoved);

            }
        }
    }

    /**
     * Removes client from clientDropDown.
     *
     * @param ct The client that is removed.
     */
    public void removeFromClient(ClientThread ct) {

        clientDropDown.removeItem(ct.getClientName());
        clientArrayList.remove(ct);

    }

    /**
     * Returns an ArrayList with the client's ClientThreads
     *
     * @return the ArrayList with the client's ClientThreads
     */
    public ArrayList<ClientThread> getClientList() {
        return clientArrayList;
    }

    /**
     * Returns an JComboBox with the clients
     *
     * @return an JComboBox with the clients
     */
    public JComboBox getDropDown() {
        return clientDropDown;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            ConnectView conView = new ConnectView();
            if (conView.getConnectionOk()) {
                try {
                    Socket clientsocket = new Socket(conView.getAddress(), conView.getPort());
                    BufferedReader inStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                    PrintWriter outStream = new PrintWriter(clientsocket.getOutputStream(), true);

                    controller.addClient(outStream, inStream);

                    controller.sendConnectionRequest(conView.getTextMessage());
                } catch (IOException ex) {
                    Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        if (e.getSource() == sendTextButton) {

            String myTextmessage = sendTextArea.getText();
            String textToBeAdded = controller.getName() + ": " + myTextmessage;
            addMyText(textToBeAdded);

            controller.sendText(myTextmessage);

            sendTextArea.setText("");

        }

        if (e.getSource() == selectColor) {
            Color c = JColorChooser.showDialog(null, "Choose a Color", controller.getColor());
            if (c != null) {
                controller.setColor(c);

            }
        }

        if (e.getSource() == sendFileButton) {
            CreateDialogForConnectionRequest C = new CreateDialogForConnectionRequest(null, controller.getUltimateChat().getConvControllerList(), controller.getUltimateChat().mainView);
            ArrayList answer = C.createDialogForConnectionRequestPopup();
        }
        if (e.getSource() == disconnectButton) {

            if (clientDropDown.getSelectedItem() != null) {

                ClientThread ct = (ClientThread) clientArrayList.get(clientDropDown.getSelectedIndex());
                controller.killClient(ct);
                removeFromClient(ct);
            }

        }

    }

}
