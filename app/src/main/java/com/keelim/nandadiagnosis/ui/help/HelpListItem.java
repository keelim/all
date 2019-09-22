package com.keelim.nandadiagnosis.ui.help;

import java.io.Serializable;

public class HelpListItem implements Serializable {
    private String title;

    public HelpListItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
