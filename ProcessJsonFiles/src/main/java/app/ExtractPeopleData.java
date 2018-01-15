package app;

import app.internal.Average;
import app.internal.DefaultSparkContext;
import app.internal.PeopleRdd;
import app.transformationfunctions.PeopleDatasetFunctions;
import app.utils.Validators;
import app.utils.filesystem.FileSystemOperations;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.IOException;
import java.util.Scanner;

public class ExtractPeopleData {
    public static final String MODULE_NAME = ExtractPeopleData.class.getSimpleName();

    public static void main(String[] args) throws IOException {
        validateParameters(args);

        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;

        while (!isValid) {
            printUserOptions();

            String userInput = scanner.nextLine();
            if (!Validators.isNumber(userInput)) {
                printInvalidInputMessage(userInput);
                continue;
            }

            switch (Integer.parseInt(userInput)) {
                case 1:
                    extractCountPerNationality(args[0], args[1]);
                    isValid = true;
                    break;
                case 2:
                    extractCountSiblingsPerPerson(args[0], args[1]);
                    isValid = true;
                    break;
                case 3:
                    calculateGeneralAverageOfSiblings(args[0], args[1]);
                    isValid = true;
                    break;

                default:
                    printInvalidInputMessage(userInput);
            }
        }
        scanner.close();
        System.exit(0);
    }

    private static void validateParameters(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Usage: " + MODULE_NAME + " inputFileName outputDirectory");
        }

        if (args[0].isEmpty()) {
            throw new RuntimeException("inputFileName cannot be empty");
        }

        if (args[1].isEmpty()) {
            throw new RuntimeException("outputDirectory cannot be empty");
        }
    }

    private static void printUserOptions() {
        System.out.println("\nWhat do you want to do with people's data set?");
        System.out.println("----------------------------------------------");
        System.out.println("1. Extract number of people per nationality");
        System.out.println("2. Extract number of siblings each person has");
        System.out.println("3. Calculate average of siblings");
    }

    private static void printInvalidInputMessage(String selection) {
        System.out.println(selection + " is not valid option.\n");
    }

    private static void extractCountPerNationality(String inputFile, String resultsDirectory) {
        PeopleRdd peopleRdd = new PeopleRdd(inputFile, DefaultSparkContext.INSTANCE.getContext());
        JavaPairRDD<String, Integer> results = PeopleDatasetFunctions.getCountPerNationality(peopleRdd.getRdd());
        FileSystemOperations.saveResults(results, resultsDirectory);
    }

    private static void extractCountSiblingsPerPerson(String inputFile, String resultsDirectory) {
        PeopleRdd peopleRdd = new PeopleRdd(inputFile, DefaultSparkContext.INSTANCE.getContext());
        JavaPairRDD<String, Integer> results = PeopleDatasetFunctions.getSiblingsCount(peopleRdd.getRdd());
        FileSystemOperations.saveResults(results, resultsDirectory);
    }

    private static void calculateGeneralAverageOfSiblings(String inputFile, String resultsDirectory) throws IOException {
        PeopleRdd peopleRdd = new PeopleRdd(inputFile, DefaultSparkContext.INSTANCE.getContext());
        Average average = PeopleDatasetFunctions.getAverageOfSiblings(peopleRdd.getRdd());
        FileSystemOperations.saveResults(average, resultsDirectory);
    }
}