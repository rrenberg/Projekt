/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author jakobarnoldsson
 */
public class XMLParser {
    
    private DocumentBuilderFactory DBFactory;
    private DocumentBuilder DBuilder;
  
    public XMLParser(){
        DBFactory = DocumentBuilderFactory.newInstance();
        
        try {
            DBuilder = DBFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String requestParser (String inText) throws Exception{
        InputSource is = new InputSource(new StringReader(inText));
        return DBuilder.parse(is).getDocumentElement().getTextContent();
    }
    
    public String sendrequestToXML(String inText){
        return "<message><request>"+rewriteTags(inText)+"</request></message>";
    }
    
    public String sendText(String inText, String inName, Color incolor){
        System.out.println("<message sender=\""+rewriteTags(inName)+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB()).substring(2)+"\">"+rewriteTags(inText)+
                "</text></message>");
        return "<message sender=\""+rewriteTags(inName)+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB()).substring(2)+"\">"+rewriteTags(inText)+
                "</text></message>";
    }
    
    public ArrayList unParseXML(String inXML){
        
        ArrayList inFormation = new ArrayList<>();
        
        try {
            Document doc = DBuilder.parse(new InputSource(new StringReader(inXML)));
            
           // doc.setTextContent(unrewriteTags(doc.getTextContent()));
            inFormation.add(doc.getDocumentElement().getAttribute("sender"));
            NodeList nList = doc.getElementsByTagName("text");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            System.out.println(eElement.getAttribute("color"));
            inFormation.add(eElement.getAttribute("color"));

            inFormation.add(unrewriteTags(DBuilder.parse(new InputSource(new StringReader(inXML))).getElementsByTagName("text").item(0).getTextContent()));
            //inFormation.add(DBuilder.parse(is).getElementById("message").getAttribute("sender"));
            //inFormation.add(DBuilder.parse(is).getElementById("text").getAttribute("color"));
            //inFormation.add(DBuilder.parse(is).getElementById("message").getElementsByTagName("text").item(0).getTextContent());
        
        } catch (SAXException ex) {
           Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return inFormation;
    }
    public String rewriteTags(String inText){
        String newString = inText.replace(">", "&gt;");
        return newString.replace("<", "&lt;");
    }
    public String unrewriteTags(String inText){
        
        String newString = inText.replace("&gt;", ">");
        return newString.replace("&lt;", ">");
    }
}
