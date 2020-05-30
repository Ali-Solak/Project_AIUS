package Project_AIUS.Exception;

public class NetworkNotFoundException extends Exception {

    public NetworkNotFoundException() {
        super("Network Adapter Could not be found.");
    }
}
