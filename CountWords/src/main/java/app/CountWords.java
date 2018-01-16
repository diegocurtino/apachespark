package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import utils.filesystem.FileSystemOperations;

import java.util.Arrays;

public class CountWords {

    public static final String MODULE_NAME = CountWords.class.getSimpleName();
    private static final Logger LOGGER = LogManager.getLogger(CountWords.class);

    public static void main(String[] args) {
        validateParameters(args);

        // Create a connection to the specified cluster (of one machine in this case: local)
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        String inputFileName = args[0];
        LOGGER.info("Counting words from {} file", inputFileName);

        // Load input data
        JavaRDD<String> input = sc.textFile(inputFileName);

        //Split up into words
        JavaRDD<String> words = input.flatMap(s -> Arrays.asList(s.split(" ")));

        // Count every word
        JavaPairRDD<String, Integer> wordCount = words.mapToPair(s -> new Tuple2(s, 1));

        // Look for duplicated words and update word's total count.
        wordCount = wordCount.reduceByKey((v1, v2) -> v1 + v2);

        FileSystemOperations.saveResults(wordCount, args[1]);
        sc.close();
    }

    private static void validateParameters(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Usage: " + MODULE_NAME + " <inputFileName> <outputDirectory>");
        }
    }
}
