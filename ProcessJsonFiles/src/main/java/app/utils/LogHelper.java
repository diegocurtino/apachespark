package app.utils;

import app.internal.Person;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;

public class LogHelper {

    public static void logPeopleInfo(Logger logger, JavaRDD<Person> result) {
        result.foreach(p -> logger.debug("\nName: " + p.getName() +
                "\nNationality: " + p.getNationality() +
                "\nNicknames: " + p.getNicknames() +
                "\nSiblings:\n " + logSiblingsInfo(p) + "\n"));
    }

    private static String logSiblingsInfo(Person p) {
        if (!p.getSiblings().isPresent()) {
            return "\tAmount: 0";
        }

        StringBuilder info = new StringBuilder();

        List<Person> siblings = p.getSiblings().get();
        info.append("\tAmount: " + siblings.size() + "\n");

        for (Person s : siblings) {
            info.append("\tName: " + s.getName() + "\n");
        }
        return info.toString();
    }
}
