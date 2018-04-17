
package com.example.krzys.quizapp.data.model.quiz;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlagAnswer implements Parcelable
{

    @SerializedName("B")
    @Expose
    private Integer b;
    @SerializedName("C")
    @Expose
    private Integer c;
    @SerializedName("E")
    @Expose
    private Integer e;
    @SerializedName("A")
    @Expose
    private Integer a;
    @SerializedName("F")
    @Expose
    private Integer f;
    @SerializedName("D")
    @Expose
    private Integer d;
    public final static Creator<FlagAnswer> CREATOR = new Creator<FlagAnswer>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FlagAnswer createFromParcel(Parcel in) {
            return new FlagAnswer(in);
        }

        public FlagAnswer[] newArray(int size) {
            return (new FlagAnswer[size]);
        }

    }
    ;

    protected FlagAnswer(Parcel in) {
        this.b = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.c = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.e = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.a = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.f = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.d = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public FlagAnswer() {
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public Integer getC() {
        return c;
    }

    public void setC(Integer c) {
        this.c = c;
    }

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    public Integer getD() {
        return d;
    }

    public void setD(Integer d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "a: " + a + "; b: " + b + "; c: " + c + "; d: " +
                d + "; e: " + e;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(b);
        dest.writeValue(c);
        dest.writeValue(e);
        dest.writeValue(a);
        dest.writeValue(f);
        dest.writeValue(d);
    }

    public int describeContents() {
        return  0;
    }

}
