package app;

import app.internal.Average;
import app.internal.DefaultSparkContext;
import app.internal.PeopleRdd;
import app.transformationfunctions.PeopleDatasetFunctions;
import com.holdenkarau.spark.testing.JavaRDDComparisons;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import scala.Tuple2;
import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The tests use Spark Testing Base's framework. The code to create expectedRDDs and compare results is taken from:
 * http://www.jesse-anderson.com/2016/04/unit-testing-spark-with-java/
 */
public class PeopleRddTransformationTest {

    private static final Logger LOGGER = LogManager.getLogger(PeopleRddTransformationTest.class);
    private static final ClassLoader CLASS_LOADER = PeopleRddTransformationTest.class.getClassLoader();
    private static JavaSparkContext sparkContext;

    @BeforeAll
    static void initSpark() {
        LOGGER.info("Starting and setting up Spark: default context, localhost...");
        sparkContext = DefaultSparkContext.INSTANCE.getContext();
    }

    @AfterAll
    static void closeSparkConnection() {
        LOGGER.info("Stopping Spark...");
        sparkContext.stop();
    }

    @DisplayName("Verify mapping per nationality")
    @ParameterizedTest
    @MethodSource("mappingPerNationalityProvider")
    void mappingPerNationalityProvider(String fileName, List<Tuple2<String, Integer>> expectedResults) {

        PeopleRdd rddFromJsonFile = createPeopleRddFromJsonFile(fileName);
        JavaPairRDD<String, Integer> result = PeopleDatasetFunctions.getCountPerNationality(rddFromJsonFile.getRdd());

        // Create expected RDD.
        JavaPairRDD<String, Integer> expectedRdd = sparkContext.parallelizePairs(expectedResults);
        ClassTag<Tuple2<String, Integer>> tag = ClassTag$.MODULE$.apply(Tuple2.class);

        // Compare actual vs expected results.
        JavaRDDComparisons.assertRDDEquals(
                JavaRDD.fromRDD(JavaPairRDD.toRDD(result), tag),
                JavaRDD.fromRDD(JavaPairRDD.toRDD(expectedRdd), tag)
        );
    }

    private static Stream<Arguments> mappingPerNationalityProvider() {
        return Stream.of(
                Arguments.of("minimal_people_singleLine.json", Arrays.asList(new Tuple2<>("Elbonian", 1))),
                Arguments.of("simple_people_singleLine.json", Arrays.asList(
                        new Tuple2<>("Elbonian", 4),
                        new Tuple2<>("South Elbonian", 1))
                )
        );
    }

    private PeopleRdd createPeopleRddFromJsonFile(String fileName) {
        String inputFileName = new File(CLASS_LOADER.getResource(fileName).getFile()).getAbsolutePath();
        return new PeopleRdd(inputFileName, sparkContext);
    }

    @DisplayName("Verify count of siblings")
    @ParameterizedTest
    @MethodSource("siblingsCountProvider")
    void verifySiblingsCount(String fileName, List<Tuple2<String, Integer>> expectedResults) {

        PeopleRdd rddFromJsonFile = createPeopleRddFromJsonFile(fileName);
        JavaPairRDD<String, Integer> result = PeopleDatasetFunctions.getSiblingsCount(rddFromJsonFile.getRdd());

        // Create expected RDD.
        JavaPairRDD<String, Integer> expectedRdd = sparkContext.parallelizePairs(expectedResults);
        ClassTag<Tuple2<String, Integer>> tag = ClassTag$.MODULE$.apply(Tuple2.class);

        // Compare actual vs expected results.
        JavaRDDComparisons.assertRDDEquals(
                JavaRDD.fromRDD(JavaPairRDD.toRDD(result), tag),
                JavaRDD.fromRDD(JavaPairRDD.toRDD(expectedRdd), tag)
        );
    }

    private static Stream<Arguments> siblingsCountProvider() {
        return Stream.of(
                Arguments.of("minimal_people_singleLine.json", Arrays.asList(new Tuple2<>("DummyA", 0))),
                Arguments.of("simple_people_singleLine.json", Arrays.asList(
                        new Tuple2<>("DummyA", 2),
                        new Tuple2<>("DummyB", 1),
                        new Tuple2<>("DummyC", 1),
                        new Tuple2<>("DummyD", 0),
                        new Tuple2<>("DummyE", 0))
                )
        );
    }

    @DisplayName("Verify people's average of siblings")
    @ParameterizedTest
    @MethodSource("siblingsAverageProvider")
    void verifyAverageOfSiblings(String fileName, float expectedAverage) {
        PeopleRdd rddFromJsonFile = createPeopleRddFromJsonFile(fileName);
        Average average = PeopleDatasetFunctions.getAverageOfSiblings(rddFromJsonFile.getRdd());

        // Delta taken from https://stackoverflow.com/questions/31776127/how-to-test-for-equality-of-float-double-in-junit#31776144
        assertEquals(average.get(), expectedAverage, 5.96e-08);
    }

    private static Stream<Arguments> siblingsAverageProvider() {
        return Stream.of(
                Arguments.of("minimal_people_singleLine.json", (float) 0),
                Arguments.of("simple_people_singleLine.json", (float) 0.8));
    }
}