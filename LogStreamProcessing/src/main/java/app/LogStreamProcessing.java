package app;

import internal.DefaultSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class LogStreamProcessing {

    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_PORT = 7777;

    public static void main(String args[]) throws InterruptedException {

        // Create a StreamingContext with a 1-second batch size from an already existing Spark context.
        JavaStreamingContext jssc = new JavaStreamingContext(DefaultSparkContext.INSTANCE.getContext(), Durations.seconds(1));

        // Create a DStream from all the input on port 7777
        JavaDStream<String> lines = jssc.socketTextStream(DEFAULT_HOSTNAME, DEFAULT_PORT);

        // Search for the specified word OR use the default one (error).
        String wordSearched = args.length == 1 ? args[0] : "error";

        // Filter our DStream for lines with "error"
        JavaDStream<String> errorLines = lines.filter(line -> line.contains(wordSearched));

        // Print out the lines with errors
        errorLines.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
