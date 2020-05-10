package Project_AIUS.Service;


import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class InputOutput {

    public InputOutput() {
        try {
            File directory = new File("C://AIUS");
            if (! directory.exists()){
                directory.mkdir();
            }

            Files.createFile(Paths.get("C://AIUS/m1.txt"));
            Files.createFile(Paths.get("C://AIUS/m2.txt"));
            Files.createFile(Paths.get("C://AIUS/m3.txt"));
            Files.createFile(Paths.get("C://AIUS/m4.txt"));
            Files.createFile(Paths.get("C://AIUS/m5.txt"));
            Files.createFile(Paths.get("C://AIUS/m6.txt"));
        }
        catch(Exception e){
            e.getMessage();
        }
    }


    public void writeFile(String message, String DATEI) {

        try (FileWriter writer = new FileWriter(DATEI,false);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(message);
            System.out.println("Die Datei wurde erfolgreich geschrieben.");

        }
        catch (FileNotFoundException e){
            System.err.println(DATEI + " nicht gefunden.");
        }
        catch (IOException e) {
            System.err.println("Beim Schreiben der Datei " + DATEI + " ist ein Fehler aufgetreten!");
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    public String readFile(String DATEI) {

        Charset charset = Charset.forName("ISO-8859-1");
        Path path = Paths.get(DATEI);

        String message = "";
        StringBuilder messageBuilder = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line = reader.readLine();

            while (line != null) {
                messageBuilder.append(line);
                messageBuilder.append("\n");
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println(DATEI + " kann nicht geöffnet werden!");
            //e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Beim Lesen von " + DATEI + " ist ein Fehler aufgetreten!");
            //e.printStackTrace();
        }
        message = messageBuilder.toString();
        return message;
    }
    public ArrayList<Integer> readSatFile(String DATEI) {

        Charset charset = Charset.forName("ISO-8859-1");
        Path path = Paths.get(DATEI);

        ArrayList<Integer> message = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line = reader.readLine();

            while (line != null) {
                message.add(Integer.parseInt(line));
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println(DATEI + " kann nicht geöffnet werden!");
            //e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Beim Lesen von " + DATEI + " ist ein Fehler aufgetreten!");
            //e.printStackTrace();
        }
        return message;
    }

    public void writeFileAppend(String message, String DATEI) {

        try (FileWriter writer = new FileWriter(DATEI,true);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(message);
            System.out.println("Die Datei wurde erfolgreich geschrieben.");

        }
        catch (FileNotFoundException e){
            System.err.println(DATEI + " nicht gefunden.");
        }
        catch (IOException e) {
            System.err.println("Beim Schreiben der Datei " + DATEI + " ist ein Fehler aufgetreten!");
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
    }

}

