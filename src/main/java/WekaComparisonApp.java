import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaComparisonApp extends Application {

    private TextArea outputArea;
    private Instances dataset;
    private String currentMode = "Classification"; // or "Regression"

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WEKA Methods Comparison Tool");

        // Create menu
        MenuBar menuBar = createMenuBar(primaryStage);

        // Create mode selection
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton classificationBtn = new RadioButton("Classification");
        classificationBtn.setToggleGroup(modeGroup);
        classificationBtn.setSelected(true);
        classificationBtn.setOnAction(e -> currentMode = "Classification");

        RadioButton regressionBtn = new RadioButton("Regression");
        regressionBtn.setToggleGroup(modeGroup);
        regressionBtn.setOnAction(e -> currentMode = "Regression");

        HBox modeBox = new HBox(10, classificationBtn, regressionBtn);
        modeBox.setPadding(new javafx.geometry.Insets(10));

        // Create buttons for analysis
        Button compareBtn = new Button("Compare Methods");
        compareBtn.setOnAction(e -> compareMethods());

        Button visualizeBtn = new Button("Visualize Results");
        visualizeBtn.setOnAction(e -> visualizeResults());

        HBox buttonBox = new HBox(10, compareBtn, visualizeBtn);
        buttonBox.setPadding(new javafx.geometry.Insets(10));

        // Create output area - now with VBox.setVgrow priority
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // Create a container for the controls
        VBox controlsContainer = new VBox(10, menuBar, modeBox, buttonBox);

        // Create main layout with BorderPane for better space management
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(controlsContainer);
        mainLayout.setCenter(outputArea);

        // Make the output area expand to fill available space
        BorderPane.setMargin(outputArea, new javafx.geometry.Insets(0, 10, 10, 10));
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        // Set scene with minimum size
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar(Stage stage) {
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open Dataset");
        openItem.setOnAction(e -> openFile(stage));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(openItem, new SeparatorMenuItem(), exitItem);

        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());

        helpMenu.getItems().add(aboutItem);

        // Create menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Dataset");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ARFF Files", "*.arff"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                DataSource source = new DataSource(selectedFile.getAbsolutePath());
                dataset = source.getDataSet();
                if (dataset.classIndex() == -1) {
                    dataset.setClassIndex(dataset.numAttributes() - 1);
                }
                outputArea.appendText("Loaded dataset: " + selectedFile.getName() + "\n");
                outputArea.appendText("Number of instances: " + dataset.numInstances() + "\n");
                outputArea.appendText("Number of attributes: " + dataset.numAttributes() + "\n");
                outputArea.appendText("Class attribute: " + dataset.classAttribute().name() + "\n");
            } catch (Exception e) {
                outputArea.appendText("Error loading file: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    private void compareMethods() {
        if (dataset == null) {
            outputArea.appendText("Please load a dataset first.\n");
            return;
        }

        outputArea.appendText("\n=== Comparing " + currentMode + " Methods ===\n");

        try {
            if (currentMode.equals("Classification")) {
                ClassificationComparator.compare(dataset, outputArea);
            } else {
                RegressionComparator.compare(dataset, outputArea);
            }
        } catch (Exception e) {
            outputArea.appendText("Error during comparison: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void visualizeResults() {
        if (dataset == null) {
            outputArea.appendText("Please load a dataset and run comparison first.\n");
            return;
        }

        try {
            if (currentMode.equals("Classification")) {
                Visualization.showClassificationResults(dataset);
            } else {
                Visualization.showRegressionResults(dataset);
            }
        } catch (Exception e) {
            outputArea.appendText("Error during visualization: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("WEKA Methods Comparison Tool");
        alert.setContentText("This application compares different classification and regression methods from WEKA.\n\n" +
                "Load your dataset, select analysis mode, and compare the results.");
        alert.showAndWait();
    }
}