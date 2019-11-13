package com.keelim.nandadiagnosis.diagnosis;

import android.os.Parcel;
import android.os.Parcelable;

public class DiagnosisItem implements Parcelable {
    String diagnosis;
    String diagnosis_description;

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis_description() {
        return diagnosis_description;
    }

    public void setDiagnosis_description(String diagnosis_description) {
        this.diagnosis_description = diagnosis_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.diagnosis);
        dest.writeString(this.diagnosis_description);
    }

    public DiagnosisItem() {
    }

    protected DiagnosisItem(Parcel in) {
        this.diagnosis = in.readString();
        this.diagnosis_description = in.readString();
    }

    public static final Creator<DiagnosisItem> CREATOR = new Creator<DiagnosisItem>() {
        @Override
        public DiagnosisItem createFromParcel(Parcel source) {
            return new DiagnosisItem(source);
        }

        @Override
        public DiagnosisItem[] newArray(int size) {
            return new DiagnosisItem[size];
        }
    };
}

