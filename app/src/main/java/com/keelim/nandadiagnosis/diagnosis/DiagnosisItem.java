package com.keelim.nandadiagnosis.diagnosis;

import android.os.Parcel;
import android.os.Parcelable;

public class DiagnosisItem implements Parcelable {
    private String diagnosis;
    private String diagnosis_description;

    public DiagnosisItem(String diagnosis, String diagnosis_description) {
        this.diagnosis = diagnosis;
        this.diagnosis_description = diagnosis_description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

// --Commented out by Inspection START (2019-12-10 오전 12:46):
//    public void setDiagnosis(String diagnosis) {
//        this.diagnosis = diagnosis;
//    }
// --Commented out by Inspection START (2019-12-10 오전 12:46):
//// --Commented out by Inspection STOP (2019-12-10 오전 12:46)
//
//// --Commented out by Inspection START (2019-12-10 오전 12:46):
////    public String getDiagnosis_description() {
// --Commented out by Inspection STOP (2019-12-10 오전 12:46)
//        return diagnosis_description;
//    }
// --Commented out by Inspection STOP (2019-12-10 오전 12:46)

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

// --Commented out by Inspection START (2019-12-10 오전 12:46):
//    public DiagnosisItem() {
//    }
// --Commented out by Inspection STOP (2019-12-10 오전 12:46)

    private DiagnosisItem(Parcel in) {
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

