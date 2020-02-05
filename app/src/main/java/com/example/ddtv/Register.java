package com.example.ddtv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {
    private Button register;
    private Button back;
    private TextInputEditText userName;
    private TextInputEditText passWord;
    private TextInputEditText passWord2;
    private User user;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );

        initView();


        register.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                user = new User ();
                user.setUsername ( userName.getText ().toString () );
                user.setPassword ( passWord.getText ().toString () );
                if ( !userName.getText ().toString ().isEmpty ( ) &&
                        !passWord.getText ().toString ().isEmpty ( ) &&
                        !passWord2.getText ( ).toString ( ).isEmpty ( ) ) {
                    if ( passWord.getText ().toString ().equals ( passWord2.getText ( ).toString ( ) ) ) {
                        user.signUp ( new SaveListener< User > ( ) {
                            @Override
                            public void done ( User user , BmobException e ) {
                                if(e==null){
                                    Toast.makeText ( Register.this , "注册成功！" , Toast.LENGTH_SHORT ).show ( );
                                    Intent intent = new Intent (  );
                                    intent.setClass ( Register.this,MainActivity.class );
                                    startActivity ( intent );
                                    finish ();
                                }else{
                                    Toast.makeText ( Register.this , "注册失败，错误："+e.getMessage () , Toast.LENGTH_SHORT ).show ( );
                                    Log.e ( "error:",e.getMessage () );
                                }
                            }
                        } );
                    }else{
                        Toast.makeText ( Register.this , "请确认两次密码输入相同！" , Toast.LENGTH_SHORT ).show ( );
                        passWord.setText ( "" );
                        passWord2.setText ( "" );
                    }
                }else {
                    Toast.makeText ( Register.this , "请将注册信息填写完整！" , Toast.LENGTH_SHORT ).show ( );
                }
            }
        } );
        back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent (  );
                intent.setClass ( Register.this,MainActivity.class );
                startActivity ( intent );
                finish ();
            }
        } );
    }

    public void initView(){
        register = ( Button )findViewById ( R.id.register_button_register ) ;
        back = ( Button ) findViewById ( R.id.register_button_back );
        userName = ( TextInputEditText )findViewById ( R.id.register_userName );
        passWord = ( TextInputEditText )findViewById ( R.id.register_passWord );
        passWord2 = ( TextInputEditText )findViewById ( R.id.register_passWord2 );
    }
}
