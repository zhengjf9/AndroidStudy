package com.example.zhengjiafeng.fileeditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText newPwd;
    private EditText conPwd;
    private Button ok;
    private Button clear;
    private String newPassword;
    private String conPassword;
    private static int MODE = MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "SavePassword";
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        share = getSharedPreferences(PREFERENCE_NAME, MODE);
        boolean valid = share.getBoolean("valid", false);
        if(valid == false) {
            setListener();
        }
        else{
            conPwd.setVisibility(View.GONE);
            newPwd.setHint("Password");
            setListenerForPass();
        }
        setListenserForClear();
    }
    private void init(){
        newPwd = (EditText)findViewById(R.id.newpwd);
        conPwd = (EditText)findViewById(R.id.conpwd);
        ok = (Button)findViewById(R.id.ok);
        clear = (Button)findViewById(R.id.clear);
    }
    private void setListener(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = newPwd.getText().toString();
                conPassword = conPwd.getText().toString();
                if (newPassword.equals("") || conPassword.equals("")){
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.equals(conPassword)){
                    Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("password", conPassword);
                    editor.putBoolean("valid", true);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, FileEdit.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
    private void setListenerForPass(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = newPwd.getText().toString();
                if(newPassword.equals("")){
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.equals(share.getString("password", "default"))){
                    Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, FileEdit.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
    private void setListenserForClear(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPwd.setText("");
                conPwd.setText("");
            }
        });
    }
}
