package Common;

import java.io.Serializable;

public class Message implements Serializable{

    private String control;
    private String associatedData;

    public Message(String c, String d){
        control = c;
        associatedData = d;
    }

    public String getControl(){
        return control;
    }

    public String getAssociatedData(){
        return associatedData;
    }

}
