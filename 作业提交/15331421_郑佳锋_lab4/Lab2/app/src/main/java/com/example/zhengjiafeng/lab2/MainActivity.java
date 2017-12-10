package com.example.zhengjiafeng.lab2;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mImage = (ImageView) findViewById(R.id.sysuimage);
        mImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);
                dialogbuilder.setTitle("上传头像");
                dialogbuilder.setItems(new String[]{"拍摄","从相册选择"},new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int num){
                        switch(num) {
                            case 0:
                                Toast.makeText(MainActivity.this, "您选择了[拍摄]", Toast.LENGTH_SHORT).show();
                                    break;
                            case 1:
                                Toast.makeText(MainActivity.this, "您选择了[从相册选择]", Toast.LENGTH_SHORT).show();
                                    break;
                            default:
                                    break;
                        }
                    }
                });
                dialogbuilder.setNegativeButton("取消", click1);
                AlertDialog dialog = dialogbuilder.create();
                dialog.show();
            }
            private DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface arg0, int arg1){
                    arg0.cancel();
                    Toast.makeText(MainActivity.this, "您选择了[取消]", Toast.LENGTH_SHORT).show();
                }
            };
        });

        final RadioGroup mRadiogroup = (RadioGroup) findViewById(R.id.group);
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                 switch(checkedId){
                     case R.id.radiobutton1:
                         Snackbar.make(mRadiogroup, "您选择了学生", Snackbar.LENGTH_SHORT)
                                 .setAction("确定",new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                             }
                                         })
                         .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                         .setDuration(5000)
                         .show();
                         break;
                     case R.id.radiobutton2:
                         Snackbar.make(mRadiogroup, "您选择了教职工", Snackbar.LENGTH_SHORT)
                                 .setAction("确定",new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                     }
                                 })
                                 .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                 .setDuration(5000)
                                 .show();
                         break;
                     default:
                         break;
                 }
            }
        });

        Button regButton = (Button) findViewById(R.id.button2);
        regButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mRadiogroup.getCheckedRadioButtonId() == R.id.radiobutton1 ){
                    Snackbar.make(mRadiogroup, "学生注册功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else{
                    Snackbar.make(mRadiogroup, "教职工注册功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });

        Button logButton = (Button) findViewById(R.id.button1);
        logButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextInputLayout id = (TextInputLayout) findViewById(R.id.input1);
                TextInputLayout pass = (TextInputLayout) findViewById(R.id.input2);
                EditText idInput = id.getEditText();
                EditText passInput = pass.getEditText();
                if (idInput.getText().toString().equals("")){
                    id.setErrorEnabled(true);
                    id.setError("学号不能为空");
                    pass.setErrorEnabled(false);
                }
                else if(!idInput.getText().toString().equals("")&& passInput.getText().toString().equals("")){
                    pass.setErrorEnabled(true);
                    pass.setError("密码不能为空");
                    id.setErrorEnabled(false);
                }
                else if(!idInput.getText().toString().equals("123456") || !passInput.getText().toString().equals("6666")){
                    id.setErrorEnabled(false);
                    pass.setErrorEnabled(false);
                    Snackbar.make(mRadiogroup, "学号或密码错误", Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else{
                    id.setErrorEnabled(false);
                    pass.setErrorEnabled(false);
                    Snackbar.make(mRadiogroup, "登录成功", Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });

























    }
}
