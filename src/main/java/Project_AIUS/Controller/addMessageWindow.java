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
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * Window to add Messages
 *
 */
public class addMessageWindow extends BaseController implements Initializable {

    private InputOutput inputOutput = new InputOutput();
    private List<Integer> messageCounter;
    @FXML
    private Button add;
    @FXML
    private Label warning;
    @FXML
    private TextArea textField;

    public addMessageWindow(ViewFactory viewFactory, String fxml) {
        super(viewFactory, fxml);
        this.messageCounter = messageCounter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        add.setOnAction(Event -> createMessage());
        warning.setVisible(false);
    }

    /**
     * Creates a textfile. Using String.format iterating name, until it's possible to create file
     * if already exists and then writes file
     *
     */
    public void createMessage() {
        int counter = 0;
        boolean running = true;
        String filenname = null;
        while (running && counter < 100) {
            try {
                filenname = String.format("%s-%03d", "message", counter);
                Files.createFile(Paths.get("Messages/"+filenname+".txt")); //String format = concat, falls bereits existiert
                running = false;                                                     //Solange probieren bis funktioniert
            } catch (FileAlreadyExistsException e) {
                System.out.println("Datei bereits vorhanden, zähle auto hoch");
                counter++;
            } catch (IOException e) {
                e.getStackTrace();
                System.err.println("Fehler beim Dateizugriff");
            } catch (Exception e) {
                System.err.println("Fehler mit der Datei");
                running = false;
            }
        }
        inputOutput.writeFile(textField.getText(),"Messages/"+filenname+".txt");
        viewFactory.openBlackboardWindow();
    }

}
