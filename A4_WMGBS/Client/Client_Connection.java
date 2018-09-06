package Client;

import Common.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client_Connection {

    private int portNum;
    private static final String hostName = "localhost";
    private InetAddress ina;
    public Socket clientSocket;
    private DataOutputStream streamToServer = null;
    private DataInputStream streamFromServer = null;
    private boolean shouldRun = true;

    public Client_Connection(int portNum) {
        this.portNum = portNum;
        setupConnection();
    }// end of constructor

    private void setupConnection(){
        // setup InetAddress
        try{
            ina = InetAddress.getByName(hostName);
        } catch (UnknownHostException e){
            System.out.println("Cannot find host name:" + hostName);
            System.exit(0);
        }

        // connect via clientSocket
        try{
            clientSocket = new Socket(ina, portNum);
            System.out.println("Connected to " + hostName);
        } catch (IOException e){
            System.out.println("Failed to connect to host");
            System.exit(1);
        }

        // setup DataOutputStream and DataInputStream
        try{
            streamToServer = new DataOutputStream(clientSocket.getOutputStream());
            streamFromServer = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e){
            System.out.println("Failed to get socket streams");
            System.exit(1);
        }
    }



    public void sendMessage (String message){
        try {
            streamToServer.writeUTF(message);
            streamToServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            end();
        }
    }

    public void listenToServer() throws InterruptedException, IOException {
        while(shouldRun){
            while(streamFromServer.available() == 0){
                Thread.sleep(1);
            }

            try{
                String dataIn = streamFromServer.readUTF();

                if(!dataIn.equals("STOP")){
                    if(!dataIn.equals("0") && !dataIn.equals("1") && !dataIn.equals("2")){
                        System.out.println(dataIn);
                    }
                }

            } catch (IOException e){
                e.printStackTrace();
                break;
            }
        }// end of while loop

        end();
        terminateConnection();
    }// end of listenToServer

    public void end(){
        try{
            streamFromServer.close();
            streamToServer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void terminateConnection(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}// end of class Client_Connection
