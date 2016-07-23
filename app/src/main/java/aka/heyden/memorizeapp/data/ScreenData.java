package aka.heyden.memorizeapp.data;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenData {
    public ScreenData() {
    }

    private String word = "";
    private String diction = "";
    private String mean = "";
    private ExampleSetence exampleList;

    public class ExampleSetence {
        ArrayList<Pair<String, String>> list;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDiction() {
        return diction;
    }

    public void setDiction(String diction) {
        this.diction = diction;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public ExampleSetence getExampleList() {
        return exampleList;
    }

    public void setExampleList(ExampleSetence exampleList) {
        this.exampleList = exampleList;
    }
}

