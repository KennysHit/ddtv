package com.example.ddtv;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.google.android.material.textfield.TextInputEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends AppCompatActivity {
    private TextInputEditText userName;
    private TextInputEditText passWord;
    private Button login;
    private Button register;
    private User user = new User ();
    //权限属性
    private final RxPermissions rxPermissions =  new  RxPermissions(this); //这是一个Activity或Fragment实例
    private String[] permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.CAMERA
    };//权限集合

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //Bmob默认初始化
        Bmob.initialize(this, "b958a1dad37d7c758503cfc3ac7fcd0f");
        initView ();

        //权限初始化
        if (lacksPermissions(this,permissionsREAD)){//读写权限没开启
            getPermission ();
        }else {

        }

        if ( BmobUser.isLogin () ){
            Toast.makeText ( this,"已登录！",Toast.LENGTH_SHORT ).show ();
            Intent intent = new Intent (  );
            intent.setClass ( MainActivity.this,Home.class );
            startActivity ( intent );
            finish ();
        }else{
            Toast.makeText ( this,"请登录！",Toast.LENGTH_SHORT ).show ();
        }

        login.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if(!userName.getText ( ).toString ( ).isEmpty () && !userName.getText ( ).toString ( ).isEmpty ()){

                    user.setUsername ( userName.getText ( ).toString ( ).trim ( ) );
                    user.setPassword ( passWord.getText ( ).toString ( ).trim ( ) );
                    user.login ( new SaveListener< User > ( ) {

                        @Override
                        public void done ( User user , BmobException e ) {
                            if(user!=null){
                                Toast.makeText ( MainActivity.this,"登陆成功！",Toast.LENGTH_SHORT ).show ();
                                Intent intent = new Intent (  );
                                intent.setClass ( MainActivity.this,Home.class );
                                startActivity ( intent );
                                finish ();
                            }else {
                                Toast.makeText ( MainActivity.this,"错误信息："+e.toString (),Toast.LENGTH_SHORT ).show ();
                            }
                        }
                    } );
                }else {
                    Toast.makeText ( MainActivity.this ,"用户名或密码为空！" , Toast.LENGTH_SHORT ).show ( );
                }

            }
        } );

        register.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent (  );
                intent.setClass ( MainActivity.this,Register.class );
                startActivity ( intent );
            }
        } );
    }
    public  void initView(){
        userName = (TextInputEditText)findViewById ( R.id.main_username );
        passWord = (TextInputEditText)findViewById ( R.id.main_password );
        login = (Button)findViewById ( R.id.main_button_loading );
        register = (Button)findViewById ( R.id.main_button_register );
    }
    private void getPermission(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("权限声明")//设置对话框的标题
                .setMessage("本软件需要获取相机和存储权限")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText (  MainActivity.this,"未获取相关权限，软件无法正常运行！",Toast.LENGTH_SHORT ).show ();
                        finish ();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which) {
                        rxPermissions
                                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA)
                                .subscribe(permission -> { //将发出2权限对象
                                    if (permission.granted) {
                                        Toast.makeText(MainActivity.this, "已获取相关权限", Toast.LENGTH_SHORT).show();
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        //拒绝权限，不要再问任何其他
                                    } else {
                                        //拒绝权限再问一次
                                        //需要转到设置
                                    }
                                });
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    private boolean lacksPermissions( Context mContexts, String [] permissions ) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts,permission)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
