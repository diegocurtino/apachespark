package app.internal;

import java.io.Serializable;

public class Average implements Serializable, Reducible {
    public int totalSum;
    public int count;

    public Average(int sum, int count) {
        totalSum = sum;
        this.count = count;
    }

    public float get() {
        return (float) totalSum / count;
    }

    @Override
    public String getValueToPersist() {
        return "(Average, " + get() + ")";
    }
}
