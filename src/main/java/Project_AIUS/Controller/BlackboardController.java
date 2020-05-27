package  Project_AIUS.Controller;

import  Project_AIUS.Model.Message;
import  Project_AIUS.Service.InputOutput;
import  Project_AIUS.Service.WifiSignalAdder;
import  Project_AIUS.View.ViewFactory;
import com.jfoenix.controls.JFXButton;
import eu.hansolo.tilesfx.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Window to leave messages.
 * Window reads text files. If files are written, the panels with text are set visible.
 *
 */
public class BlackboardController extends BaseController implements Initializable {

    private InputOutput inputOutput = new InputOutput();
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;
    private Message m6;
    private Boolean running = true;
    private List<Integer> messageCounter = new ArrayList<>();
    @FXML
    private JFXButton home;
    @FXML
    private JFXButton browser;
    @FXML
    private JFXButton message;
    @FXML
    private JFXButton satellite;
    @FXML
    private JFXButton settings;
    @FXML
    private Pane pane;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;
    @FXML
    private Pane pane4;
    @FXML
    private Pane pane5;
    @FXML
    private Button add;
    @FXML
    private Button close;
    @FXML
    private Button close1;
    @FXML
    private Button close2;
    @FXML
    private Button close3;
    @FXML
    private Button close4;
    @FXML
    private Button close5;
    @FXML
    private Label textField;
    @FXML
    private Label textField1;
    @FXML
    private Label textField2;
    @FXML
    private Label textField3;
    @FXML
    private Label textField4;
    @FXML
    private Label textField5;
    @FXML
    private AnchorPane mainWindow;
    @FXML  ImageView imageView;
    private WifiSignalAdder wifiSignalAdder;


    public BlackboardController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);

        m1 = new Message("");
        m2 = new Message("");
        m3 = new Message("");
        m4 = new Message("");
        m5 = new Message("");
        m6 = new Message("");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageView.setLayoutX(1350);
        WifiSignalAdder wifiSignalAdder = new WifiSignalAdder();
        wifiSignalAdder.ajustData(imageView);

        home.setOnAction(Event -> viewFactory.openMainWindow());
        browser.setOnAction(Event -> viewFactory.openBrowserWindow());
        satellite.setOnAction(Event -> viewFactory.openSatDataWindow());

        textField.setWrapText(true);
        textField.setTextAlignment(TextAlignment.JUSTIFY);
        textField1.setWrapText(true);
        textField1.setTextAlignment(TextAlignment.JUSTIFY);
        textField2.setWrapText(true);
        textField2.setTextAlignment(TextAlignment.JUSTIFY);
        textField3.setWrapText(true);
        textField3.setTextAlignment(TextAlignment.JUSTIFY);
        textField4.setWrapText(true);
        textField4.setTextAlignment(TextAlignment.JUSTIFY);
        textField5.setWrapText(true);
        textField5.setTextAlignment(TextAlignment.JUSTIFY);

        add.setOnAction(Event -> viewFactory.openAddStage(messageCounter));
        close.setOnAction(Event -> close(pane,   "C://AIUS/m1.txt",1));
        close1.setOnAction(Event -> close(pane1, "C://AIUS/m2.txt",2));
        close2.setOnAction(Event -> close(pane2, "C://AIUS/m3.txt",3));
        close3.setOnAction(Event -> close(pane3, "C://AIUS/m4.txt",4));
        close4.setOnAction(Event -> close(pane4, "C://AIUS/m5.txt",5));
        close5.setOnAction(Event -> close(pane5, "C://AIUS/m6.txt",6));

        pane.setVisible(false);
        pane1.setVisible(false);
        pane2.setVisible(false);
        pane3.setVisible(false);
        pane4.setVisible(false);
        pane5.setVisible(false);

        readMessages();
        setupMessages(m1, m2, m3, m4, m5, m6);
    }

    public void readMessages() {
        m1.setMessage(inputOutput.readFile("C://AIUS/m1.txt"));
        m2.setMessage(inputOutput.readFile("C://AIUS/m2.txt"));
        m3.setMessage(inputOutput.readFile("C://AIUS/m3.txt"));
        m4.setMessage(inputOutput.readFile("C://AIUS/m4.txt"));
        m5.setMessage(inputOutput.readFile("C://AIUS/m5.txt"));
        m6.setMessage(inputOutput.readFile("C://AIUS/m6.txt"));
    }


    public void setupMessages(Message m1, Message m2, Message m3, Message m4, Message m5, Message m6) {
        if (m1.getMessage().length() > 0) {
            pane.setVisible(true);
            textField.setText(m1.getMessage());
            messageCounter.add(1);
        }
        if (m2.getMessage().length() > 0) {
            pane1.setVisible(true);
            textField1.setText(m2.getMessage());
            messageCounter.add(2);
        }
        if (m3.getMessage().length() > 0) {
            pane2.setVisible(true);
            textField2.setText(m3.getMessage());
            messageCounter.add(3);
        }
        if (m4.getMessage().length() > 0) {
            pane3.setVisible(true);
            textField3.setText(m4.getMessage());
            messageCounter.add(4);
        }
        if (m5.getMessage().length() > 0) {
            pane4.setVisible(true);
            textField4.setText(m5.getMessage());
            messageCounter.add(5);
        }
        if (m6.getMessage().length() > 0) {
            pane5.setVisible(true);
            textField5.setText(m6.getMessage());
            messageCounter.add(6);
        }
    }

    public void openAddWindow() {
        viewFactory.openAddStage(this.messageCounter);
    }

    public void close(Pane pane, String data,int messageNumber) {
        pane.setVisible(false);
        inputOutput.writeFile("", data);

        Iterator<Integer> it = messageCounter.iterator();
        while(it.hasNext()){
            int message = it.next();
            if(message == messageNumber){
                it.remove();
            }
        }
    }
}
