package aka.heyden.memorizeapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenData implements Parcelable {
    public ScreenData() {
    }

    private String word = "";
    private String diction = "";
    private String mean = "";
    // private ArrayList<Pair<String, String>> exampleList;

    public ScreenData(Parcel src) {
        word = src.readString();
        diction = src.readString();
        mean = src.readString();
       // exampleList = src.readArrayList(Pair.class.getClassLoader());
    }

    public static final Creator<ScreenData> CREATOR = new Creator<ScreenData>() {
        @Override
        public ScreenData createFromParcel(Parcel in) {
            return new ScreenData(in);
        }

        @Override
        public ScreenData[] newArray(int size) {
            return new ScreenData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.word);
        dest.writeString(this.diction);
        dest.writeString(this.mean);
        // dest.writeArray(this.exampleList);
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

    /*public Pair<String, String>[] getExampleList() {
        return exampleList;
    }

    public void setExampleList(Pair<String, String>[] exampleList) {
        this.exampleList = exampleList;
    }*/
}

