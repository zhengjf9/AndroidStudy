package com.example.zhengjiafeng.birthdayreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zhengjiafeng on 2017/12/7.
 */

public class AddItem extends AppCompatActivity {
    public EditText name;
    public EditText birth;
    public EditText gift;
    public Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        init();
        setOnListener();
    }
    void init(){
       name = (EditText)findViewById(R.id.name_input);
       birth = (EditText)findViewById(R.id.birth_input);
       gift = (EditText)findViewById(R.id.gift_input);
       addBtn = (Button)findViewById(R.id.add);
    }
    void setOnListener(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString();
                String birth_str = birth.getText().toString();
                String gift_str = gift.getText().toString();
                if(name_str.equals("")){
                    Toast.makeText(AddItem.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyDbHelper myDbHelper =  myDbHelper = new MyDbHelper(getApplicationContext(), "myDataBase", null, 1);;
                    if(myDbHelper.isExist(name_str)){
                        Toast.makeText(AddItem.this, "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent();
                        mIntent.putExtra("name", name_str);
                        mIntent.putExtra("birth", birth_str);
                        mIntent.putExtra("gift", gift_str);
                        setResult(0, mIntent);
                        AddItem.this.finish();
                    }
                }
            }
        });
    }
}
