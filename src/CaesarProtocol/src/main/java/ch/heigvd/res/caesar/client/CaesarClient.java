package ch.heigvd.res.caesar.client;

import ch.heigvd.res.caesar.protocol.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Olivier Liechti (olivier.liechti@heig-vd.ch)
 */
public class CaesarClient {

    private static final Logger LOG = Logger.getLogger(CaesarClient.class.getName());
    private Socket ClientSocket;
    private Protocol p;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tH:%1$tM:%1$tS::%1$tL] Client > %5$s%n");
        LOG.info("Caesar client starting...");
        //LOG.info("Protocol constant: " + Protocol.A_CONSTANT_SHARED_BY_CLIENT_AND_SERVER);

        CaesarClient c = new CaesarClient("127.0.0.1", 3006);
        c.makeTest();
    }

    public CaesarClient(String adrServer, int portServer) {
        p = new Protocol();

        try {
            ClientSocket = new Socket(adrServer, portServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream(), "UTF-8"));
            PrintWriter os = new PrintWriter(new OutputStreamWriter(ClientSocket.getOutputStream(), "UTF-8"));
            String buffer = new String();

            os.println(p.getKey());       // send a byte to the server
            os.flush();

            buffer = is.readLine();
            int b = Integer.valueOf(buffer);     // read a byte sent by the server

            if (b != 100) {
                throw new IOException("Server refused connection");
            } else {
                LOG.info("server accepted connection");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) throws IOException {

        PrintWriter os = new PrintWriter(new OutputStreamWriter(ClientSocket.getOutputStream(), "UTF-8"));
        os.println(msg);
        os.flush();
    }

    private void makeTest() {
        String strArray[] = {"Message un", "Méssage 2", "M&/().,- 3"};

        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream(), "UTF-8"));
            String strAnswer;

            for (String s : strArray) {
                LOG.info("[INFO] sending encrypted message: " + s + "(" + p.encryptMessage(s) + ")");
                sendMessage(p.encryptMessage(s));
                strAnswer = is.readLine();
                LOG.info("[INFO] Server replied           : " + p.decryptMessage(strAnswer, p.getKey()) + "(" + strAnswer + ")");
            }
            pressAnyKeyToContinue();
            ClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pressAnyKeyToContinue() {
        System.out.println("Press any key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }
}
