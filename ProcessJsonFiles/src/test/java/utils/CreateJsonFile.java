package utils;

import app.internal.PeopleRdd;
import app.internal.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.internal.FactoryType;
import utils.internal.MinimalPeopleFactory;
import utils.internal.SimplePeopleFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CreateJsonFile {

    private static final Logger LOGGER = LogManager.getLogger(CreateJsonFile.class);
    private static final String MODULE_NAME = CreateJsonFile.class.getSimpleName();

    public static void main(String[] args) {
        validateInputValues(args);

        boolean shouldExportOneRecordPerLine = Boolean.parseBoolean(args[1]);
        String filenameSuffix = shouldExportOneRecordPerLine ? "singleLine" : "multiLine";

        ObjectMapper mapper = setupJsonMapper(shouldExportOneRecordPerLine);

        LOGGER.info("Json records will be exported {} format", shouldExportOneRecordPerLine
                ? "as a single-line blob" : "with indentation and new line characters");

        FactoryType factoryType = FactoryType.valueOf(args[0].toUpperCase()); // Probably a bit too much, but I wanted to use Enums internally.
        String fullyQualifiedOutputFilename = getResourcesAbsolutePath() + factoryType.name().toLowerCase() + "_people_" + filenameSuffix + ".json";

        try {
            // Convert object to JSON string and save it in the specified file and format.
            mapper.writeValue(new File(fullyQualifiedOutputFilename), getPeopleInfo(factoryType));
            LOGGER.debug("Data exported to {}", fullyQualifiedOutputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectMapper setupJsonMapper(boolean shouldExportOneRecordPerLine) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module()); // Added to serialize Optional fields' content.

        if (!shouldExportOneRecordPerLine) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return mapper;
    }

    private static void validateInputValues(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Usage: " + MODULE_NAME + " factoryName exportOneRecordPerLine");
        }

        if (FactoryType.valueOf(args[0].toUpperCase()) == null) {
            throw new RuntimeException("The specified factory does not exist");
        }
    }

    private static String getResourcesAbsolutePath() {
        return Paths.get("").toAbsolutePath().toString() + "/" + PeopleRdd.MODULE_NAME + "/src/test/resources/";
    }

    private static List<Person> getPeopleInfo(FactoryType factoryType) {
        switch (factoryType) {
            case MINIMAL:
                return MinimalPeopleFactory.createListOfPeople();
            case SIMPLE:
                return SimplePeopleFactory.createListOfPeople();
            default:
                return null;
        }
    }
}