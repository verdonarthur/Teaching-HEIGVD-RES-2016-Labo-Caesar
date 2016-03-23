package ch.heigvd.res.caesar.protocol;

import java.util.Random;

/**
 *
 * @author Olivier Liechti
 */
public class Protocol {
  
  //public static final int A_CONSTANT_SHARED_BY_CLIENT_AND_SERVER = 42;
    public static final int NUMBER_OF_LETTER = 26;
  private int key;

    public Protocol() {
        Random rand = new Random();
        key = rand.nextInt(NUMBER_OF_LETTER);
    }

    public int getKey() {
        return key;
    }
    
    public String encryptMessage(String msg)
    {
        return encryptMessage(msg, this.key);
    }
    
    public String encryptMessage(String msg, int key)
    {
        String rtn = new String();
        char[] array = msg.toCharArray();
        
        for(char c : array)
        {
            rtn += (char)(c+key);
        }
        return rtn;
    }
    
    public String decryptMessage(String msg, int key)
    {
        String rtn = new String();
        char[] array = msg.toCharArray();
        
        for(char c : array)
        {
            rtn += (char)(c-key);
        }
        return rtn;
    }
}
