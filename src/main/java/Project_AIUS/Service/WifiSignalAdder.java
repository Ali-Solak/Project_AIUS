package  Project_AIUS.Service;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WifiSignalAdder {

    private Image image0;
    private Image image1;
    private ScheduledExecutorService scheduledExecutorService;


    public WifiSignalAdder() {
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
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected");
        } catch (IOException e) {
            System.out.println("Internet is not connected");
        }
        return false;
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
