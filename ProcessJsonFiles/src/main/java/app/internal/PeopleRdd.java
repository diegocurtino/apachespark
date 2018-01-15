package app.internal;

import app.utils.LogHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class PeopleRdd implements Serializable {

    public static final String MODULE_NAME = PeopleRdd.class.getSimpleName();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LogManager.getLogger(PeopleRdd.class);
    private JavaRDD<Person> peopleRecordsFromJson;

    public PeopleRdd(String jsonFile, JavaSparkContext sparkContext) {
        LOGGER.info("Spark will process {} file ", jsonFile);

        JavaRDD<String> input = sparkContext.textFile(jsonFile);
        peopleRecordsFromJson = input.mapPartitions(i -> Arrays.asList(JSON_MAPPER.readValue(new File(jsonFile), Person[].class)).iterator());

        if (LOGGER.isDebugEnabled()) {
            LogHelper.logPeopleInfo(LOGGER, peopleRecordsFromJson);
        }
    }

    public JavaRDD<Person> getRdd() {
        return peopleRecordsFromJson;
    }
}