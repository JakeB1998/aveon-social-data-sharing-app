package com.example.madcompetition.backend.settings;

public class AllSettingsInterface {
    private static final AllSettingsInterface ourInstance = new AllSettingsInterface();

    public static AllSettingsInterface getInstance() {
        return ourInstance;
    }

    private AllSettingsInterface() {
    }
}
