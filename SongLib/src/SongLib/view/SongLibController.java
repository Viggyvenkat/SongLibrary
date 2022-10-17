/*
Names: Blaise Willson, Vingnesh Venkat
java version: java 16
javafx version: javafx 19
 */

package SongLib.view;

import SongLib.app.SongLib;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.*;


public class SongLibController implements Initializable {

    @FXML
    private TextField album;

    @FXML
    private TextField artist;

    @FXML
    private TextField name;

    @FXML
    private TextField year;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Button Delete;
    @FXML
    private TextArea SongID;
    @FXML
    private ListView<String> SongList;

    Alert alert = new Alert(Alert.AlertType.NONE); //alert for

    public void deleteSelection(ActionEvent event) throws IOException{

        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            File inputFile = new File("inputfile.txt");
            File tempFile = new File("tempfile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = SongList.getSelectionModel().getSelectedItem();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.contains(lineToRemove)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
            reader.close();
            inputFile.delete();

            File inputFile1 = new File("inputfile.txt");
            tempFile.renameTo(inputFile1);
            int index = SongList.getSelectionModel().getSelectedIndex() + 1;
            SongList.getSelectionModel().select(index);
            SongList.getFocusModel().focus(index);
            SongList.getItems().remove(lineToRemove);
        }

    }

    public void addSong(ActionEvent event) throws IOException{

        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            if (name.getText().equals("") || artist.getText().equals("")) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("No title or artist");
                alert.show();
                return;
            }

            Scanner sc = null;
            try {
                sc = new Scanner(new File("inputfile.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            List<String> lines = new ArrayList<String>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(name.getText().trim() + "| " + artist.getText().trim())) {
                    //make popup error
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("No title or artist");
                    alert.show();
                    return;
                }
            }
            sc.close();

            FileWriter writer = new FileWriter("inputfile.txt", true);
            writer.write(name.getText().trim() + "| " + artist.getText().trim() + " | " + year.getText().trim() + "| " + album.getText().trim() + "\n");
            writer.close();

            //SongList.getItems().add((name.getText()+", "+artist.getText()));
            addList();
            SongList.getSelectionModel().select(name.getText().trim() + "| " + artist.getText().trim());
            int index = SongList.getSelectionModel().getSelectedIndex();
            SongList.getFocusModel().focus(index);
            setZero();
        }
    }
    public void editSong(ActionEvent event) throws IOException{
        ObservableList<String> selectedItem = SongList.getSelectionModel().getSelectedItems();
        String itemSelected = (selectedItem.isEmpty()?"uh oh no selection":selectedItem.toString());
        itemSelected = itemSelected.substring(1, itemSelected.length()-1);

        Scanner sc = null;
        try {
            sc = new Scanner(new File("inputfile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<String>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.contains(itemSelected)){
                name.setText(line.split("\\| ")[0]);
                artist.setText(line.split("\\| ")[1]);
                year.setText(line.split("\\| ")[2]);
                album.setText(line.split("\\| ")[3]);
            }
        }
        sc.close();

    }

    public void confirmEdit(ActionEvent event) throws IOException{
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            if (name.getText().equals("") || artist.getText().equals("")) {
                //add error
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("No title or artist");
                alert.show();
                return;
            }

            deleteSelection(event);
            addSong(event);
        }
    }

    public void setZero(){
        name.setText("");
        artist.setText("");
        year.setText("");
        album.setText("");
    }
    /*
    public void switchToEditPopUp(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("SongLib/view/SongLibSceneEditPopUp.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    */

    public void addList(){
        SongList.getItems().clear();
        Scanner sc = null;
        try {
            sc = new Scanner(new File("inputfile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<String>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            lines.add(line.split("\\|")[0] + "|" + line.split("\\|")[1]);
        }
        sc.close();

        //sort in alphabetic order
        lines.sort(String::compareToIgnoreCase);

        String[] arr = lines.toArray(new String[0]);
        SongList.getItems().addAll(arr);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addList();
        SongList.getSelectionModel().select(1);

        SongList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        SongList.getSelectionModel().selectedItemProperty().addListener(this::changedSelection);
    }

    private void changedSelection(ObservableValue<? extends String> Observable, String oldSel, String newSel){

        ObservableList<String> selectedItem = SongList.getSelectionModel().getSelectedItems();
        String itemSelected = (selectedItem.isEmpty()?"uh oh no selection":selectedItem.toString());
        itemSelected = itemSelected.substring(1, itemSelected.length()-1);

        Scanner sc = null;
        try {
            sc = new Scanner(new File("inputfile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<String>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.contains(itemSelected)){
                SongID.setText(line);
            }
        }
        sc.close();
    }
}