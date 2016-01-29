/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.util.ArrayList;

/**
 *
 * @author The ZumBot 2.0
 */
public class UltimateChat implements Runnable {
    
    MainView mainView;
    ArrayList<ConversationController> conversationControllerList;
    int port;
    String name;
    
    public UltimateChat(){
        mainView = new MainView(this);
        System.out.println("hej");
        System.out.println("adasd");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UltimateChat();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
