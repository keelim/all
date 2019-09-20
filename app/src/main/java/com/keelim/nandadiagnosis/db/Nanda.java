package com.keelim.nandadiagnosis.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nanda")
public class Nanda {
    @PrimaryKey
    Long id;

    @ColumnInfo(name = "reason")
    String reason;

    @ColumnInfo(name = "diagnosis")
    String diagnosis;

    @ColumnInfo(name = "class_name")
    String class_name;

    @ColumnInfo(name = "domain_name")
    String domain_name;

    public Nanda(Long id, String reason, String diagnosis, String class_name, String domain_name) {
        this.id = null;
        this.reason = "";
        this.diagnosis = "";
        this.class_name = "";
        this.domain_name = "";
    }
}
