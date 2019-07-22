package com.example.notes__.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes__.controller.AppContext;
import com.example.notes__.model.PriorityType;
import com.example.notes__.R;
import com.example.notes__.model.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Details extends AppCompatActivity {

    private static final String LOG_TAG = "Details";

    private static final int NAME_LENGTH = 15;
    private EditText todoDetails;
    private Document doc;

    private ArrayList<Document> listDocument;
    private int actionType;
    private int docIndex;

    private PriorityType currentPriorityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        todoDetails = findViewById(R.id.todoDetails);
        listDocument = ((AppContext) getApplicationContext()).getListDocument();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionType = getIntent().getExtras().getInt(AppContext.ACTION_TYPE);
        prepareDocument(actionType);
    }

    private void prepareDocument(int actionType) {
        switch (actionType) {
            case AppContext.ACTION_NEW_TASK: {
                doc = new Document();
                break;
            }
            case AppContext.ACTION_UPDATE: {
                docIndex = getIntent().getExtras().getInt(AppContext.DOC_INDEX);
                doc = listDocument.get(docIndex);
                todoDetails.setText(doc.getContent());
                break;
            }
            default:
                break;
        }
        currentPriorityType = doc.getPriorityType();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (todoDetails.getText().toString().trim().length() == 0) finish();
                else saveDocument();
                finish();
                return true;
            }
            case R.id.save: {
                saveDocument();
                return true;
            }
            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_delete);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDocument();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            case R.id.important: {
                currentPriorityType = PriorityType.IMPORTANT;
                return true;
            }
            case R.id.ordinary: {
                currentPriorityType = PriorityType.ORDINARY;
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDocument() {
        if (actionType == AppContext.ACTION_UPDATE) {
            boolean edited = false;
            SharedPreferences sharedPref = getSharedPreferences(String.valueOf(
                    doc.getCreateDate().getTime()), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (!todoDetails.getText().toString().equals(doc.getContent())) {
                doc.setName(getDocumentName());
                doc.setContent(todoDetails.getText().toString());
                editor.putString(AppContext.FIELD_CONTENT, doc.getContent());
                edited = true;
            }
            if (currentPriorityType != doc.getPriorityType()) {
                doc.setPriorityType(currentPriorityType);
                editor.putInt(AppContext.FIELD_PRIORITY_TYPE, doc.getPriorityType().getIndex());
                edited = true;
            }
            if (edited) {
                String path = ((AppContext) getApplicationContext()).getPrefsDir();
                File file = new File(path, doc.getCreateDate().getTime() + ".xml");
                doc.setCreateDate(new Date());
                editor.putString(AppContext.FIELD_NAME, doc.getName());
                editor.putLong(AppContext.FIELD_CREATE_DATE, doc.getCreateDate().getTime());
                editor.commit();
                file.renameTo(new File(path, doc.getCreateDate().getTime() + ".xml"));
            }
        } else if (actionType == AppContext.ACTION_NEW_TASK) {
            doc.setName(getDocumentName());
            doc.setContent(todoDetails.getText().toString());
            doc.setPriorityType(currentPriorityType);
            doc.setCreateDate(new Date());
            SharedPreferences sharedPref = getSharedPreferences(String.valueOf(
                    doc.getCreateDate().getTime()), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(AppContext.FIELD_CONTENT, doc.getContent());
            editor.putString(AppContext.FIELD_NAME, doc.getName());
            editor.putLong(AppContext.FIELD_CREATE_DATE, doc.getCreateDate().getTime());
            editor.putInt(AppContext.FIELD_PRIORITY_TYPE, doc.getPriorityType().getIndex());
            editor.putBoolean(AppContext.FIELD_CHECK, doc.getCheckBox());
            editor.commit();
            listDocument.add(doc);
        }
        finish();
    }

    private String getDocumentName() {
        StringBuilder str = new StringBuilder(todoDetails.getText());
        String tmpName = str.toString().split("\n")[0];
        if (tmpName.equals(str.toString())) {
            if (str.length() > NAME_LENGTH) str.delete(NAME_LENGTH, str.length()).append("...");
            return (str.length() > 0) ? str.toString() : getResources().getString(R.string.new_document) ;
        }
        else return tmpName + "\n...";
    }

    private void deleteDocument() {
        if (actionType == AppContext.ACTION_UPDATE) {
            File file = getCurrentFile();
            if (file.delete()) {
                listDocument.remove(docIndex);
            } else {
                Log.e(LOG_TAG, "Can't delete file: " + file.getName());
            }
        }
        finish();
    }

    private File getCurrentFile() {
        String filePath = ((AppContext) getApplicationContext()).getPrefsDir()
                + "/" + doc.getCreateDate().getTime() + ".xml";
        return new File(filePath);
    }
}
