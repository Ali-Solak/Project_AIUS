package  Project_AIUS.Service;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    public InternetSpeedData(){
        this.si = new SystemInfo();
        this.hal = si.getHardware();
        this.networks = hal.getNetworkIFs().toArray(new NetworkIF[0]);
        this.net =  networks[8];

        try {
            this.url = new URL("http://speedtest.ftp.otenet.gr/files/test1Mb.db");
        }
        catch(java.net.MalformedURLException e){
            e.getMessage();
        }

    }

    public long readInternetSpeed(NetworkIF net) {

        long download1 = net.getBytesRecv();
        long timestamp1 = net.getTimeStamp();
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("downExample.txt");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch (Exception e){

        }

        //Updating network stats
        net.updateAttributes();

        long download2 = net.getBytesRecv();
        long timestamp2 = net.getTimeStamp();

        return  (download2 - download1)/(timestamp2 - timestamp1);
    }

    /**
     * Checks all Network Adapters
     */
    public void getWorkingNetworkAdapter(){

        for(NetworkIF network :networks){
            if(readInternetSpeed(network)>0){
                net = network;
            }
        }
    }

    public NetworkIF getNet() {
        return net;
    }
}
