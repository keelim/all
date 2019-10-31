package com.keelim.nandadiagnosis.mainFragment.search.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DbItem implements Parcelable {
    private int nanda_mysql_id;
    private String reason;
    private String diagnosis;
    private String class_name;
    private String domain_name;


    public int getNanda_mysql_id() {
        return nanda_mysql_id;
    }

    public void setNanda_mysql_id(int nanda_mysql_id) {
        this.nanda_mysql_id = nanda_mysql_id;
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
        dest.writeInt(this.nanda_mysql_id);
        dest.writeString(this.reason);
        dest.writeString(this.diagnosis);
        dest.writeString(this.class_name);
        dest.writeString(this.domain_name);
    }

    public DbItem() {
    }

    protected DbItem(Parcel in) {
        this.nanda_mysql_id = in.readInt();
        this.reason = in.readString();
        this.diagnosis = in.readString();
        this.class_name = in.readString();
        this.domain_name = in.readString();
    }

    public static final Creator<DbItem> CREATOR = new Creator<DbItem>() {
        @Override
        public DbItem createFromParcel(Parcel source) {
            return new DbItem(source);
        }

        @Override
        public DbItem[] newArray(int size) {
            return new DbItem[size];
        }
    };
}
