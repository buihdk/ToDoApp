package com.buihdk.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEdit;
    String edited_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Do something else instead?");
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edited_item = getIntent().getStringExtra("item_for_editing");
        etEdit = (EditText) findViewById(R.id.etEdit);
        etEdit.setText(edited_item);
        etEdit.requestFocus();
    }

    public void onSaveItem(View view) {
        Intent data = new Intent();
        data.putExtra("edited_item", etEdit.getText().toString());
        setResult(RESULT_OK, data);
        this.finish();
    }

}
