import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Attribute;
import weka.classifiers.Evaluation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.M5P;
import java.util.Random;

public class Visualization {

    public static void showClassificationResults(Instances dataset) throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Classification Results Visualization");

            // Prepare classifiers
            Classifier[] classifiers = {
                    new NaiveBayes(),
                    new J48(),
                    new RandomForest(),
                    new Logistic()
            };

            String[] classifierNames = {"Naive Bayes", "J48", "Random Forest", "Logistic Regression"};
            double[] accuracies = new double[classifiers.length];

            try {
                for (int i = 0; i < classifiers.length; i++) {
                    Evaluation eval = new Evaluation(dataset);
                    eval.crossValidateModel(classifiers[i], dataset, 10, new Random(1));
                    accuracies[i] = eval.pctCorrect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // Create chart
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Classifier");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Accuracy (%)");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Classifier Comparison");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Accuracy");

            for (int i = 0; i < classifiers.length; i++) {
                series.getData().add(new XYChart.Data<>(classifierNames[i], accuracies[i]));
            }

            barChart.getData().add(series);

            Scene scene = new Scene(barChart, 800, 600);
            stage.setScene(scene);
            stage.show();
        });
    }

    public static void showRegressionResults(Instances dataset) throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Regression Results Visualization");

            // Prepare regressors
            Classifier[] regressors = {
                    new LinearRegression(),
                    new M5P(),
                    new MultilayerPerceptron()
            };

            String[] regressorNames = {"Linear Regression", "M5P", "Neural Network"};
            double[] rmseValues = new double[regressors.length];

            try {
                for (int i = 0; i < regressors.length; i++) {
                    Evaluation eval = new Evaluation(dataset);
                    eval.crossValidateModel(regressors[i], dataset, 10, new Random(1));
                    rmseValues[i] = eval.rootMeanSquaredError();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // Create chart
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Regressor");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("RMSE");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Regressor Comparison (Lower RMSE is better)");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("RMSE");

            for (int i = 0; i < regressors.length; i++) {
                series.getData().add(new XYChart.Data<>(regressorNames[i], rmseValues[i]));
            }

            barChart.getData().add(series);

            Scene scene = new Scene(barChart, 800, 600);
            stage.setScene(scene);
            stage.show();
        });
    }
}