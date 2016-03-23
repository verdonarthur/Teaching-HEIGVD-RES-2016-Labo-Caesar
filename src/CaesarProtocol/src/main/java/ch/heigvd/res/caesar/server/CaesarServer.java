package ch.heigvd.res.caesar.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti (olivier.liechti@heig-vd.ch)
 */
public class CaesarServer {

  private static final Logger LOG = Logger.getLogger(CaesarServer.class.getName());

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tH:%1$tM:%1$tS::%1$tL] Server > %5$s%n");
    LOG.info("Caesar server starting...");
    //LOG.info("Protocol constant: " + Protocol.A_CONSTANT_SHARED_BY_CLIENT_AND_SERVER);
    
    ServerSocket socketserver = null;
        Socket socketDuClient = null;

        try {
            socketserver = new ServerSocket(3006);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socketDuClient = socketserver.accept();

                new CaesarServerThread(socketDuClient).start();
                
                socketDuClient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
  }
  
}
