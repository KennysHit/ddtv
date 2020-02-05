package com.example.ddtv;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private BmobFile headPicture;


    public void setHeadPicture ( BmobFile headPicture ) {
        this.headPicture = headPicture;
    }

    public BmobFile getHeadPicture () {
        return headPicture;
    }




}