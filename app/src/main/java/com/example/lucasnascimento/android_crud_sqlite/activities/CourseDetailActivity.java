package com.example.lucasnascimento.android_crud_sqlite.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucasnascimento.android_crud_sqlite.R;
import com.example.lucasnascimento.android_crud_sqlite.helpers.BDSQLiteHelper;
import com.example.lucasnascimento.android_crud_sqlite.objects.Course;

public class CourseDetailActivity extends AppCompatActivity {
    private Course course;
    private AlertDialog alert;
    TextView tvDescription;
    TextView tvClassHours;
    TextView tvStatus;
    TextView tvRegisterDate;
    BDSQLiteHelper SQLHelper;
    Toolbar toolbar;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvClassHours = (TextView) findViewById(R.id.tvClassHours);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvRegisterDate = (TextView) findViewById(R.id.tvRegisterDate);

        extras = getIntent().getExtras();
        getValuesExtras(extras);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getValuesExtras(extras);
        setValues();
    }

    private void getValuesExtras(Bundle extras) {
        SQLHelper = new BDSQLiteHelper(CourseDetailActivity.this);
        Course c = extras.getParcelable("course");
        course = SQLHelper.getCourse(c.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        if (id == R.id.action_delete) {
            deleteCourseDialog();
            return true;
        }
        if (id == R.id.action_edit) {
            Intent intent = new Intent(CourseDetailActivity.this, PersistCourseActivity.class);
            intent.putExtra("course", course);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setValues(){
        toolbar.setTitle(course.getName());
        tvDescription.setText(course.getDescription());
        tvClassHours.setText(course.getClassHours()+"h");
        tvStatus.setText(course.getStatus()==true ? getString(R.string.course_active) : getString(R.string.course_inactive));
        tvRegisterDate.setText(course.getRegisterDateFormated());
    }

    public void deleteCourseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_msg));
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                SQLHelper = new BDSQLiteHelper(CourseDetailActivity.this);
                SQLHelper.removeCourse(course);
                Toast.makeText(CourseDetailActivity.this,getString(R.string.msg_remove),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //
            }
        });
        alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
