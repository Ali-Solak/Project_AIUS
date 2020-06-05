package Project_AIUS.Controller;

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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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

    String htLink = "https://";
    String DEFAULT_URL = "https://www.google.com";
    String searchInput;

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


    public void search(WebEngine engine, TextField searchField) {
        searchInput = searchField.getText();
        if (searchField.getText().contains("https:")) {
            engine.load(searchInput);
        } else {
            engine.load(htLink + searchInput);
        }

    }

    /**
     * Inject JavaScript into Webview to go back to last visited site
     *
     * @param engine
     */
    public void goBack(WebEngine engine) {
        Platform.runLater(() -> {
            engine.executeScript("history.back()");
        });
    }

    public void goForward(WebEngine engine) {
        Platform.runLater(() -> {
            engine.executeScript("history.forward()");
        });
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

                    bp.setCenter(setupWebServices(tab, forward, back, search, refresh, google, searchField, loading, history));
                    tab.setContent(bp);


                    //observable if all tabs are closed, create new tab
                    final ObservableList<Tab> tabs = tb.getTabs();
                    tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                    tabs.add(tabs.size() - 1, tab);

                    tb.getSelectionModel().select(tab);
                    tab.setGraphic(closeButton(tab));
                }
            }
        });
    }

    private void setupButtons(WebEngine engine, JFXButton forward, JFXButton back, JFXButton search, JFXButton refresh, JFXButton google, TextField searchField) {
        forward.setOnAction(Event -> goForward(engine));
        back.setOnAction(Event -> goBack(engine));
        search.setOnAction(Event -> search(engine, searchField));
        refresh.setOnAction(Event -> reload(engine));
        google.setOnAction(Event -> engine.load(DEFAULT_URL));

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

        bp.setCenter(setupWebServices(firstTab, forward, back, search, refresh, google, searchField, loading, history));
        bp.setTop(hBox);

        firstTab.setContent(bp);

        firstTab.setText("Start");
        firstTab.setClosable(true);
        tb.getTabs().addAll(firstTab);
        firstTab.setGraphic(closeButton(firstTab));
    }

    private void closeTab(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (handler != null) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
        }
    }

    private void reload(WebEngine engine) {
        engine.reload();
    }

    /**
     * Sets up closing button for tab
     * @param tab
     * @return
     */
    private Button closeButton(Tab tab) {
        StackedFontIcon stackedFontIcon = new StackedFontIcon();
        FontIcon fontIcon = new FontIcon("eli-remove-circle");
        fontIcon.setFill(Color.rgb(168, 104, 160));
        stackedFontIcon.getChildren().addAll(fontIcon);
        Button closeButton = new Button();
        closeButton.setGraphic(stackedFontIcon);

        closeButton.setOnAction(Event -> closeTab(tab));

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

    /**
     * @param webEngine
     * @param loading   Uses Listener to check worker state of the webengine and binds it to spinner.
     *                  Shows a loading symbol if something is being loaded.
     */
    private void loading(WebEngine webEngine, JFXSpinner loading) {

        loading.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {

                            loading.setVisible(false);
                        }
                        if (newState == Worker.State.RUNNING) {
                            loading.setVisible(true);
                        }
                    }
                });
    }

    /**
     * @param webEngine
     * @param searchField Ajusts the search field in the browser with the current used websites link
     */
    private void ajustSearchField(WebEngine webEngine, TextField searchField) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchField.setText(newValue);
            }
        });
    }

    private void ajustTabText(WebEngine webEngine, Tab tab) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.contains("www")) {
                    tab.setText(newValue.substring(12, 18));
                } else {
                    tab.setText(newValue.substring(8, 18));
                }
            }
        });
    }

    /**
     * Adds last visited Website Urls to combobox
     *
     * @param webEngine
     * @param comboBox
     */
    private void addToHistory(WebEngine webEngine, ComboBox<String> comboBox) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                comboBox.getItems().add(newValue);
            }
        });
    }

    public void putHistoryInSearchField(WebEngine engine, TextField searchField, ComboBox<String> comboBox) {
        comboBox.setOnAction(Event -> searchField.setText(comboBox.getSelectionModel().getSelectedItem()));
    }

    /**
     * @param tab
     * @param forward
     * @param back
     * @param search
     * @param refresh
     * @param google
     * @param searchField
     * @param loading
     * @return Webview
     * Sets up all necessary services for the Webview.
     */
    private WebView setupWebServices(Tab tab, JFXButton forward, JFXButton back, JFXButton search, JFXButton refresh, JFXButton google, TextField searchField, JFXSpinner loading, ComboBox<String> comboBox) {
        final WebEngine engine;
        WebView webView = new WebView();
        engine = webView.getEngine();
        engine.load(DEFAULT_URL);

        setupButtons(engine, forward, back, search, refresh, google, searchField);

        addToHistory(engine, comboBox);
        putHistoryInSearchField(engine, searchField, comboBox);

        loading(engine, loading);
        ajustSearchField(engine, searchField);
        ajustTabText(engine, tab);
        loadingFailed(engine);

        return webView;
    }


    /**
     * Shows alert window when Worker state of webengine is failed
     * @param webEngine
     */
    private void loadingFailed(WebEngine webEngine) {
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.FAILED) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Connection Failure");
                            alert.setContentText("Connecting to website failed.");

                            alert.showAndWait();
                        }
                    }
                });
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
