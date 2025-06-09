# WEKA Methods Comparison Tool

A JavaFX application for comparing classification and regression algorithms from the WEKA machine learning library.


## Features

- Compare multiple classification algorithms (Naive Bayes, J48, Random Forest, Logistic Regression)
- Compare multiple regression algorithms (Linear Regression, M5P, Neural Network)
- Visualize comparison results with bar charts
- Load datasets in ARFF or CSV format
- Simple and intuitive user interface

## Requirements

- Java 21 JDK or later
- Maven 3.6.0 or later

## Installation

1. Clone the repository
2. Build the project with Maven

## Running the Application

Run the WekaComparisonApp class directly from your IDE or terminal

## Usage

1. Load a Dataset:
   
2. Click "File" → "Open Dataset"

3. Select an ARFF or CSV file containing your data
The application will automatically detect the class attribute

4. Select Analysis Mode:
Choose "Classification" for classification problems
Choose "Regression" for regression problems

5. Compare Methods:
Click "Compare Methods" to run the analysis
Results will appear in the output area below

6. Visualize Results:
Click "Visualize Results" to see a bar chart comparison
Classification shows accuracy percentages
Regression shows RMSE values (lower is better)

## Sample Datasets

The application works with standard WEKA dataset formats. Example datasets:

Classification (iris.arff):

@relation iris

@attribute sepallength numeric
@attribute sepalwidth numeric
@attribute petallength numeric
@attribute petalwidth numeric
@attribute class {Iris-setosa,Iris-versicolor,Iris-virginica}

@data
5.1,3.5,1.4,0.2,Iris-setosa
4.9,3.0,1.4,0.2,Iris-setosa
...



Regression (housing.arff):

@relation housing

@attribute house_size numeric
@attribute bedrooms numeric
@attribute age numeric
@attribute price numeric

@data
2100,3,15,399900
1600,3,40,329900
...

## Troubleshooting

1. "Can't have more folds than instances!"
Your dataset is too small for 10-fold cross-validation
The application automatically switches to an 80/20 train-test split for small datasets
For meaningful results, use datasets with at least 10-15 instances

2. JavaFX runtime components missing
Make sure you're using Java 21+ and have the JavaFX dependencies in your pom.xml

