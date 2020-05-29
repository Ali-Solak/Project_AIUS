package  Project_AIUS.View;

import  Project_AIUS.Controller.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class ViewFactory {


    private Stage stage;
    private ColorTheme colorTheme = ColorTheme.DARK;
    private FontSize fontSize = FontSize.MEDIUM;

    public ViewFactory() {
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void openMainWindow(){
        BaseController mainWindow = new MainWindow(this, "/AIUS/MainWindow.fxml");
        initializeWindow(mainWindow);
    }
    public void openBrowserWindow(){
        BaseController browserWindow = new BrowserWindow(this, "/AIUS/BrowserWindow.fxml");
        initializeWindow(browserWindow);
    }
    public void openSatDataWindow(){
        BaseController browserWindow = new SatelliteDataWindow(this, "/AIUS/SatelliteDataWindow.fxml");
        initializeWindow(browserWindow);
    }
    public void openBlackboardWindow(){
        BaseController browserWindow = new BlackboardController(this, "/AIUS/BlackboardWindow.fxml");
        initializeWindow(browserWindow);
    }
    public void openAddStage(){
        BaseController addWindow = new addMessageWindow(this,"/AIUS/addMessageWindow.fxml");
        initializeStage(addWindow);
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }


    private void initializeWindow(BaseController baseController) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        var scene = new Scene(parent);
        updateStyle(scene);
        stage.setScene(scene);

        stage.hide();
        stage.setMaximized(true);
        stage.show();
    }

    private void initializeStage(BaseController baseController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }



    private void updateStyle(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
        //scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
    }

    public Stage getStage() {
        return stage;
    }
}
