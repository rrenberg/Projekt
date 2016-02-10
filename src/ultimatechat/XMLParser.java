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
        
        ArrayList<String> inFormation = new ArrayList<>();
        
        try {
            Document doc = DBuilder.parse(new InputSource(new StringReader(inXML)));
            
           // doc.setTextContent(unrewriteTags(doc.getTextContent()));
            //doc.getDocumentElement().normalize();
            inFormation.add(((Element) (doc.getElementsByTagName("message").item(0))).getAttribute("sender"));
            if(inFormation.get(0).length()==0){
                throw new NullPointerException();
            }
            NodeList nList = doc.getElementsByTagName("text");
            Node nNode = nList.item(0);
            NodeList n = nNode.getChildNodes();
            
            Element eElement = (Element) nNode;
            String message = eElement.getTextContent();
            NodeList nList2 = nNode.getChildNodes();
            System.out.println(nNode.getTextContent());
            for(int i =0; i<nList2.getLength();i++){
                Node n2 = nList2.item(i);
               if (n2.getNodeType() == Node.ELEMENT_NODE) {
               Element e = (Element) n2;
                //Element e = (Element) n2;
                if(!e.getTagName().equals("fetstil") && !e.getTagName().equals("kursiv")){
                    System.out.println(e.getTagName());
                    nNode.removeChild(n2);
                }
                
            }
            }
//            eElement.getElementsByTagName("fetstil").item(0).getTextContent();
            System.out.println(eElement.getAttribute("color"));
            inFormation.add(eElement.getAttribute("color"));

            inFormation.add(unrewriteTags(nNode.getTextContent()));
            //inFormation.add(DBuilder.parse(is).getElementById("message").getAttribute("sender"));
            //inFormation.add(DBuilder.parse(is).getElementById("text").getAttribute("color"));
            //inFormation.add(DBuilder.parse(is).getElementById("message").getElementsByTagName("text").item(0).getTextContent());
        
        } catch (SAXException ex) {
           Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
           
           return invalidXML(inFormation);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return invalidXML(inFormation);
        } catch (NullPointerException exp){
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, exp);
            return invalidXML(inFormation);
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
    
    private ArrayList<String> invalidXML(ArrayList<String> inFormation){
        ArrayList<String> errArray = new ArrayList<>();
           if(inFormation.size()!=0){
                errArray.add(inFormation.get(0));
           }else{
                   errArray.add("Okänd");
           }
           errArray.add((Integer.toHexString(Color.RED.getRGB()).substring(2)));
           errArray.add("Kunde inte parsa XML");
           return errArray;
    }
    
}
