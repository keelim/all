package com.keelim.nandadiagnosis.mainFragment.search.db;

import java.io.Serializable;

public class DbItem implements Serializable {
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
}
