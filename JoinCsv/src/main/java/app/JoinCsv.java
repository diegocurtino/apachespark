/**
 * Illustrates joining two csv files
 */
package app;

import com.opencsv.CSVReader;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class JoinCsv {
    private static final String RESOURCES_RELATIVE_PATH = "src/main/resources/";

    private static PairFunction<String, Integer, String[]> parseCsvLine() {
        return line -> {
            CSVReader reader = new CSVReader(new StringReader(line));
            String[] elements = reader.readNext();
            Integer key = Integer.parseInt(elements[0]);
            return new Tuple2(key, elements);
        };
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Usage JoinCsv csv1 csv2");
        }

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JoinCsvFiles");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> csvFile1 = sc.textFile(RESOURCES_RELATIVE_PATH + args[0]);
        JavaRDD<String> csvFile2 = sc.textFile(RESOURCES_RELATIVE_PATH + args[1]);

        JavaPairRDD<Integer, String[]> keyedRDD1 = csvFile1.mapToPair(parseCsvLine());
        JavaPairRDD<Integer, String[]> keyedRDD2 = csvFile2.mapToPair(parseCsvLine());

        keyedRDD1.foreach(e -> System.out.println("These are the values from the 1st CSV: " + Arrays.asList(e._2)));
        keyedRDD2.foreach(e -> System.out.println("These are the values from the 2st CSV: " + Arrays.asList(e._2)));

        JavaPairRDD<Integer, Tuple2<String[], String[]>> result = keyedRDD1.join(keyedRDD2);
        List<Tuple2<Integer, Tuple2<String[], String[]>>> resultCollection = result.collect();

        resultCollection.forEach(row -> System.out.println("Key " + row._1 + ":\t" + Arrays.asList(row._2._1) + "\t" + Arrays.asList(row._2._2)));
    }
}