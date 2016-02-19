package GUI;

import InputHandling.DataView;
import InputHandling.IParserViewer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

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
    final ImageView img = new ImageView("file:.\\inputoutput\\ChandanHat.jpg");

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
        parserViewer.runPhases();
        // iterate over phases and increment progress bar as you go

        // after all phases are over
        // load new panel

        progressBar.setProgress(1);
        text.setText("Finished with analysis, loading results...");

        initResults();
    }

    private void initResults() {
        MenuBar menuBar = getMenu();
        result = new VBox(12);
        SplitPane pane = new SplitPane();
        TreeItem<String> dummyNode = new CheckBoxTreeItem<>();
        pane.setOrientation(Orientation.HORIZONTAL);
        TreeView tree = new TreeView(dummyNode);
        tree.setEditable(true);
        tree.setShowRoot(false);
        Map<String, Iterator<String>> data = getPatternClasses();
        for (String pattern : data.keySet()) {
            // create root
            CheckBoxTreeItem<String> node = new CheckBoxTreeItem<>(pattern);
            Iterator<String> it = data.get(pattern);
            it.forEachRemaining(s -> {
                CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(s);
                checkBoxTreeItem.setSelected(true);
                checkBoxTreeItem.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        updateImage(tree.getSelectionModel().getSelectedItems());
                    }
                });
                node.getChildren().add(checkBoxTreeItem);
            });
            node.setExpanded(true);
            // add root to tree
            dummyNode.getChildren().add(node);
        }
        tree.setCellFactory(CheckBoxTreeCell.forTreeView());

        tree.setRoot(dummyNode);

//        Image image = new Image("file:C:\\Users\\budocf\\Downloads\\1.jpg");
        img.setFitHeight(500);
        img.setFitWidth(500);
        pane.getItems().addAll(tree, img);
        result.getChildren().add(pane);
        BorderPane borderPane = new BorderPane(result, menuBar, null, null, null);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateImage(ObservableList selectedItems) {
        //update image


        //wait for image to be updated
        String imagePath = parserViewer.getOutputDirectory() + "\\outputGraph.png";
        File toWrite = new File(imagePath);

        while (!isCompletelyWritten(toWrite)) ;

        Image image = new Image("file:" + imagePath);

        img.setImage(image);
    }

    private boolean isCompletelyWritten(File file) {
        RandomAccessFile stream = null;
        try {
            stream = new RandomAccessFile(file, "rw");
            return true;
        } catch (Exception e) {
            System.out.println("Skipping file " + file.getName() + " for this iteration due it's not completely written");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    System.out.println("Exception during closing file " + file.getName());
                }
            }
        }
        return false;
    }

    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(event -> initStartPage());
        MenuItem export = new MenuItem("Export");
        export.setOnAction(event -> exportImage());
        file.getItems().addAll(restart, export);


        Menu help = new Menu("Help");
        MenuItem howto = new MenuItem("How to use");
        MenuItem about = new MenuItem("About");

        howto.setOnAction(event -> {
            try {
                openWebpage(new URI("https://github.com/cbudo/UMLCreator"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Design Parser");
            alert.setHeaderText(null);
            alert.setContentText("Design Parser\nv7.627.374\nCreated by: Chris Budo and Benjamin Efron");
            alert.show();

        });
        help.getItems().addAll(howto, about);

        menuBar.getMenus().addAll(file, help);
        return menuBar;
    }

    private Map<String, Iterator<String>> getPatternClasses() {
        Map<String, Iterator<String>> patternClasses = new HashMap<>();
        if (parserViewer.getSingletons().hasNext())
            patternClasses.put("Singleton", parserViewer.getSingletons());
        if (parserViewer.getDecorators().hasNext())
            patternClasses.put("Decorators", parserViewer.getDecorators());
        if (parserViewer.getComponents().hasNext())
            patternClasses.put("Components", parserViewer.getComponents());
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

        initStartPage();
        //initResults();
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
