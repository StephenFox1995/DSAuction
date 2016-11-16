
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Main {
  private static InetAddress host;


  private static String commandLineArgOptions = "Options:\n" +
          "\tserver <port>: Run program as server.\n" +
          "\tclient <port>: Run program as client.";

  public static void main(String[] args) {
    int portNumber = 0;
    if (args.length != 2) {
      System.out.println("Invalid command line arguments specified.");
      System.out.println(commandLineArgOptions);
      System.exit(1);
    }
    else {
      try {
        portNumber = Integer.parseInt(args[1]);
        if (portNumber < 0 && portNumber > 65536) {
          System.out.println("Invalid port number");
          System.exit(1);
        }
      } catch (NumberFormatException e) {
        System.out.println("<port> argument must be a number.");
        System.exit(1);
      }
    }

    if (args[0].equals("server")) {
      Server server = Server.sharedServer();
      server.beginListening(portNumber);
    }
    else if (args[0].equals("client")) {
      Client client = new Client();
      try {
        host = InetAddress.getLocalHost();
        client.connectToServer(host, portNumber);
      } catch (UnknownHostException e) {
        System.out.print("Could not connect to local host.");
        System.exit(1);
      }
    } else {
      System.out.println("No valid command line arguments specified.");
      System.out.println(commandLineArgOptions);
    }
  }
}
