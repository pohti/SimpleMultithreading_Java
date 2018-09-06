package PokemonGame;

import Client.Client_Player;
import Common.Keyboard;
import Common.Utility;

import java.io.*;

public class Pokemon_Client {
    Client_Player player;
    private DataInputStream serverToClient = null;
    private DataOutputStream clientToServer = null;

    public Pokemon_Client(Client_Player player){
        this.player = player;
        serverToClient = new DataInputStream(player.getInputStream());
        clientToServer = new DataOutputStream(player.getOutputStream());
    }

    public void run(){
        logIn();
    }
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // game related methods
    private void logIn(){
        String playerName, password;
        playerName = Keyboard.readString("\nEnter nickname : ");
        password = Utility.getHash(Keyboard.readString("Enter password : "));

        sendMsg(playerName);
        sendMsg(password);

        // wait for server authentication
        String result = receiveMsg();
        if(result.equals("YES")){
            gameOn();
        } else if (result.equals("NO")){
            System.out.println("Incorrect username or password");
            System.exit(1);
        }
    }
    private void gameOn(){
        String computerName = Keyboard.readString("Enter opponent nick: ");
        sendMsg(computerName);

        System.out.println(receiveMsg()+ "\n"); // log in successful
        gameRound();
    }
    private void gameRound(){
        int round = 1;
        while(true){
            // display player cards
            System.out.println(receiveMsg()); // round x ... turn
            System.out.println(receiveMsg()); // player info
            // let the player choose the card
            String cardChoice = Keyboard.readString("Enter card choice: ");
            sendMsg(cardChoice);
            // computer's turn
            // listen to game report
            listenToServer();
            // final stage
            finalStage();
            // keep playing?
            System.out.println("---------------------------------");
            String nextWar = Keyboard.readString("Next War (Y/N) : ");
            sendMsg(nextWar);
            if(nextWar.toLowerCase().equals("n")) break;
            round++;
        }
    }
    private void finalStage(){
        System.out.println(receiveMsg()); // xxx wins
        // act accordingly
        int result = Integer.parseInt(receiveMsg());
        switch(result){
            case 0: // player wins
                System.out.println(receiveMsg() + "\n"); // player info
                String monsterChoice = Keyboard.readString("Choose a monster to upgrade: ");
                sendMsg(monsterChoice);
                String upgradeChoice = Keyboard.readString("Choose a weapon to upgrade (P/F/K): ");
                sendMsg(upgradeChoice);
                System.out.println(receiveMsg()); // show new player info
                break;
            case 1: // computer wins
                break;
            case 2: // draw
                break;
        }
    }

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // communication related methods
    private void sendMsg(String message){
        player.sendMsg(message);
    }
    private String receiveMsg(){
        String message = null;
        try{
            message = serverToClient.readUTF();
        } catch (IOException e){
            e.printStackTrace();
        }
        return message;
    }
    private String receiveMsg(String message){
        sendMsg(message);
        return receiveMsg();
    }
    private void listenToServer(){
        while(true){
            String message = receiveMsg();
            if(!message.equals("STOP")) {
                System.out.println(message);
            } else break;
        }
    }
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // utility
}
