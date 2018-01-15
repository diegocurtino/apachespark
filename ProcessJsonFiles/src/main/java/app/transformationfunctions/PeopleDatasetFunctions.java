package app.transformationfunctions;

import app.internal.Average;
import app.internal.Person;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PeopleDatasetFunctions {
    public static JavaPairRDD<String, Integer> getCountPerNationality(JavaRDD<Person> peopleRDD) {
        JavaPairRDD<String, Integer> peopleCount = peopleRDD.mapToPair(p -> new Tuple2<>(p.getNationality(), 1));
        return peopleCount.reduceByKey((x, y) -> x + y);
    }

    public static JavaPairRDD<String, Integer> getSiblingsCount(JavaRDD<Person> peopleRDD) {
        JavaPairRDD<String, Integer> peopleCount = peopleRDD.mapToPair(p -> {
            int count = !p.getSiblings().isPresent() ? 0 : p.getSiblings().get().size();
            return new Tuple2<>(p.getName(), count);
        });
        return peopleCount.reduceByKey((x, y) -> x + y);
    }

    public static Average getAverageOfSiblings(JavaRDD<Person> peopleRDD) {
        JavaRDD<Average> averageRdd = peopleRDD.mapPartitions(combineRddElementsWithAccumulatorAndReturnIterator());
        return averageRdd.reduce(mergeAccumulators());
    }

    private static FlatMapFunction<Iterator<Person>, Average> combineRddElementsWithAccumulatorAndReturnIterator() {
        return (FlatMapFunction<Iterator<Person>, Average>) personIterator -> {

            Average newAverage = new Average(0, 0);
            while (personIterator.hasNext()) {
                Optional<List<Person>> siblings = personIterator.next().getSiblings();
                newAverage.totalSum += !siblings.isPresent() ? 0 : siblings.get().size();
                newAverage.count += 1;
            }

            ArrayList<Average> iterableOfAverages = new ArrayList<>();
            iterableOfAverages.add(newAverage);
            return iterableOfAverages.iterator();
        };
    }

    private static Function2<Average, Average, Average> mergeAccumulators() {
        return (Function2<Average, Average, Average>) (Average a, Average b) -> {
            a.totalSum += b.totalSum;
            a.count += b.count;
            return a;
        };
    }
}
