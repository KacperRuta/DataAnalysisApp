import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import javafx.scene.control.TextArea;
import java.util.Random;

public class ClassificationComparator {

    public static void compare(Instances dataset, TextArea outputArea) throws Exception {
        // Prepare classifiers
        NaiveBayes nb = new NaiveBayes();
        J48 j48 = new J48();
        RandomForest rf = new RandomForest();
        Logistic logistic = new Logistic();

        // Evaluate each classifier
        Evaluation nbEval = evaluateClassifier(nb, dataset);
        Evaluation j48Eval = evaluateClassifier(j48, dataset);
        Evaluation rfEval = evaluateClassifier(rf, dataset);
        Evaluation logEval = evaluateClassifier(logistic, dataset);

        // Display results
        outputArea.appendText("\n=== Naive Bayes ===\n");
        outputArea.appendText(nbEval.toSummaryString() + "\n");
        outputArea.appendText(nbEval.toClassDetailsString() + "\n");
        outputArea.appendText(nbEval.toMatrixString() + "\n");

        outputArea.appendText("\n=== J48 Decision Tree ===\n");
        outputArea.appendText(j48Eval.toSummaryString() + "\n");
        outputArea.appendText(j48Eval.toClassDetailsString() + "\n");
        outputArea.appendText(j48Eval.toMatrixString() + "\n");

        outputArea.appendText("\n=== Random Forest ===\n");
        outputArea.appendText(rfEval.toSummaryString() + "\n");
        outputArea.appendText(rfEval.toClassDetailsString() + "\n");
        outputArea.appendText(rfEval.toMatrixString() + "\n");

        outputArea.appendText("\n=== Logistic Regression ===\n");
        outputArea.appendText(logEval.toSummaryString() + "\n");
        outputArea.appendText(logEval.toClassDetailsString() + "\n");
        outputArea.appendText(logEval.toMatrixString() + "\n");
    }

    private static Evaluation evaluateClassifier(weka.classifiers.Classifier classifier, Instances dataset) throws Exception {
        Evaluation eval = new Evaluation(dataset);

        if (dataset.numInstances() < 10) {
            // Better approach for small datasets - stratified split
            dataset.randomize(new Random(1));
            int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
            int testSize = dataset.numInstances() - trainSize;

            Instances train = new Instances(dataset, 0, trainSize);
            Instances test = new Instances(dataset, trainSize, testSize);

            // Ensure at least one instance of each class in training set
            if (train.numInstances() > 0 && test.numInstances() > 0) {
                classifier.buildClassifier(train);
                eval.evaluateModel(classifier, test);
            } else {
                // Fallback to leave-one-out if dataset is very small
                for (int i = 0; i < dataset.numInstances(); i++) {
                    train.delete(i);
                    classifier.buildClassifier(train);
                    eval.evaluateModel(classifier, test);
                }
            }
        } else {
            // Use 10-fold CV for larger datasets
            eval.crossValidateModel(classifier, dataset, 10, new Random(1));
        }

        // Build final model on full dataset for inspection
        classifier.buildClassifier(dataset);
        return eval;
    }
}
