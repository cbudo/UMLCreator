package GUI;

import InputHandling.DataView;
import InputHandling.IParserViewer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by budocf on 2/17/2016.
 */
public class DesignParserGUI extends Application {
    private Stage primaryStage;
    private Pane rootLayout;
    private IParserViewer parserViewer;
    private final Properties properties = new Properties();
    final Button config = new Button("Load Config");
    final Button analyze = new Button("Analyze");
    final ProgressBar progressBar = new ProgressBar(0);
    final Text text = new Text("Waiting for user input...");

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the root layout.
     */
    public void initStartPage() {
        // Load root layout from fxml file.

        final FileChooser fileChooser = new FileChooser();
        config.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    try {
                        parserViewer.setPropertiesFile(file.getAbsolutePath());
                        text.setText("Config file read!");
                        analyze.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                AnalyzeClasses();
                            }
                        });
                    } catch (IOException e) {
                        text.setText("Config file failed to load!");
                        e.printStackTrace();
                    }
                }

            }

        });
        final GridPane pane = new GridPane();
        GridPane.setConstraints(config, 0, 0);
        GridPane.setConstraints(analyze, 1, 0);
        GridPane.setConstraints(progressBar, 0, 1, 2, 1);
        GridPane.setHalignment(progressBar, HPos.CENTER);
        GridPane.setConstraints(text, 0, 2, 2, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        pane.setHgap(60);
        pane.setVgap(60);
        pane.getChildren().addAll(config, analyze, progressBar, text);

        rootLayout = new VBox(12);
        rootLayout.getChildren().add(pane);
        rootLayout.setPadding(new Insets(120, 120, 120, 120));

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void AnalyzeClasses() {
        // run code analysis based on the info in the properties file
        text.setText("Starting Analysis");

        // load new
    }


    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Design Parser");
        parserViewer = new DataView();

        initStartPage();
    }
}
