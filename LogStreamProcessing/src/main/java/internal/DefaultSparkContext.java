package internal;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public enum DefaultSparkContext {
    INSTANCE;

    // Create a local StreamingContext with two working threads
    private static final SparkConf SPARK_CONFIGURATION = new SparkConf().setMaster("local[2]").setAppName("Any");
    private static final JavaSparkContext JAVA_SPARK_CONTEXT = new JavaSparkContext(SPARK_CONFIGURATION);

    public JavaSparkContext getContext() {
        return JAVA_SPARK_CONTEXT;
    }
}
