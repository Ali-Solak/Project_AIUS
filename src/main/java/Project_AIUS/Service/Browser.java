package Project_AIUS.Service;

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

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Window utilises Javafx webview to create a Browser
 */
public class Browser {

    private final WebEngine webEngine;
    private WebView webView;
    String htLink = "https://";
    String DEFAULT_URL = "https://www.google.com";
    String searchInput;

    public Browser() {
        this.webView = new WebView();
        this.webEngine = this.webView.getEngine();
    }

    public void search(TextField searchField) {
        searchInput = searchField.getText();
        if (searchField.getText().contains("https:")) {
            webEngine.load(searchInput);
        } else {
            webEngine.load(htLink + searchInput);
        }

    }

    /**
     * Inject JavaScript into Webview to go back to last visited site
     *
     */
    public void goBack() {
        Platform.runLater(() -> {
            webEngine.executeScript("history.back()");
        });
    }

    public void goForward() {
        Platform.runLater(() -> {
            webEngine.executeScript("history.forward()");
        });
    }


    private void setupButtons(JFXButton forward, JFXButton back, JFXButton search, JFXButton refresh, JFXButton google, TextField searchField) {
        forward.setOnAction(Event -> goForward());
        back.setOnAction(Event -> goBack());
        search.setOnAction(Event -> search(searchField));
        refresh.setOnAction(Event -> reload());
        google.setOnAction(Event -> webEngine.load(DEFAULT_URL));

    }


    private void reload() {
        webEngine.reload();
    }


    /**
     * @param loading   Uses Listener to check worker state of the webengine and binds it to spinner.
     *                  Shows a loading symbol if something is being loaded.
     */
    private void loading(JFXSpinner loading) {

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
     * @param searchField Ajusts the search field in the browser with the current used websites link
     */
    private void ajustSearchField(TextField searchField) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchField.setText(newValue);
            }
        });
    }

    private void ajustTabText(Tab tab) {
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
     * @param comboBox
     */
    private void addToHistory(ComboBox<String> comboBox) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                comboBox.getItems().add(newValue);
            }
        });
    }

    public void putHistoryInSearchField(TextField searchField, ComboBox<String> comboBox) {
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
    public WebView setupWebServices(Tab tab, JFXButton forward, JFXButton back, JFXButton search, JFXButton refresh, JFXButton google, TextField searchField, JFXSpinner loading, ComboBox<String> comboBox) {

        webEngine.load(DEFAULT_URL);

        setupButtons(forward, back, search, refresh, google, searchField);

        addToHistory(comboBox);
        putHistoryInSearchField(searchField, comboBox);

        loading(loading);
        ajustSearchField(searchField);
        ajustTabText(tab);
        loadingFailed();

        return webView;
    }


    /**
     * Shows alert window when Worker state of webengine is failed
     *
     */
    private void loadingFailed() {
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

    public void closeBrowser(){
        webEngine.executeScript("window.stop()");
        webEngine.executeScript("window.close()");
    }


}
