package GUI;

import InputHandling.DataView;
import InputHandling.IParserViewer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by budocf on 2/17/2016.
 */
public class DesignParserGUI extends Application {
    private Stage primaryStage;
    private Pane result;
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
        progressBar.setProgress(0);
        text.setText("Waiting for user input...");

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

        Pane startPane = new VBox(12);
        startPane.getChildren().add(pane);
        startPane.setPadding(new Insets(120, 120, 120, 120));

        // Show the scene containing the root layout.
        Scene scene = new Scene(startPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void AnalyzeClasses() {
        // run code analysis based on the info in the properties file
        text.setText("Starting Analysis");
        progressBar.setProgress(.1);

        // get phases from dataview
        // set value to increment progress bar by (1.00-.10)/size(phases)

        // iterate over phases and increment progress bar as you go

        // after all phases are over
        // load new panel

        progressBar.setProgress(1);
        text.setText("Finished with analysis, loading results...");

        initResults();
    }

    private void initResults() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction( event -> initStartPage());
        MenuItem export = new MenuItem("Export");
        export.setOnAction(event -> exportImage());
        file.getItems().addAll(restart);
        menuBar.getMenus().add(file);
        result = new VBox(12);
        SplitPane pane = new SplitPane();
        TreeItem<String> dummyNode = new CheckBoxTreeItem<>();
        pane.setOrientation(Orientation.HORIZONTAL);
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("1");
        rootItem.setExpanded(true);

        CheckBoxTreeItem<String> otherRootItem = new CheckBoxTreeItem<>("2");
        otherRootItem.setExpanded(true);
        dummyNode.getChildren().addAll(rootItem, otherRootItem);
        TreeView tree = new TreeView(dummyNode);
        tree.setEditable(true);
        tree.setShowRoot(false);

        tree.setCellFactory(CheckBoxTreeCell.forTreeView());

        CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>("a");
        rootItem.getChildren().add(checkBoxTreeItem);
        otherRootItem.getChildren().add(checkBoxTreeItem);
        checkBoxTreeItem = new CheckBoxTreeItem<>("b");
        rootItem.getChildren().add(checkBoxTreeItem);
        otherRootItem.getChildren().add(checkBoxTreeItem);

        tree.setRoot(dummyNode);
        Image image = new Image("https://scontent-ord1-1.xx.fbcdn.net/hphotos-xft1/v/t1.0-9/12540763_886543088597_7920897415091937587_n.jpg?oh=6f9e7f688cd651de2b14af2c9b91b4f3&oe=5758BC81");
        ImageView img = new ImageView(image);
        pane.getItems().addAll(tree, img);
        result.getChildren().add(pane);
        BorderPane borderPane = new BorderPane(result, menuBar, null, null, null);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Map<String,List<String>> getPatternClasses(){
        Map<String, List<String>> patternClasses = new HashMap<>();
        List stuff = new ArrayList<>();
        stuff.add("UMLCreator.DataStorage.DataStore.ParsedDataStorage");
        patternClasses.put("Singleton",stuff);
        return patternClasses;
    }

    private void exportImage() {
        // export image - to user specified directory?
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

        //initStartPage();
        initResults();
    }
}
