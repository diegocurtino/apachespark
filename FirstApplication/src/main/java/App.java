import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {

        // Create a connection to the specified cluster (of one machine in this case: local)
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        System.out.println("Running my 1st Spark App");

        // Load input data
        JavaRDD<String> input = sc.textFile(args[0]);

        //Split up into words
        JavaRDD<String> words = input.flatMap(s -> Arrays.asList(s.split(" ")));

        JavaPairRDD<String, Integer> counts = words.mapToPair(s -> new Tuple2(s, 1));
        counts = counts.reduceByKey((v1, v2) -> v1 + v2);

        String outputDirectory = args[1];
        System.out.println("Save RDD results into directory:" + outputDirectory);

        // For an explanation of what the files of the output are see
        // https://stackoverflow.com/questions/23898098/what-are-the-files-generated-by-spark-when-using-saveastextfile#23902886
        counts.saveAsTextFile(outputDirectory);

        //Close the connection.
        sc.close();
    }
}
