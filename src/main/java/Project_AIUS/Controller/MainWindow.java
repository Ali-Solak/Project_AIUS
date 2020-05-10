package  Project_AIUS.Controller;



import  Project_AIUS.Service.WifiSignalAdder;
import  Project_AIUS.View.ViewFactory;
import com.jfoenix.controls.JFXButton;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainWindow extends BaseController implements Initializable {

    @FXML private JFXButton home;
    @FXML private JFXButton browser;
    @FXML private JFXButton message;
    @FXML private JFXButton satellite;
    @FXML private JFXButton settings;
    @FXML private ImageView image;
    @FXML private AnchorPane mainWindow;
    @FXML private Clock clock;
    @FXML private ImageView imageView;
    private WifiSignalAdder wifiSignalAdder;

    public MainWindow(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        wifiSignalAdder = new WifiSignalAdder();
        wifiSignalAdder.ajustData(imageView);

        clock = ClockBuilder.create()
                .skinType(Clock.ClockSkinType.SLIM)
                .textVisible(true)
                .dateVisible(true)
                .text("Bukalagi")
                .prefWidth(150)
                .prefHeight(150)
                .textColor(Color.rgb(168,104,160))
                .titleColor(Color.rgb(168,104,160))
                .hourTickMarkColor(Color.rgb(168,104,160))
                .secondColor(Color.rgb(168,104,160))
                .tickLabelColor(Color.rgb(168,104,160))
                .minuteTickMarkColor(Color.rgb(168,104,160))
                .dateColor(Color.rgb(168,104,160))
                .knobColor(Color.rgb(168,104,160))
                .minuteColor(Color.rgb(168,104,160))
                .hourColor(Color.rgb(168,104,160))
                .running(true)
                .build();

        clock.setLayoutX(1100);
        mainWindow.getChildren().addAll(clock);

        Image image = new Image(getClass().getResource("/images/satellite.jpg").toExternalForm());
        imageView.setLayoutX(1250);

        browser.setOnAction(Event -> openBrowser());
        message.setOnAction(Event -> openMessage());
        satellite.setOnAction(Event -> openSatData());
    }

    public void openSatData(){
        wifiSignalAdder.getScheduledExecutorService().shutdown();
        viewFactory.openSatDataWindow();
        try {
            wifiSignalAdder.getScheduledExecutorService().awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }
    public void openBrowser(){
        wifiSignalAdder.getScheduledExecutorService().shutdown();
        viewFactory.openBrowserWindow();
        try {
            wifiSignalAdder.getScheduledExecutorService().awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }

    public void openMessage(){
        wifiSignalAdder.getScheduledExecutorService().shutdown();
        viewFactory.openBlackboardWindow();
        try {
            wifiSignalAdder.getScheduledExecutorService().awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }
}

