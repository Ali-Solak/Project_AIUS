package Project_AIUS.Controller;

import Project_AIUS.Model.Message;
import Project_AIUS.Service.FileComparator;
import Project_AIUS.Service.InputOutput;
import Project_AIUS.Service.WifiSignalAdder;
import Project_AIUS.View.ViewFactory;
import com.jfoenix.controls.JFXButton;
import eu.hansolo.tilesfx.Tile;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.StackedFontIcon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.spec.MGF1ParameterSpec;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Window to leave messages.
 * Window reads all text files in folder and creates a message-pane for all to make them visible.
 */
public class BlackboardController extends BaseController implements Initializable {

    private InputOutput inputOutput = new InputOutput();
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
    private Button add;
    @FXML
    private ScrollPane scroll;
    @FXML
    private AnchorPane mainWindow;
    @FXML
    ImageView imageView;
    private WifiSignalAdder wifiSignalAdder;
    private File files;
    private ArrayList<File> fileListo;

    public BlackboardController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);

        try {
            File directory = new File("Messages");
            if (! directory.exists()){
                directory.mkdir();
            }
        }
        catch(Exception e){
            e.getMessage();
        }
        files = new File("Messages");
        fileListo = new ArrayList<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //vertical only
        scroll.setFitToWidth(true);

        imageView.setLayoutX(1350);
        WifiSignalAdder wifiSignalAdder = new WifiSignalAdder();
        wifiSignalAdder.ajustData(imageView);

        home.setOnAction(Event -> viewFactory.openMainWindow());
        browser.setOnAction(Event -> viewFactory.openBrowserWindow());
        satellite.setOnAction(Event -> viewFactory.openSatDataWindow());
        add.setOnAction(Event -> viewFactory.openAddStage());

        setupMessages(readMessagesAndSort());
    }

    /**
     * Takes all Files from Folder, puts it into an ArrayLists and sorts it by date
     * @return List of Files
     */
    public ArrayList<File> readMessagesAndSort() {
        File[] fileList = files.listFiles();

        for (File f : fileList) {
            fileListo.add(f);
        }

        Collections.sort(fileListo, new FileComparator());

        return fileListo;
    }

    /**
     * uses setupMessagePane to create Message-Panes for every file and moves Message-Panes in right position
     * by iterating through all files and increasing x and y positions.
     * @param fileListo
     */
    public void setupMessages(ArrayList<File> fileListo) {
        double x = 229;
        double y = 122;
        int messageCounter = 0;

        for (File f : fileListo) {
            if (messageCounter % 3 == 0 && messageCounter != 0) {
                y += 420;
                x = 226;
                setupMessagePane(f, x, y);
            }

            setupMessagePane(f, x, y);
            x += 398;
            messageCounter++;
        }
    }

    /**
     * Takes File and Creates Message-Panes with labels and close buttons. Uses x and y positions
     * @param file
     * @param x
     * @param y
     */
    public void setupMessagePane(File file, double x, double y) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.MM.yyyy HH:mm");
        Long now = file.lastModified();
        Label timestamp = new Label();
        timestamp.setText(simpleDateFormat.format(now));
        timestamp.setStyle("-fx-text-fill: #a868a0");
        timestamp.setTextAlignment(TextAlignment.CENTER);
        timestamp.setWrapText(true);

        Pane pane = new Pane();
        HBox hBox = new HBox();
        HBox hBox1 = new HBox();
        Button close = new Button();
        Label textField = new Label();
        hBox.getStyleClass().add("hbox");
        hBox1.getStyleClass().add("hbox");
        pane.getStyleClass().add("pane");

        pane.setPrefHeight(300);
        pane.setPrefWidth(300);

        textField.setPrefHeight(231);
        textField.setPrefWidth(298);
        textField.setLayoutX(2);
        textField.setLayoutY(41);
        textField.setWrapText(true);
        textField.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        textField.setTextAlignment(TextAlignment.JUSTIFY);
        textField.setAlignment(Pos.TOP_LEFT);
        textField.getStyleClass().add("textField");
        textField.setText(inputOutput.readFile(file.toString()));

        hBox.setPrefHeight(41);
        hBox.setPrefWidth(299);
        hBox.setLayoutX(0);
        hBox.setLayoutY(259);
        hBox1.setLayoutX(0);
        hBox1.setLayoutY(0);
        hBox1.setPrefHeight(41);
        hBox1.setPrefWidth(299);
        hBox1.getChildren().addAll(setupButton(close, "eli-remove-circle"));
        hBox.getChildren().addAll(timestamp);
        pane.getChildren().addAll(hBox, hBox1, textField);

        close.setOnAction(Event -> close(pane, file));

        pane.setLayoutX(x);
        pane.setLayoutY(y);

        mainWindow.getChildren().addAll(pane);
    }

    public void openAddWindow() {
        viewFactory.openAddStage();
    }

    public void close(Pane pane, File file) {
        mainWindow.getChildren().remove(pane);
        file.delete();
        viewFactory.openBlackboardWindow();
    }

    private Button setupButton(Button button, String iconCode) {
        StackedFontIcon stackedFontIcon = new StackedFontIcon();
        FontIcon fontIcon = new FontIcon(iconCode);
        stackedFontIcon.getChildren().addAll(fontIcon);
        button.setStyle(" -fx-background-color: #141124;" +
                "-fx-background-radius: 0 0 0 0;");
        fontIcon.setFill(Color.rgb(168, 104, 160));
        fontIcon.setIconSize(20);
        button.setGraphic(stackedFontIcon);
        return button;
    }
}
