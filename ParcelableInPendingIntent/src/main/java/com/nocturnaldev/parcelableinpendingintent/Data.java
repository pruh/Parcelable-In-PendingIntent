package com.nocturnaldev.parcelableinpendingintent;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private int mInt;
    private String mStr;

    public static final Parcelable.Creator<Data> CREATOR
            = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data() { }

    private Data(Parcel in) {
        mInt = in.readInt();
        mStr = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mInt);
        out.writeString(mStr);
    }

    public int getInt() {
        return mInt;
    }

    public void setInt(int Int) {
        this.mInt = Int;
    }

    public String getStr() {
        return mStr;
    }

    public void setStr(String Str) {
        this.mStr = Str;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Data{");
        sb.append("mInt=").append(mInt);
        sb.append(", mStr='").append(mStr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
