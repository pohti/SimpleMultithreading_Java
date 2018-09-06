package PokemonGame;

import Common.Utility;
import Server.Server_PlayerThread;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Pokemon_Server {
    Server_PlayerThread playerThread;

    // fixed variables
    private final String ASH_PW = "password1";
    private final String MISTY_PW = "password2";
    private final String BROCK_PW = "password3";

    private final String FILE_PATH = "./dataFiles/";
    private final String PLAYERS_FILE = FILE_PATH + "Players.dat";

    // variables
    private Player player = new Player();
    private Player computer = new Player();
    private Card playerCard;
    private Card computerCard;
    double playerEL, computerEL;

    // connection related variables
    private DataOutputStream serverToClient = null;
    private DataInputStream clientToServer = null;
    private boolean shouldRun = true;

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////

    public Pokemon_Server(Server_PlayerThread playerThread){
        this.playerThread = playerThread;
        // acquire input and output streams
            serverToClient = new DataOutputStream(playerThread.getOutputStream());
            clientToServer = new DataInputStream(playerThread.getInputStream());
    }// end of constructor


    /*
    *   TODO: a series of questions and answers
    *   TODO: broadcast the exchange back to the viewers
    */


    public void run(){
        initializePlayers();
        logInScreen();
    }// end of run()
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // game related methods
    private void logInScreen(){
        player.name = receiveMsg();
        player.password = receiveMsg();
        // verify password
        if(verifyPW()){
            println("\nLog in successful!");
            partition();
            sendMsg("YES");
            gameOn();
        } else {
            println("\nPlayer name and password mismatch");
            partition();
            sendMsg("NO");
            System.exit(1);
        }
        // redirect to appropriate player
    }// end of logInScreen()
    private void gameOn(){
        // TODO: broadcast that the game has begun
        // TODO:
        // load player cards
        player.deck = loadCards(player.name);

        // getting computer name
        computer.name = receiveMsg();
        computer.deck = loadCards(computer.name);

        partition();
        sendMsg("The game has begun!");
        gameRound();
    }//end of gameOn()
    private void gameRound(){
        // play until the player quits
        int round = 1;
        while(true){
            // VARIABLES
            Random rand = new Random();
            playerEL = computerEL = 0;
            playerCard = new Card();
            computerCard = new Card();

            // *****************************************************
            // PLAYER's turn
            // display player cards
            playerTurn(round);
            // *****************************************************
            // computer's turn
            computer.choice = rand.nextInt(computer.deck.size());
            computerCard = computer.deck.get(computer.choice);
            sendMsg(computer.name + " chose " + computerCard.name);
            println(computer.name + " chose " + computerCard.name );

            sendMsg(computerCard.toString());
            println(computerCard.toString());
            // *****************************************************
            displayMultipliers();

            sendMsg("STOP");
            // *****************************************************
            // Decide winner
            // allow winner to upgrade
            // update the card
            // update the deck
            finalStage();
            // keep playing?
            String nextWar = receiveMsg();
            if(nextWar.toLowerCase().equals("n")){

                break;
            }

            round++;
        }//end of while loop
        broadcast("Player has left the game. Game over");
        System.out.println("Player has left the game");

    }//end of gameRound()
    private void playerTurn(int round){
        sendMsg("Round " + round + ": " + player.name + "'s turn");
        System.out.println("Round " + round + ": " + player.name + "'s turn");

        sendMsg(player.toString());
        System.out.println(player.toString());
        // let the player choose the card
        player.choice = Integer.parseInt(receiveMsg())-1;
        playerCard = player.deck.get(player.choice);
        println(player.name + "chose " + playerCard.name);
        sendMsg(player.name + " chose " + playerCard.name);
    }
    private void displayMultipliers(){
        Random rand = new Random();
        // let Random number decide which weapon to be used
        int weaponChoice = rand.nextInt(3);
        // both players follow the choice
        // generate random Energy Multiplier
        DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
        double playerMultiplier = 0 + 1*rand.nextDouble();
        double computerMultiplier = 0 + 1*rand.nextDouble();
        playerMultiplier = Double.valueOf(oneDigit.format(playerMultiplier));
        computerMultiplier = Double.valueOf(oneDigit.format(computerMultiplier));
        // *****************************************************
        // Display calculated results
        print("Weapon to be used : ");
        switch(weaponChoice){
            case 0 :
                println("Punch");
                sendMsg("Weapon to be used : Punch");
                break;
            case 1 :
                println("Fireball") ;
                sendMsg("Weapon to be used : Fireball");
                break;
            case 2 :
                println("Kick") ;
                sendMsg("Weapon to be used : Fireball");
                break;
        }
        println("-------------------------------");
        sendMsg("-------------------------------");

        println(player.name + " energy multiplier => " + playerMultiplier);
        sendMsg(player.name + " energy multiplier => " + playerMultiplier);
        print(player.name);
        switch(weaponChoice){
            case 0 :
                playerEL = playerCard.PE * playerMultiplier;
                println(" Punch energy level : " + playerEL) ;
                sendMsg(player.name + " Punch energy level : " + playerEL);
                break;
            case 1 :
                playerEL = playerCard.FE * playerMultiplier;
                println(" Fireball energy level : " + playerEL) ;
                sendMsg(player.name + " Fireball energy level : " + playerEL);
                break;
            case 2 :
                playerEL = playerCard.KE * playerMultiplier;
                println(" Kick energy level : " + playerEL) ;
                sendMsg(player.name + " Kick energy level : " + playerEL);
                break;
        }
        println("\n" + computer.name + " energy multiplier => " + computerMultiplier);
        sendMsg(computer.name + " energy multiplier => " + computerMultiplier);
        print(computer.name);
        switch(weaponChoice){
            case 0 :
                computerEL = computerCard.PE * computerMultiplier;
                println(" Punch energy level : " + computerEL) ;
                sendMsg(computer.name + " Punch energy level : " + computerEL);
                break;
            case 1 :
                computerEL = computerCard.FE * computerMultiplier;
                println(" Fireball energy level : " + computerEL) ;
                sendMsg(computer.name + " Kick energy level : " + computerEL);
                break;
            case 2 :
                computerEL = computerCard.KE * computerMultiplier;
                println(" Kick energy level : " + computerEL) ;
                sendMsg(computer.name + " Kick energy level : " + computerEL);
                break;
        }
    }//end of displayMultipliers
    private void finalStage(){
        // player wins
        if(playerEL > computerEL){
            println("\n" + player.name  + " wins 5 energy points!");
            sendMsg("\n" + player.name  + " wins 5 energy points!");
            sendMsg("0");
            sendMsg(player.toString());
            partition();
            // choose card
            player.choice = Integer.parseInt(receiveMsg()) - 1;
            playerCard = player.deck.get(player.choice);
            // choose PE,KE or FE
            char upgradeChoice = receiveMsg().charAt(0);

            switch(upgradeChoice){
                case 'P' : playerCard.PE+=5 ; break;
                case 'F' : playerCard.FE+=5 ; break;
                case 'K' : playerCard.KE+=5 ; break;
            }
            updateDeck(player, playerCard);
            player.display();
            sendMsg(player.toString());
        }
        // computer wins
        else if (playerEL == computerEL){
            println("Draw");
            sendMsg("Draw");
            sendMsg("1");
        }
        // draws
        else{
            println(computer.name + " wins!");
            sendMsg(computer.name + " wins!");
            sendMsg("2");
        }
    }
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // file IO related methods
    private void initializePlayers(){
        // write player info and passwords into "Players.dat"
        try{
            PrintWriter outFile = new PrintWriter (PLAYERS_FILE);
            outFile.println("Ash," + Utility.getHash(ASH_PW));
            outFile.println("Misty," + Utility.getHash(MISTY_PW));
            outFile.println("Brock," + Utility.getHash(BROCK_PW));

            outFile.close();
        }
        catch(IOException e){
            System.out.println("File named" + PLAYERS_FILE + " not found");
        }
    }// end of initializePlayers()
    // to load the cards of given player
    private ArrayList<Card> loadCards(String name){
        ArrayList<Card> tempCards = new ArrayList<Card>();

        try{
            String fileName = FILE_PATH + name + ".dat";
            Scanner inFile = new Scanner(new File(fileName));

            // read line by line
            while(inFile.hasNextLine()){
                Card tempCard = new Card();

                // disect the line
                String line = inFile.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                tempCard.name = lineScanner.next();
                tempCard.PE = lineScanner.nextInt();
                tempCard.FE = lineScanner.nextInt();
                tempCard.KE = lineScanner.nextInt();

                // add the tempCard to the deck
                tempCards.add(tempCard);
            }

            inFile.close();
        }
        catch(FileNotFoundException e){
            println("Failed to load cards for " + name);
        }

        return tempCards;
    }// end of loadCards()
    private boolean verifyPW(){
        // read line by line
        // check whether if both nickname and pw match or not
        boolean foundPlayer = false;
        try{
            Scanner inFile = new Scanner (new File(PLAYERS_FILE));

            // check line by line
            while(inFile.hasNextLine()){
                // disect the line
                String line = inFile.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                if(player.name.equals(lineScanner.next())){
                    if(player.password.equals(lineScanner.next())){
                        foundPlayer = true;
                        return foundPlayer;
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            println("ERROR : File named " + PLAYERS_FILE + " not found!");
            System.exit(1);
        }

        return foundPlayer;
    }// end of verifyPW()
    private void updateDeck(Player tempPlayer, Card newCard){
        // find the same card from the deck
        for(Card tempC : tempPlayer.deck){
            // replace the card
            if(tempC.name.equals(newCard.name)){
                tempC = newCard;
            }
        }

        // update the deck inside the dat file
        try{
            String fileName = FILE_PATH + tempPlayer.name + ".dat" ;
            PrintWriter outFile = new PrintWriter (fileName);

            for(Card tempCard : tempPlayer.deck){
                outFile.print(tempCard.name + ",");
                outFile.print(tempCard.PE + ",");
                outFile.print(tempCard.FE + ",");
                outFile.println(tempCard.KE);
            }
            outFile.close();
        }
        catch(IOException e){
            println("File not found");
        }
    }
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // communication related methods
    private void sendMsg(String line){
        broadcast(line);
        try {
            serverToClient.writeUTF(line);
            serverToClient.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message to client");
            e.printStackTrace();
        }
    }
    private String receiveMsg(){
        String message = "";
        try {
            message = clientToServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
    private String receiveMsg(String message){
        sendMsg(message);
        return receiveMsg();
    }
    private void broadcast(String message){
        playerThread.broadcast(message);
    }
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // utility
    public static void println(String line){
        System.out.println(line);
    }
    public static void print(String line){
        System.out.print(line);
    }
    static public void partition(){
        Utility.printDoubleLine(35);
    }

}
