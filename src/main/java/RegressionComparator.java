import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.M5P;
import weka.core.Instances;
import javafx.scene.control.TextArea;
import java.util.Random;

public class RegressionComparator {

    public static void compare(Instances dataset, TextArea outputArea) throws Exception {
        // Prepare regressors
        LinearRegression lr = new LinearRegression();
        M5P m5p = new M5P();
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        // Evaluate each regressor
        Evaluation lrEval = evaluateRegressor(lr, dataset);
        Evaluation m5pEval = evaluateRegressor(m5p, dataset);
        Evaluation mlpEval = evaluateRegressor(mlp, dataset);

        // Display results
        outputArea.appendText("\n=== Linear Regression ===\n");
        outputArea.appendText(lrEval.toSummaryString() + "\n");

        outputArea.appendText("\n=== M5P Model Tree ===\n");
        outputArea.appendText(m5pEval.toSummaryString() + "\n");

        outputArea.appendText("\n=== Multilayer Perceptron ===\n");
        outputArea.appendText(mlpEval.toSummaryString() + "\n");
    }

    private static Evaluation evaluateRegressor(weka.classifiers.Classifier regressor, Instances dataset) throws Exception {
        Evaluation eval = new Evaluation(dataset);

        if (dataset.numInstances() < 10) {
            // For small datasets, use percentage split (80% train, 20% test)
            dataset.randomize(new Random(1));
            int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
            int testSize = dataset.numInstances() - trainSize;

            Instances train = new Instances(dataset, 0, trainSize);
            Instances test = new Instances(dataset, trainSize, testSize);

            regressor.buildClassifier(train);
            eval.evaluateModel(regressor, test);
        } else {
            // For larger datasets, use 10-fold CV
            eval.crossValidateModel(regressor, dataset, Math.min(10, dataset.numInstances()), new Random(1));
        }

        regressor.buildClassifier(dataset);
        return eval;
    }
}