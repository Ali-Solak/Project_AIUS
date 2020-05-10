package  Project_AIUS.Controller;

import  Project_AIUS.Service.InputOutput;
import  Project_AIUS.View.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.*;

public class addMessageWindow extends BaseController implements Initializable {

    private InputOutput inputOutput = new InputOutput();
    private List<Integer> messageCounter;
    @FXML
    private Button add;
    @FXML
    private Label warning;
    @FXML
    private TextArea textField;

    public addMessageWindow(List<Integer> messageCounter, ViewFactory viewFactory, String fxml) {
        super(viewFactory, fxml);
        this.messageCounter = messageCounter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAddButton();
        warning.setVisible(false);
        System.out.println(messageCounter);

    }

    public void setupAddButton() {

        if (messageAvailable(1)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m1.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if (messageAvailable(2)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m2.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if (messageAvailable(3)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m3.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if (messageAvailable(4)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m4.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if (messageAvailable(5)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m5.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if (messageAvailable(6)) {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    inputOutput.writeFile(textField.getText(), "C://AIUS/m6.txt");
                    viewFactory.openBlackboardWindow();
                }
            });
        }
        if(!messageAvailable(1) &&
                !messageAvailable(2) &&
                !messageAvailable(3) &&
                !messageAvailable(4) &&
                !messageAvailable(5) &&
                !messageAvailable(6)){
            warning.setVisible(true);
        }

    }

    public boolean messageAvailable(int messageNumber) {

        for (int message : messageCounter) {
            if (message == messageNumber) {
                System.out.println(message);
                System.out.println(messageNumber);
                return false;
            }
        }
        return true;
    }
}
