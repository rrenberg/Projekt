/*
 * XMLParser
 *
 * Version 1.0
 *
 * 16-02-2016
 */
package ultimatechat;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *The class XMLParser provides a parser to pars recived messages and to messages 
 * that will be sent to clients.
 * 
 * @author Rasmus Renberg and Jakob Arnoldsson
 */
public class XMLParser {

    private DocumentBuilderFactory DBFactory;
    private DocumentBuilder DBuilder;

    /**
     *Constructor that creates a DocumentBuilderFactroy.
     */
    public XMLParser() {
        DBFactory = DocumentBuilderFactory.newInstance();

        try {
            DBuilder = DBFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *Function requestParser takes in XML with request tags and pars it.
     * 
     * @param inText The request message
     * @return String Returns a string of the parsed request message.
     * @throws Exception If the request message is wrong
     */
    public String requestParser(String inText) throws Exception {
        InputSource is = new InputSource(new StringReader(inText));
        return unrewriteTags(DBuilder.parse(is).getDocumentElement().getTextContent());
    }

    /**
     * Function checkIfRequest check if the recived message is a request message.
     *
     * @param inXML A string of the recived xml.
     * @return boolean Returns a boolean value (true if inXML have request tags).
     * @throws Exception
     */
    public boolean checkIfRequest(String inXML) throws Exception {
        
        //Takes the first message tag and get the children of it.
        Document doc = DBuilder.parse(new InputSource(new StringReader(inXML)));
        NodeList nList = doc.getElementsByTagName("message");
        Node nNode = nList.item(0);
        NodeList nList2 = nNode.getChildNodes();
        boolean request = false;
        
        //For every child, check if it is a request tag.
        for (int i = 0; i < nList2.getLength(); i++) {
            Node n2 = nList2.item(i);
            if (n2.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n2;
                if (e.getTagName().equals("request")) {
                    request = true;
                }
            }
        }

        return request;
    }

    /**
     * 
     * Function sendrequestToXML  creates request message in xml-format.
     *
     * @param inText Message that we send when we want to connect
     * @param inName Our name
     * @return String Returns the xml-string
     */
    public String sendrequestToXML(String inText, String inName) {
        return "<message sender=\"" + rewriteTags(inName) + "\">" + "<request>" 
                + rewriteTags(inText) + "</request></message>";
    }

    /**
     *
     * Function sendText creates a message in xml-format.
     * 
     * @param inText Our text we want to send
     * @param inName Our name
     * @param incolor Our color of our text
     * @return String Returns the message in xml-format
     */
    public String sendText(String inText, String inName, Color incolor) {
        return "<message sender=\"" + rewriteTags(inName) + "\">" + "<text color=\"#"
                + Integer.toHexString(incolor.getRGB()).substring(2) + "\">" + rewriteTags(inText)
                + "</text></message>";
    }

    /**
     * 
     * Function unParseXML pars our recived xml and throws exception if 
     * the xml was wrong.
     *
     * @param inXML The xml message we recived
     * @return inFormation Returns an Arraylist with parsed information from the recived message
     * @throws Exception
     */
    public synchronized ArrayList unParseXML(String inXML) throws Exception {
        
        System.out.println(inXML);
        System.out.println("Slut");

        ArrayList<String> inFormation = new ArrayList<>();

        //As long as inXML isnt null, try to pars
        if (!inXML.equals("null")) {
            
            Document doc = DBuilder.parse(new InputSource(new StringReader(inXML)));
            
            //Get name of the sender
            inFormation.add(((Element) (doc.getElementsByTagName("message").
                    item(0))).getAttribute("sender"));
            
            //If name null, throw exception
            if (inFormation.get(0).length() == 0) {
                throw new NullPointerException();
            }
            
            //Takes the first text tag, get textcontent and children to it.
            NodeList nList = doc.getElementsByTagName("text");
            Node nNode = nList.item(0);

            Element eElement = (Element) nNode;
            String message = eElement.getTextContent();
            NodeList nList2 = nNode.getChildNodes();
            boolean disconnect = false;
            
            //For every child of the text tag
            for (int i = 0; i < nList2.getLength(); i++) {
                Node n2 = nList2.item(i);
                if (n2.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n2;
                    
                    //Check if it have "fetstil","kursive" and "disconnect" tag.
                    if (!e.getTagName().equals("fetstil") && !e.getTagName().
                            equals("kursiv")) {
                        
                        if (e.getTagName().equals("disconnect")) {
                            disconnect = true;
                        }
                        nNode.removeChild(n2);
                    }

                }
            }
      
            //Get color of sender.
            inFormation.add(eElement.getAttribute("color"));
            
            //Get text message of sender
            inFormation.add(unrewriteTags(nNode.getTextContent()));
            
            //If disconnect, add 1
            if (disconnect) {
                inFormation.add("1");
            }
            
            //If inXML is a request reply and the reply is no, then disconnect
            if(checkIfRequest(inXML)){
                if(((Element) (doc.getElementsByTagName("request").
                        item(0))).getAttribute("reply").equals("no")){
                    
                    inFormation.add("2");
                }
            }

        }

        return inFormation;

    }

    /**
     * Function rewriteTags, rewrite tag signs and "&" to specified combination.
     *
     * @param inText Text that this signs should be rewriten.
     * @return newString Returns the rewriten string.
     */
    public String rewriteTags(String inText) {
        String newString = inText.replace(">", "&gt;").replace("<", "&lt;").
                replace("&", "&amp;");
        return newString;
    }

    /**
     * Function unrewriteTags does the opposite to what rewriteTags does.
     *
     * @param inText Text that should be unrewriten.
     * @return newString Returns the unrewriten string.
     */
    public String unrewriteTags(String inText) {
        String newString = inText.replace("&gt;", ">").replace("&lt;", "<").
                replace("&amp;", "");
        return newString;
    }

    /**
     * Function disconnectXML creates the xml-message for disconnection.
     *
     * @param inName Our name
     * @param incolor Our color
     * @return Returns the disconnect messages in xml-format
     */
    public String disconnectXML(String inName, Color incolor) {
        return "<message sender=\"" + rewriteTags(inName) + "\">" + "<text color=\"#"
                + Integer.toHexString(incolor.getRGB()).substring(2) + "\">" + "<disconnect/>"
                + "</text></message>";
    }
    
    //Function that sends reply no during connection request
    public String sendrequestNOToXML(String inText, String inName) {
        return "<message sender=\"" + rewriteTags(inName) + "\">" + 
                "<request reply=\"no\"><text color=\"#"
                + Integer.toHexString((Color.BLUE).getRGB()).substring(2) + 
                "\">" + rewriteTags(inText)
                + "</text></request></message>";
    }
}

