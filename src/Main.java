
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Main {
  private static InetAddress host;
  private static int PORT_NUMBER = 8080;
  private static boolean runAsServer = false;

  private static String commandLineArgOptions = "Options:\n" +
          "\tserver: Run program as server.\n" +
          "\tclient: Run program as client.";

  public static void main(String[] args) {
    if (args.length <= 0) {
      System.out.println("No command line arguments specified.");
      System.out.println(commandLineArgOptions);
    }
    else if (args[0].equals("server") ) {
      Server server = Server.sharedServer();
      server.beginListening(PORT_NUMBER);
    }
    else if (args[0].equals("client")) {
      Client client = new Client();
      try {
        host = InetAddress.getLocalHost();
        client.connectToServer(host, PORT_NUMBER);
      } catch (UnknownHostException e) {
        System.out.print("Could not get local host.");
        System.exit(1);
      }
    } else {
      System.out.println("No valid command line arguments specified.");
      System.out.println(commandLineArgOptions);
    }
  }
}
