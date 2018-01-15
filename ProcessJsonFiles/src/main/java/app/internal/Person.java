package app.internal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

// Class must be serializable to avoid Serialization exceptions when executing Spark's actions.
// @JsonIdentityInfo is required to allow Jackson to serialize recursive relationships (person -> siblings = person).
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Person implements Serializable {

    private String name;
    private String nationality;
    private List<String> nicknames;
    private List<Person> siblings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public Optional<List<Person>> getSiblings() {
        return Optional.ofNullable(siblings);
    }

    public void setSiblings(List<Person> siblings) {
        this.siblings = siblings;
    }
}
