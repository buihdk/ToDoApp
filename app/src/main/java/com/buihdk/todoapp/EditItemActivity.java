package com.buihdk.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    EditText etEdit;
    String edited_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edited_item = getIntent().getStringExtra("editing_item");
        etEdit = (EditText) findViewById(R.id.etEdit);
        etEdit.setText(edited_item);
        etEdit.requestFocus();
    }

    public void onSaveItem(View view) {
        if (etEdit.getText().toString().trim().matches("")) {
            Toast.makeText(this, "Item cannot be blank!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra("edited_item", etEdit.getText().toString());
        setResult(RESULT_OK, data);
        this.finish();
    }

}