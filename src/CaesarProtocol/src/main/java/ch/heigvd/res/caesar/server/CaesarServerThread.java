/*
 * HEIG-VD / MCR 
 * Ioannis Noukakis && Djomo Patrick Deslé
 * Laboratoire N°01
 * File : CaesarServerThread.java
 */
package ch.heigvd.res.caesar.server;

import ch.heigvd.res.caesar.protocol.Protocol;
import static ch.heigvd.res.caesar.protocol.Protocol.NUMBER_OF_LETTER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author durza9390
 */
public class CaesarServerThread extends Thread {

    private Socket sock;
    private PrintWriter pw;
    private static int c = 0;
    private int numberClient;
    private Protocol p;

    private BufferedReader in;

    public CaesarServerThread(Socket sock) {
        this.sock = sock;
        p = new Protocol();
    }

    @Override
    public void run() {
        System.out.println("[INFO][" + getCurrentTimeStamp() + "] Client n°" + String.valueOf(++c) + " connected!");
        numberClient = c;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream() ,StandardCharsets.UTF_8));
            pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8));
            
            String buffer = new String();
            
            buffer = in.readLine();
            int key = Integer.valueOf(buffer);
            
            System.out.println("[SECURITY][" + getCurrentTimeStamp() + "] Key: " + key);
            if(key == -1)
            {
                System.out.println("[ERROR] end of stream reached. Aborting...");
                return;
            }
            
            if(key > NUMBER_OF_LETTER || key < 0)
                pw.println(0);
            else
                pw.println(100);
            
            pw.flush();
            
            while (true) {
                buffer = in.readLine();

                if (buffer == null) {
                    break;
                }

                if (!buffer.equals("")) {
                    this.productionProcess(buffer, key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[INFO][" + getCurrentTimeStamp() + "] Client " + numberClient + " disconected");
    }

    private void productionProcess(String command, int keyClient) throws IOException {
        System.out.println("[INFO][" + getCurrentTimeStamp() + "] encripted message recieved: " + command);
        command = p.decryptMessage(command, keyClient);
        System.out.println("[INFO][" + getCurrentTimeStamp() + "] decripted message         : " + command);
        command = p.encryptMessage(command, keyClient);
        pw.println(command);
        pw.flush();
        System.out.println("[INFO][" + getCurrentTimeStamp() + "] reply sent");
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
