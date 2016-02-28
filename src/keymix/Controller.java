package keymix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Controller {
    private Set<File> importedSounds = new TreeSet<>();
    private Map<KeyCode, AudioClip>[] maps = new Map[10];
    private boolean[] presses = new boolean[26];
    private int myMap = 0;

    private final String DEFAULT_OPTION = "Select an option.";

    @FXML private Button importFile;

    @FXML protected void keyPress(KeyEvent keyEvent) {
        String id = keyEvent.getText().toUpperCase();

        if (maps[myMap] != null && (keyEvent.getCode() == KeyCode.BACK_SPACE ||
                keyEvent.getCode() == KeyCode.SPACE)) {
            maps[myMap].values().stream().forEach(clip -> clip.stop());
        }

        if(maps[myMap] != null && id.length() == 1 && Character.isLetter(id.charAt(0))
                && maps[myMap].containsKey(keyEvent.getCode()) && !presses[id.charAt(0) - 'A']) {
            presses[id.charAt(0) - 'A'] = true;
            importFile.getScene().lookup("#" + id).getStyleClass().add("pressed");
            maps[myMap].get(keyEvent.getCode()).play();
        }
        keyEvent.consume();
    }

    @FXML protected void keyRelease(KeyEvent keyEvent) {
        //String id = keyEvent.getText();
        String id = keyEvent.getText().toUpperCase();
        //System.out.println(id);
        if(maps[myMap] != null && id.length() == 1 && Character.isLetter(id.charAt(0))
                && maps[myMap].containsKey(keyEvent.getCode())) {
            presses[id.charAt(0) - 'A'] = false;
            importFile.getScene().lookup("#" + id).getStyleClass().remove("pressed");
        }
        keyEvent.consume();
    }

    @FXML protected void importFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.midi");
        fileChooser.getExtensionFilters().add(extFilter);

        List<File> files = fileChooser.showOpenMultipleDialog(importFile.getScene().getWindow());
        if(files != null) {
            importedSounds.addAll(files);
        }
    }

    @FXML protected void removeFiles(ActionEvent event) {
        ChoiceDialog<File> dialog = new ChoiceDialog<>(new File(DEFAULT_OPTION), importedSounds);
        dialog.setTitle("Delete Sample");
        dialog.setHeaderText("Choose what sample you would like to delete");
        dialog.setContentText("Sample:");

        Optional<File> result = dialog.showAndWait();
        result.ifPresent(file -> importedSounds.remove(file));

        event.consume();
    }

    @FXML protected void clickNumber(ActionEvent event) {
        String id = ((Node)event.getTarget()).idProperty().getValue();
        myMap = id.charAt(0) - '0';
        event.consume();
    }

    @FXML protected void clickLetter(ActionEvent event) {
        String id = ((Node)event.getTarget()).idProperty().getValue();

        ChoiceDialog<File> dialog = new ChoiceDialog<>(new File(DEFAULT_OPTION), importedSounds);
        dialog.setTitle("Choose Sample");
        dialog.setHeaderText("Choose what sample you would like to map to letter " + id);
        dialog.setContentText("Sample:");

        Optional<File> result = dialog.showAndWait();
        result.ifPresent(file -> this.mapKey(file, id));

        event.consume();
    }

    private void mapKey(File file, String id) {
        if (!file.toString().equals(DEFAULT_OPTION)) {
            if (maps[myMap] == null) {
                maps[myMap] = new HashMap<>();
            }
            maps[myMap].put(KeyCode.valueOf(id), new AudioClip(file.toURI().toString()));
        } else {
            if (maps[myMap] != null) {
                maps[myMap].remove(KeyCode.valueOf(id));
            }
        }
    }

    @FXML protected void saveKeymap(ActionEvent event) {
//        File file = new File("temp");
//        FileOutputStream f = new FileOutputStream(file);
//        ObjectOutputStream s = new ObjectOutputStream(f);
//        s.writeObject(fileObj);
//        s.close();

        event.consume();
    }

    @FXML protected void importKeymap(ActionEvent event) {

        event.consume();
    }
}
