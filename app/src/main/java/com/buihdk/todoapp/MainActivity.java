package com.buihdk.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    ArrayList<Item> todoItems;
    CustomItemsAdapter aToDoAdapter;

    ListView lvItems;
    EditText etEditText;
    Item new_item;
    Item editing_item;
    Item edited_item;
    Item delete_item;
    String item_name;
    String item_date;

    private Calendar calendar;
    private static TextView tvDueDate;
    private static int year, month, day;

    int index; // position of an item being edited in the array list
    private final int REQUEST_CODE = 20; // request code can be any value, used to determine the result type later
    private ToDoAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etEditText = (EditText) findViewById(R.id.etEditText);

        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // set default date to current date
        tvDueDate.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));

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
        todoItems = new ArrayList<Item>(db.readItems()); //Array List of class Item
        aToDoAdapter = new CustomItemsAdapter(this, todoItems); // initialize Custom ArrayAdapter
        lvItems.setAdapter(aToDoAdapter); // connect adapter to a list view

        // remove item by long click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)  {
                delete_item = todoItems.get(position);
                db.deleteItems(delete_item);
                todoItems.remove(delete_item);
                aToDoAdapter.notifyDataSetChanged();
                //Log.d("MainActivity","position"+position);
                Toast.makeText(MainActivity.this, String.valueOf(position)+ " " + String.valueOf(id), Toast.LENGTH_SHORT).show();
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
                //i.putExtra("editing_item",todoItems.get(position));
                // open child activity whose result can be retrieved to the parent activity
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name = etEditText.getText().toString();
                item_name = item_name.trim();
                if (item_name.matches("")) {
                    Toast.makeText(MainActivity.this, "Item cannot be blank!" + todoItems.get(2), Toast.LENGTH_SHORT).show();
                    return;
                }
                //new_item.item_name = item_name;
                item_date = tvDueDate.getText().toString();
                new_item = new Item(item_name,item_date);
                db.insertItems(new_item);
                aToDoAdapter.add(new_item);
                etEditText.setText("");
            }
        });

        tvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract edited item's value from extra
            //edited_item = data.getExtras().getString("edited_item");
            //edited_item = edited_item.trim();
            //db.updateItems(editing_item, editing_date, edited_item, edited_date); //writeItems();
            //todoItems.set(index, edited_item);
            aToDoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Calendar c = Calendar.getInstance();
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            tvDueDate.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));
        }
    }
}






