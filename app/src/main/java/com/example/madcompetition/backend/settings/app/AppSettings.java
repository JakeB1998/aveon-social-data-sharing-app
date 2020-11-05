package com.example.madcompetition.backend.settings.app;

import java.io.Serializable;
import java.util.Locale;

public class AppSettings implements Serializable {

    private Locale selectedLocale;


    public AppSettings(Locale selectedLocale)
    {
        this.selectedLocale = selectedLocale;
    }


}
