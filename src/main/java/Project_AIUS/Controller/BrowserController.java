package Project_AIUS.Controller;

import Project_AIUS.Service.Browser;
import Project_AIUS.Service.WifiSignalAdder;
import Project_AIUS.View.ViewFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.StackedFontIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static javafx.concurrent.Worker.State.FAILED;


/**
 * Window utilises Javafx webview to create a Browser
 */
public class BrowserController extends BaseController implements Initializable {

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
    private TabPane tb;
    private WifiSignalAdder wifiSignalAdder;



    public BrowserController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        home.setOnAction(Event -> openMain());
        message.setOnAction(Event -> openMessage());
        satellite.setOnAction(Event -> openSatData());
        setUpTabs();
    }



    /**
     * A tab is created with an attached listener, if a the tab(plus button) is pressed
     * a new tab is created together with all neccesary elements (buttons, webview etc)
     */
    public void setUpTabs() {
        setupFirstTab();

        Tab newtab = new Tab();
        newtab.setText("+");
        newtab.setClosable(true);
        tb.getTabs().addAll(newtab);

        tb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldSelectedTab, Tab newSelectedTab) {
                if (newSelectedTab == newtab) {

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    wifiSignalAdder = new WifiSignalAdder();
                    wifiSignalAdder.ajustData(imageView);

                    Tab tab = new Tab();

                    JFXButton search = new JFXButton();
                    JFXButton google = new JFXButton();
                    TextField searchField = new TextField();
                    JFXButton forward = new JFXButton();
                    JFXButton back = new JFXButton();
                    JFXButton refresh = new JFXButton();
                    JFXSpinner loading = new JFXSpinner();
                    ComboBox<String> history = new ComboBox<>();
                    history.setPromptText("History");
                    searchField.setText("https://www.google.com");

                    loading.setPrefSize(19, 25);
                    HBox hBox = new HBox(5);
                    hBox.getChildren().setAll(setupButton(back, "enty-arrow-with-circle-left"), setupButton(forward, "enty-arrow-with-circle-right"), loading,
                            setupButton(refresh, "eli-refresh"), setupButton(google, "eli-home-alt"), setupButton(search, "eli-search"),
                            searchField, history, imageView);
                    HBox.setHgrow(searchField, Priority.ALWAYS);
                    forward.setTranslateX(-13);


                    tab.setText("new Tab");
                    BorderPane bp = new BorderPane();

                    bp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                    bp.setTop(hBox);

                    Browser browser = new Browser();
                    bp.setCenter(browser.setupWebServices(tab, forward, back, search, refresh, google, searchField, loading, history));
                    tab.setContent(bp);


                    //observable if all tabs are closed, create new tab
                    final ObservableList<Tab> tabs = tb.getTabs();
                    tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                    tabs.add(tabs.size() - 1, tab);

                    //close respective tab
                    tb.getSelectionModel().select(tab);
                    tab.setGraphic(closeButton(browser, tab));
                }
            }
        });
    }



    private void setupFirstTab() {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        wifiSignalAdder = new WifiSignalAdder();
        wifiSignalAdder.ajustData(imageView);

        Tab firstTab = new Tab();
        BorderPane bp = new BorderPane();

        JFXButton search = new JFXButton();
        JFXButton google = new JFXButton();
        TextField searchField = new TextField();
        JFXButton forward = new JFXButton();
        JFXButton back = new JFXButton();
        JFXButton refresh = new JFXButton();
        JFXSpinner loading = new JFXSpinner();
        ComboBox<String> history = new ComboBox<>();
        history.setPromptText("History");
        searchField.setText("https://www.google.com");

        loading.setPrefSize(19, 25);
        HBox hBox = new HBox(5);
        hBox.getChildren().setAll(setupButton(back, "enty-arrow-with-circle-left"), setupButton(forward, "enty-arrow-with-circle-right"), loading,
                setupButton(refresh, "eli-refresh"), setupButton(google, "eli-home-alt"), setupButton(search, "eli-search"),
                searchField, history, imageView);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        forward.setTranslateX(-13);

        Browser browser = new Browser();

        bp.setCenter(browser.setupWebServices(firstTab, forward, back, search, refresh, google, searchField, loading, history));
        bp.setTop(hBox);

        firstTab.setContent(bp);

        firstTab.setText("Start");
        firstTab.setClosable(true);
        tb.getTabs().addAll(firstTab);
        firstTab.setGraphic(closeButton(browser, firstTab));
    }

    private void closeTab(Browser browser, Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (handler != null) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
            browser.closeBrowser();
        }
    }


    /**
     * Sets up closing button for tab
     * @param tab
     * @return
     */
    private Button closeButton(Browser browser, Tab tab) {
        StackedFontIcon stackedFontIcon = new StackedFontIcon();
        FontIcon fontIcon = new FontIcon("eli-remove-circle");
        fontIcon.setFill(Color.rgb(168, 104, 160));
        stackedFontIcon.getChildren().addAll(fontIcon);
        Button closeButton = new Button();
        closeButton.setGraphic(stackedFontIcon);

        closeButton.setOnAction(Event -> closeTab(browser, tab));

        closeButton.setStyle("-fx-background-color: none;" +
                "-fx-background-radius: 0 0 0 0;");

        return closeButton;
    }

    private JFXButton setupButton(JFXButton button, String iconCode) {
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


    public void openSatData() {
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        viewFactory.openSatDataWindow();

    }

    public void openMain() {
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        viewFactory.openMainWindow();
    }

    public void openMessage() {
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        viewFactory.openBlackboardWindow();
    }
}
