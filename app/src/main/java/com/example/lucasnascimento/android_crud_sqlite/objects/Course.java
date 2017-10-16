package com.example.lucasnascimento.android_crud_sqlite.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lucasnascimento on 15/10/17.
 */

public class Course implements Parcelable {
    private int id;
    private String name;
    private String description;
    private int classHours;
    private String registerDate;
    private boolean status;

    public Course() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        this.registerDate = dateFormat.format(date).toString();
    }

    protected Course(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        classHours = in.readInt();
        registerDate = in.readString();
        status = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(classHours);
        parcel.writeString(registerDate);
        parcel.writeByte((byte)(status ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descricao) {
        this.description = descricao;
    }

    public int getClassHours() {
        return classHours;
    }

    public void setClassHours(int classHours) {
        this.classHours = classHours;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRegisterDateFormated(){
        String inputString = this.getRegisterDate();
        Locale brasil = new Locale("pt", "BR");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",brasil);
        Date inputDate = null;
        try {
            inputDate = f.parse(inputString);
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }

        if(inputDate!=null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            inputString = dateFormat.format(inputDate).toString();
        }
        return inputString;
    }
}
