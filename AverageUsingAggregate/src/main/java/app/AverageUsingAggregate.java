package app;

import java.util.Arrays;

import internal.Average;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

public class AverageUsingAggregate {

    public static void main(String[] args) {

        // Create a connection to the specified cluster (of one machine in this case: local)
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("CalculateAverageUsingAggregate");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        // Creates an RDD and loads it completely in memory (use only when testing/trying; never in production)
        JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4));

        Average initialValue = new Average(0, 0);
        // Example of aggregate function usage. Parameter's explanation:
        // 1st: both the initial value and the result's type of value.
        // 2nd: function to update the local aggregator with each new value read from the local RDD partition.
        // 3rd: function to merge workers' aggregator.
        Average result = rdd.aggregate(initialValue, combineRddElementsWithAccumulator(), mergeWorkerAggregators());
        System.out.println("The average is: " + result.getAverage());
        sc.stop();
    }

    private static Function2<Average, Integer, Average> combineRddElementsWithAccumulator() {
        return (Function2<Average, Integer, Average>) (average, currentValue) -> {
            average.totalSum += currentValue;
            average.numberOfElements += 1;
            return average;
        };
    }

    private static Function2<Average, Average, Average> mergeWorkerAggregators() {
        return (Function2<Average, Average, Average>) (a, b) -> {
            a.totalSum += b.totalSum;
            a.numberOfElements += b.numberOfElements;
            return a;
        };
    }
}