/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
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
        return "<message><request>"+inText+"</request></message>";
    }
    
    public String sendText(String inText, String inName, Color incolor){
        return "<message sender=\""+inName+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB())+"\">"+inText+
                "</text></message>";
    }
    
    public ArrayList unParseXML(String inXML){
        InputSource is = new InputSource(new StringReader(inXML));
        
        ArrayList inFormation = new ArrayList<>();
        try {
            DBuilder.parse(is).getElementById("message").getAttribute("sender");
            inFormation.add(DBuilder.parse(is).getElementById("message").getAttribute("sender"));
            inFormation.add(DBuilder.parse(is).getElementById("text").getAttribute("color"));
            inFormation.add(DBuilder.parse(is).getElementById("message").getElementsByTagName("text").item(0).getTextContent());
        
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return inFormation;
    }
    
}
