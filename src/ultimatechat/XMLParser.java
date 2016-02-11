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
import org.xml.sax.SAXParseException;

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
        System.out.println("Inne i requesPars:"+inText);
        InputSource is = new InputSource(new StringReader(inText));
        return DBuilder.parse(is).getDocumentElement().getTextContent();
    }
    
    public String sendrequestToXML(String inText, String inName){
        return "<message sender=\""+rewriteTags(inName)+"\">"+"<request>"+rewriteTags(inText)+"</request></message>";
    }
    
    public String sendText(String inText, String inName, Color incolor){
        System.out.println("<message sender=\""+rewriteTags(inName)+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB()).substring(2)+"\">"+rewriteTags(inText)+
                "</text></message>");
        return "<message sender=\""+rewriteTags(inName)+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB()).substring(2)+"\">"+rewriteTags(inText)+
                "</text></message>";
    }
    
    public ArrayList unParseXML(String inXML) throws Exception{
        System.out.println("asd: "+inXML);
        
        ArrayList<String> inFormation = new ArrayList<>();
        
        //try {
            if(!inXML.equals("null")){
                System.out.println(inXML.equals("null"));
                System.out.println("asd2: "+inXML);
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
                boolean disconnect = false;
                for(int i =0; i<nList2.getLength();i++){
                    Node n2 = nList2.item(i);
                   if (n2.getNodeType() == Node.ELEMENT_NODE) {
                   Element e = (Element) n2;
                    //Element e = (Element) n2;
                    if(!e.getTagName().equals("fetstil") && !e.getTagName().equals("kursiv")){
                        if(e.getTagName().equals("disconnect")){
                            disconnect = true;
                            System.out.print("diss true");
                        }
                        System.out.println(e.getTagName());
                        nNode.removeChild(n2);
                    }

                }
                }
    //            eElement.getElementsByTagName("fetstil").item(0).getTextContent();
                System.out.println(eElement.getAttribute("color"));
                inFormation.add(eElement.getAttribute("color"));

                inFormation.add(unrewriteTags(nNode.getTextContent()));
                if(disconnect){
                    inFormation.add("1");
                    System.out.print("diss true 2");
                }

            //inFormation.add(DBuilder.parse(is).getElementById("message").getAttribute("sender"));
            //inFormation.add(DBuilder.parse(is).getElementById("text").getAttribute("color"));
            //inFormation.add(DBuilder.parse(is).getElementById("message").getElementsByTagName("text").item(0).getTextContent());
            }
       // } catch (Exception ex) {
           //Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
           System.out.println(invalidXML(inFormation));
           //return invalidXML(inFormation);
        //}
        
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
    
    public String disconnectXML(String inName, Color incolor){
        return "<message sender=\""+rewriteTags(inName)+"\">"+"<text color=\"#"+
                Integer.toHexString(incolor.getRGB()).substring(2)+"\">"+"<disconnect/>"+
                "</text></message>";
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
