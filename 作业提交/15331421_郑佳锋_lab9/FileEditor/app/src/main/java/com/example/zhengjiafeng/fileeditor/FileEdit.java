package com.example.zhengjiafeng.fileeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by zhengjiafeng on 2017/11/30.
 */

public class FileEdit extends AppCompatActivity {
    private EditText name;
    private EditText content;
    private Button save;
    private Button load;
    private Button clear;
    private Button delete;
    private String name_str;
    private String content_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);

        init();
        setListener();
    }
    public void init(){
        name = (EditText)findViewById(R.id.name);
        content = (EditText)findViewById(R.id.content);
        save = (Button)findViewById(R.id.save);
        load = (Button)findViewById(R.id.load);
        clear = (Button)findViewById(R.id.clear);
        delete = (Button)findViewById(R.id.delete);
    }
    public void setListener(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name_str = name.getText().toString();
               content_str = content.getText().toString();
                if(name_str.equals("")){
                    Toast.makeText(FileEdit.this, "Fail to save file", Toast.LENGTH_SHORT).show();
                }
                else {
                    try (FileOutputStream fileOutputStream = openFileOutput(name_str, MODE_PRIVATE)) {
                        fileOutputStream.write(content_str.getBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Toast.makeText(FileEdit.this, "Save successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        Toast.makeText(FileEdit.this, "Fail to save file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = name.getText().toString();
                if(name_str.equals("")){
                    Toast.makeText(FileEdit.this, "Fail to load file", Toast.LENGTH_SHORT).show();
                    content.setText("");
                }
                try (FileInputStream fileInputStream = openFileInput(name_str))
                {
                    byte[] contents = new byte[fileInputStream.available()];
                        while (fileInputStream.read(contents) != -1) {

                        }
                        String text = new String(contents);
                        content.setText(text);
                    Toast.makeText(FileEdit.this, "Load successfully", Toast.LENGTH_SHORT).show();
                }
                catch (IOException ex)
                {
                    Toast.makeText(FileEdit.this, "Fail to load file", Toast.LENGTH_SHORT).show();
                    content.setText("");
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = name.getText().toString();
                if(name_str.equals("")){
                    Toast.makeText(FileEdit.this, "Please input a file name", Toast.LENGTH_SHORT).show();
                }
                else{
                    deleteFile(name_str);
                    Toast.makeText(FileEdit.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
