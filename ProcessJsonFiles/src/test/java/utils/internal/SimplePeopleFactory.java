package utils.internal;

import app.internal.Person;

import java.util.Arrays;
import java.util.List;

public class SimplePeopleFactory {

    public static List<Person> createListOfPeople() {
        Person personA = new Person();
        personA.setName("DummyA");
        personA.setNationality("Elbonian");
        personA.setNicknames(Arrays.asList("DA", "DummyA"));

        Person personB = new Person();
        personB.setName("DummyB");
        personB.setNationality("Elbonian");
        personB.setNicknames(Arrays.asList("DB", "DummyB"));

        Person personC = new Person();
        personC.setName("DummyC");
        personC.setNationality("Elbonian");
        personC.setNicknames(Arrays.asList("DC", "DummyC"));

        Person personD = new Person();
        personD.setName("DummyD");
        personD.setNationality("Elbonian");
        personD.setNicknames(Arrays.asList("DD", "DummyD"));

        Person personE = new Person();
        personE.setName("DummyE");
        personE.setNationality("South Elbonian");
        personE.setNicknames(Arrays.asList("DE", "DummyE"));

        personA.setSiblings(Arrays.asList(personB, personC));
        personB.setSiblings(Arrays.asList(personA));
        personC.setSiblings(Arrays.asList(personA));

        return Arrays.asList(personA, personB, personC, personD, personE);
    }
}