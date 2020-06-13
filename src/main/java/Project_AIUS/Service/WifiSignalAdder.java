package  Project_AIUS.Service;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Takes an ImageView and puts accordingly pictures in place that fit
 * the status of the internet connection, utilizing a ScheduledExecuter service
 * to check the connection every ten seconds
 */
public class WifiSignalAdder {

    private Image image0;
    private Image image1;
    private ScheduledExecutorService scheduledExecutorService;


    public WifiSignalAdder() {
        //Allow renegotiation to socket in case of lost connection to prevent crash
        System.setProperty( "sun.security.ssl.allowUnsafeRenegotiation", "true" );

        image0 = new Image(getClass().getResource("/images/noInternet.jpg").toExternalForm());
        image1 = new Image(getClass().getResource("/images/wifi4.jpg").toExternalForm());

    }

    public void ajustData(ImageView imageView) {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Update
            Platform.runLater(() -> {
                
                if (checkInternetConnection()) {
                    imageView.setImage(image1);
                }
                else {
                    imageView.setImage(image0);
                }
            });
        }, 0, 10, TimeUnit.SECONDS);
    }

    public boolean checkInternetConnection() {
        try {
            InetAddress[] addresses = InetAddress.getAllByName("www.google.com");
            for (InetAddress address : addresses) {
                if (address.isReachable(5000))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected1");
            return false;
        } catch (IOException e) {
            System.out.println("Internet is not connected2");
            return false;
        }
        catch(Exception e){
            System.out.println("other connection failure");
            return false;
        }
        return false;
    }



    public void lookingForSignal(ProgressBar progressBar, Label label) {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Update
            Platform.runLater(() -> {

                if (checkInternetConnection()) {
                    progressBar.setVisible(false);
                    label.setVisible(false);
                }
                else {
                    progressBar.setVisible(true);
                    label.setVisible(true);
                }
            });
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void closeThread(ScheduledExecutorService executorService){
        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
}
