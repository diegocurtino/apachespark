/**
 * Illustrates a simple map partitions in Java to compute the average
 */
package app;

import internal.Average;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class AverageUsingPartitions {

    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("CalculateAverageUsingPartitions");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));

        Average result = rdd.mapPartitions(combineRddElementsWithAccumulatorAndReturnIterator()).reduce(mergeAccumulators());
        System.out.println("Average calculated using partitions is: " + result.getAverage());
    }

    private static FlatMapFunction<Iterator<Integer>, Average> combineRddElementsWithAccumulatorAndReturnIterator() {
        return (FlatMapFunction<Iterator<Integer>, Average>) input -> {

            Average newAverage = new Average(0, 0);
            while (input.hasNext()) {
                newAverage.totalSum += input.next();
                newAverage.numberOfElements += 1;
            }

            ArrayList<Average> iterableOfAverages = new ArrayList<>();
            iterableOfAverages.add(newAverage);
            return iterableOfAverages;
        };
    }

    private static Function2<Average, Average, Average> mergeAccumulators() {
        return (Function2<Average, Average, Average>) (Average a, Average b) -> {
            a.totalSum += b.totalSum;
            a.numberOfElements += b.numberOfElements;
            return a;
        };
    }
}