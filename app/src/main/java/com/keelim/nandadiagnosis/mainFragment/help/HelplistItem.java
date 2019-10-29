package com.keelim.nandadiagnosis.mainFragment.help;

import java.io.Serializable;

public class HelplistItem implements Serializable {
    private String title;

    public HelplistItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
