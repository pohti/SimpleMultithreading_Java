package PokemonGame;

import java.util.ArrayList;

public class Player{

    public String name;
    public String password;
    public int choice;
    public ArrayList<Card> deck = new ArrayList<Card>();

    // TODO: reimplement the method
    public void display(){
        //Interface.partition();
        Pokemon_Server.println("Player name : " + name);
        //Pokemon_Server.println("Password : " + password);
        Pokemon_Server.println("");
        System.out.printf("%-13s%3s%3s%3s\n"
                ,"CardName","PE",
                "FE","KE");
        Pokemon_Server.println("------------------------");
        int i=1;
        for(Card c : deck){
            Pokemon_Server.print(i + ". ");
            c.display();
            i++;
        }
        Pokemon_Server.partition();
    }
    public String toString(){
        String playerStr = "";
        playerStr += "==================================\n";
        playerStr += "Player name: " + name + "\n\n";
        playerStr += "Card Name \tPE\tFE\tKE\n";
        int i=1;
        for(Card c: deck){
            playerStr += i + ". " + c.toString() + "\n";
            i++;
        }
        return playerStr;
    }
}
