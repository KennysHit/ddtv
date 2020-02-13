package com.example.ddtv;

import android.graphics.Bitmap;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Video extends BmobObject {

    private String title;
    private BmobFile file;
    private String author;

    public void setTitle ( String title ) {
        this.title = title;
    }

    public void setFile ( BmobFile file ) {
        this.file = file;
    }

    public void setAuthor ( String author ) {
        this.author = author;
    }

    public String getTitle () {
        return title;
    }

    public BmobFile getFile () {
        return file;
    }

    public String getAuthor () {
        return author;
    }
}