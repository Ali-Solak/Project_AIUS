package Project_AIUS.Service;

import Project_AIUS.Exception.NetworkNotFoundException;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Uses oshi Core Libary to get data from the network adapter and read the internet speed.
 */
public class InternetSpeedData {

    private SystemInfo si;
    private HardwareAbstractionLayer hal;
    private NetworkIF net;
    private NetworkIF[] networks;
    private URL url;

    public InternetSpeedData() {
        this.si = new SystemInfo();
        this.hal = si.getHardware();
        this.networks = hal.getNetworkIFs().toArray(new NetworkIF[0]);
        try {
            setWorkingNetworkAdapter();
        }
        catch (NetworkNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            this.url = new URL("http://speedtest.ftp.otenet.gr/files/test1Mb.db");

        } catch (java.net.MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Downloads example file and calculates download speed by looking at the received bytes
     * on the Network adapter
     * @param net
     * @return
     */
    public long readInternetSpeed(NetworkIF net) {

        long download1 = net.getBytesRecv();
        long timestamp1 = net.getTimeStamp();
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("downExample.txt");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Updating network stats
        net.updateAttributes();

        long download2 = net.getBytesRecv();
        long timestamp2 = net.getTimeStamp();

        return (download2 - download1) / (timestamp2 - timestamp1);
    }

    /**
     * Checks all Network Adapters chooses the one with internet connection
     * @Exception NetworkAdapterNotFound
     */
    public void setWorkingNetworkAdapter() throws NetworkNotFoundException{

        for (NetworkIF network : networks) {
            if (network.getSpeed()>0) {
                this.net = network;
            }
            else {
                if(networks[1] == null){
                    throw new NetworkNotFoundException();
                }
                this.net = networks[1];
            }
        }
    }

    public NetworkIF getNet() {
        return net;
    }
}
