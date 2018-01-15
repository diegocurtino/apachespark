package internal;

import java.io.Serializable;

public class Average implements Serializable {
    public int totalSum;
    public int numberOfElements;

    public Average(int total, int num) {
        totalSum = total;
        numberOfElements = num;
    }

    public float getAverage() {
        return (float) totalSum / numberOfElements;
    }
}
