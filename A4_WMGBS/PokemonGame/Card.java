package PokemonGame;

public class Card {
    public String name;
    public int PE;
    public int FE;
    public int KE;

    public void display(){
        System.out.printf("%-10s%3d%3d%3d\n"
                ,name , PE , FE , KE);
    }

    public String toString(){
        String cardStr = "";
        cardStr += name + "\t" + PE + "  " + FE + "  " + KE;

        return cardStr;
    }
}
