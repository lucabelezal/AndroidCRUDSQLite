package com.example.lucasnascimento.android_crud_sqlite.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lucasnascimento.android_crud_sqlite.R;
import com.example.lucasnascimento.android_crud_sqlite.adapters.CourseRecycleViewAdapter;
import com.example.lucasnascimento.android_crud_sqlite.helpers.BDSQLiteHelper;
import com.example.lucasnascimento.android_crud_sqlite.objects.Course;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvCourses;
    BDSQLiteHelper dBHelper;
    String orderBy;
    Toolbar toolbar;
    MaterialSearchView searchView;
    private String LIKE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Listar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        SharedPreferences prefs = getSharedPreferences("ordem", MODE_PRIVATE);
        orderBy = prefs.getString("orderBy", null);
        if (orderBy == null){
            orderBy = "DESC";
        }
        rvCourses = (RecyclerView) findViewById(R.id.rvCourses);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PersistCourseActivity.class);
                i.putExtra("course", new Course());
                startActivity(i);
            }
        });
        addListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(LIKE==null){
            addListeners();
        }else{
            doSearch(LIKE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                LIKE = newText;
                doSearch(newText);
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }
            @Override
            public void onSearchViewClosed() {
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        if(id == R.id.action_list){
            SharedPreferences sharedPreferences = getSharedPreferences("ordem", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(orderBy=="DESC"){
                orderBy = "ASC";
            }else{
                orderBy = "DESC";
            }
            editor.putString("orderBy", orderBy);
            editor.commit();
            addListeners();
            return true;
        }
        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void addListeners() {
        rvCourses.setHasFixedSize(true);
        rvCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCourses.setAdapter(new CourseRecycleViewAdapter(getAllCourses(orderBy), new CourseRecycleViewAdapter.OnClickListener() {
            @Override
            public void onClickItemList(Course course, int position) {
                Intent i = new Intent(MainActivity.this, CourseDetailActivity.class);
                i.putExtra("course", course);
                startActivity(i);
            }
        }));
    }

    private ArrayList<Course> getAllCourses(String orderBy) {
        dBHelper = new BDSQLiteHelper(this);
        ArrayList<Course> list = new ArrayList<>();
        try {
            for (Course course : dBHelper.getCourses(orderBy)) {
                list.add(course);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    private void doSearch(String like) {
        rvCourses.setHasFixedSize(true);
        rvCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dBHelper = new BDSQLiteHelper(this);

        ArrayList<Course> list = new ArrayList<>();
        try {
            for (Course course : dBHelper.searchCourses(orderBy,like)) {
                list.add(course);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        rvCourses.setAdapter(new CourseRecycleViewAdapter(list, new CourseRecycleViewAdapter.OnClickListener() {
            @Override
            public void onClickItemList(Course course, int position) {
                Intent i = new Intent(MainActivity.this, CourseDetailActivity.class);
                i.putExtra("course", course);
                startActivity(i);
            }
        }));
    }
}

