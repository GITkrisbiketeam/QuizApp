
package com.example.krzys.quizapp.data.dto.quiz;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlagAnswer implements Parcelable
{
    @SerializedName("A")
    @Expose
    private Integer a;
    @SerializedName("B")
    @Expose
    private Integer b;
    @SerializedName("C")
    @Expose
    private Integer c;
    @SerializedName("D")
    @Expose
    private Integer d;
    @SerializedName("E")
    @Expose
    private Integer e;
    @SerializedName("F")
    @Expose
    private Integer f;
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
        this.a = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.b = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.c = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.d = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.e = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.f = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public FlagAnswer() {
    }

    public FlagAnswer(Integer a, Integer b, Integer c, Integer d, Integer e, Integer f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
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

    public Integer getD() {
        return d;
    }

    public void setD(Integer d) {
        this.d = d;
    }

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "a: " + a + "; b: " + b + "; c: " + c + "; d: " +
                d + "; e: " + e;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(a);
        dest.writeValue(b);
        dest.writeValue(c);
        dest.writeValue(d);
        dest.writeValue(e);
        dest.writeValue(f);
    }

    public int describeContents() {
        return  0;
    }

}
