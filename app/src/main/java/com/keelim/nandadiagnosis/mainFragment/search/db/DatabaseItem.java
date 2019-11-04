package com.keelim.nandadiagnosis.mainFragment.search.db;

import android.os.Parcel;
import android.os.Parcelable;

public class DatabaseItem implements Parcelable {
    private int primaryKey;
    private String reason;
    private String diagnosis;
    private String class_name;
    private String domain_name;


    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.primaryKey);
        dest.writeString(this.reason);
        dest.writeString(this.diagnosis);
        dest.writeString(this.class_name);
        dest.writeString(this.domain_name);
    }

    public DatabaseItem() {
    }

    protected DatabaseItem(Parcel in) {
        this.primaryKey = in.readInt();
        this.reason = in.readString();
        this.diagnosis = in.readString();
        this.class_name = in.readString();
        this.domain_name = in.readString();
    }

    public static final Creator<DatabaseItem> CREATOR = new Creator<DatabaseItem>() {
        @Override
        public DatabaseItem createFromParcel(Parcel source) {
            return new DatabaseItem(source);
        }

        @Override
        public DatabaseItem[] newArray(int size) {
            return new DatabaseItem[size];
        }
    };
}
