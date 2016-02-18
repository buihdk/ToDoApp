package com.buihdk.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;

    ListView lvItems;
    EditText etEditText;
    String new_item;
    String editing_item;
    String edited_item;

    int index; // position of an item being edited in the array list
    private final int REQUEST_CODE = 20; // request code can be any value, used to determine the result type later
    private ToDoAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etEditText = (EditText) findViewById(R.id.etEditText);

        // set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // instantiate database
        db = new ToDoAppDatabase(this);

        // show list of items
        //readItems();
        todoItems = new ArrayList<String>(db.todoItems());
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems); // initialize ArrayAdapter
        lvItems.setAdapter(aToDoAdapter); // connect adapter to a list view

        // remove item by long click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteItems(todoItems.get(position)); //writeItems();
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                //Log.d("MainActivity","position"+position);
                //Toast.makeText(MainActivity.this, String.valueOf(position)+ " " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // edit item by short click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position; // Store the position of the edited item which will be used in onActivityResult() method
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                editing_item = todoItems.get(position);
                // put "extra" into the bundle for access in the EditItemActivity
                i.putExtra("editing_item",todoItems.get(position));
                // open child activity whose result can be retrieved to the parent activity
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void onAddItem(View view) {
        new_item = etEditText.getText().toString();
        db.insertItems(new_item); //writeItems();
        aToDoAdapter.add(new_item);
        etEditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract edited item's value from extra
            edited_item = data.getExtras().getString("edited_item");
            db.updateItems(editing_item, edited_item); //writeItems();
            todoItems.set(index, edited_item);
            aToDoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void readItems() {
//        File filesDir = getFilesDir(); // get specific directory for read/write access
//        File file = new File(filesDir, "todo.txt"); // instantiate a file type to read file in the specific dir
//        try {
//            todoItems = new ArrayList<String>(FileUtils.readLines(file));
//        } catch (IOException e) {
//
//        }
//    }
//    private void writeItems() {
//        File filesDir = getFilesDir(); // get specific directory for read/write access
//        File file = new File(filesDir, "todo.txt"); // instantiate a file type to write file in the specific dir
//        try {
//            FileUtils.writeLines(file, todoItems);
//        } catch (IOException e) {
//
//        }
//    }

}