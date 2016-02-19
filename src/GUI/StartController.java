package GUI;

import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by budocf on 2/18/2016.
 */
public class StartController {
    public Text LoadingBarText;
    public javafx.scene.control.ProgressBar ProgressBar;

    public void LoadConfig(ActionEvent actionEvent) {
        LoadingBarText.setText("Getting user input");
        ProgressBar.setProgress(0);
        FileChooser fileChooser = new FileChooser();


    }

    public void Analyze(ActionEvent actionEvent) {

    }
}
