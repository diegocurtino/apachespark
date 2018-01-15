package utils.internal;

import app.internal.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinimalPeopleFactory {

    public static List<Person> createListOfPeople() {
        Person personA = new Person();
        personA.setName("DummyA");
        personA.setNationality("Elbonian");
        personA.setNicknames(Arrays.asList("DA", "DummyA"));

        return Arrays.asList(personA);
    }
}
