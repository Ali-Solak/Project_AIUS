package Project_AIUS;

import Project_AIUS.View.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.openMainWindow();
    }


    public static void main(String[] args) {
        launch();
    }

}
