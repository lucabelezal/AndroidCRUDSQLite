package com.example.lucasnascimento.android_crud_sqlite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.lucasnascimento.android_crud_sqlite.R;
import com.example.lucasnascimento.android_crud_sqlite.helpers.BDSQLiteHelper;
import com.example.lucasnascimento.android_crud_sqlite.objects.Course;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class HomeActivity extends AppCompatActivity {

    Button btnRegister, btnList;
    BDSQLiteHelper dBHelper;
    Toolbar toolbar;
    Bundle extras;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        btnRegister = (Button) findViewById(R.id.registerButton);
        btnList = (Button) findViewById(R.id.listButton);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, PersistCourseActivity.class);
                i.putExtra("course", new Course());
                startActivity(i);
            }

        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

