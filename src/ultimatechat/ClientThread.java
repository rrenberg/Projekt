/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author The ZumBot 2.0
 */
public class ClientThread {
    
    private DataInputStream DIStream;
    private DataOutputStream DOStream;
    
    
    public ClientThread(DataInputStream inPutStream, DataOutputStream outPutStream){
        DIStream = inPutStream;
        DOStream = outPutStream;
    }
}
