package com.example.notes__.controller;

import android.app.Application;

import com.example.notes__.model.Document;

import java.util.ArrayList;

public class AppContext extends Application {
    public static final String ACTION_TYPE = ".ActionType";
    public static final String DOC_INDEX = ".ActionIndex";

    public static final int ACTION_NEW_TASK = 0;
    public static final int ACTION_UPDATE = 1;

    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CREATE_DATE = "createDate";
    public static final String FIELD_PRIORITY_TYPE = "priorityType";

    private ArrayList<Document> listDocument = new ArrayList<>();

    public ArrayList<Document> getListDocument() {
        return listDocument;
    }

    public String getPrefsDir() {
        return getApplicationInfo().dataDir + "/" + "shared_prefs";
    }

}
