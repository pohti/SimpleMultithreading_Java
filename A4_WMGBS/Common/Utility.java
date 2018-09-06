package Common;

import java.security.MessageDigest;

public class Utility {

    public static String getHash(String base)
    {
        String message="";

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            message = hexString.toString();

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return message;
    }

    public static void printLine(int num)
    {
        printLine(num,'-');
    }

    public static void printDoubleLine(int num)
    {
        printLine(num,'=');
    }

    public static void printLine(int num,char pattern)
    {
        for(int i =0;i<num;i++)
        {
            System.out.print(pattern);
        }
        System.out.println("");

    }

}