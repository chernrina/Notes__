package com.example.notes__.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes__.controller.Adapter;
import com.example.notes__.controller.AppContext;
import com.example.notes__.R;
import com.example.notes__.model.PriorityType;
import com.example.notes__.model.ListComparator;
import com.example.notes__.model.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView listTask;
    private EditText search;
    private Intent intent;
    private Adapter arrayAdapter;
    private ArrayList<Document> listDocument;
    private static Comparator<Document> comparator = ListComparator.getDateComparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listTask = findViewById(R.id.listTasks);
        listTask.setOnItemClickListener(new ListViewClickListener());
        listTask.setEmptyView(findViewById(R.id.emptyView));
        listDocument = ((AppContext)getApplicationContext()).getListDocument();
        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextChangeListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        intent = new Intent(this, Details.class);
        fillList();
    }

    private void fillList() {
        File prefsdir = new File(getApplicationInfo().dataDir, "shared_prefs");
        if(prefsdir.exists() && prefsdir.isDirectory()) {
            String[] str = prefsdir.list();
            for (int i = 0; i< str.length; i++) {
                SharedPreferences sharedPref = getSharedPreferences(str[i].replace(
                        ".xml",""), Context.MODE_PRIVATE);
                Document doc = new Document();
                doc.setContent(sharedPref.getString(AppContext.FIELD_CONTENT, null));
                doc.setName(sharedPref.getString(AppContext.FIELD_NAME, null));
                doc.setCreateDate(new Date(sharedPref.getLong(AppContext.FIELD_CREATE_DATE,0)));
                doc.setPriorityType(PriorityType.values()[sharedPref.getInt
                        (AppContext.FIELD_PRIORITY_TYPE, 0)]);
                if (!listDocument.contains(doc)) listDocument.add(doc);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sort();
        checkSearchActive();
    }

    private void checkSearchActive() {
        if (listDocument.isEmpty()) search.setEnabled(false);
        else {
            search.setEnabled(true);
            arrayAdapter.getFilter().filter(search.getText());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked()) return true;
        switch (item.getItemId()) {
            case R.id.add_task: {
                Bundle bundle = new Bundle();
                bundle.putInt(AppContext.ACTION_TYPE, AppContext.ACTION_NEW_TASK);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
            case R.id.delete_all: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Удалить все заметки?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllFiles();
                        listDocument.clear();
                        onStart();
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
            case R.id.menu_sort_date: {
                item.setChecked(true);
                comparator = ListComparator.getDateComparator();
                sort();
                return true;
            }
            case R.id.menu_sort_name: {
                item.setChecked(true);
                comparator = ListComparator.getNameComparator();
                sort();
                return true;
            }
            case R.id.menu_sort_priority: {
                item.setChecked(true);
                comparator = ListComparator.getPriorityComparator();
                sort();
                return true;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sort() {
        Collections.sort(listDocument,comparator);
        updateIndexes();
        arrayAdapter = new Adapter(this,listDocument);
        listTask.setAdapter(arrayAdapter);
        arrayAdapter.getFilter().filter(search.getText());
        setTitle(getResources().getString(R.string.app_name)+" ("
                +listDocument.size()+")");
    }

    private void updateIndexes() {
        int i = 0;
        for (Document doc: listDocument) {
            doc.setNumber(i++);
        }
    }

    public void searchClear(View view) {
        search.setText("");
    }

    private void deleteAllFiles() {
        for (Document doc: listDocument) {
            getCurrentFile(doc).delete();
        }
    }

    private File getCurrentFile(Document doc) {
        String path = ((AppContext)getApplicationContext()).getPrefsDir() + "/" +
                doc.getCreateDate().getTime() + ".xml";
        return new File(path);
    }

    class ListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppContext.ACTION_TYPE,AppContext.ACTION_UPDATE);
            bundle.putInt(AppContext.DOC_INDEX,((Document)parent.getAdapter().getItem(position)).getNumber());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    class TextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            arrayAdapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }
}
