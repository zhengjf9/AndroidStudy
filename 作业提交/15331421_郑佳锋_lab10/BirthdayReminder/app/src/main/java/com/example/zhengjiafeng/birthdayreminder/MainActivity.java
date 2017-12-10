package com.example.zhengjiafeng.birthdayreminder;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public ListView list;
    public Button addBtn;
    public List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    public SimpleAdapter simpleAdapter;
    public MyDbHelper myDbHelper;
    private static final String TABLE_NAME = "Contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        myDbHelper = new MyDbHelper(this, "myDataBase", null, 1);
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null,null,null,null);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            Map<String, String> temp = new LinkedHashMap<String, String>();
            temp.put("name", cursor.getString(1));
            temp.put("birth", cursor.getString(2));
            temp.put("gift", cursor.getString(3));
            data.add(temp);
        }
        cursor.close();
        db.close();
        setOnListener();
    }
    void init(){
        addBtn = (Button) findViewById(R.id.add);
        list = (ListView) findViewById(R.id.list);
        String[] name = new String[]{"姓名"};
        String[] birth = new String[]{"生日"};
        String[] gift = new String[]{"礼物"};
        for (int i = 0; i < name.length; i++) {
            Map<String, String> temp = new LinkedHashMap<String, String>();
            temp.put("name", name[i]);
            temp.put("birth", birth[i]);
            temp.put("gift", gift[i]);
            data.add(temp);
        }
        simpleAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.item,
                new String[]{"name", "birth","gift"}, new int[]{R.id.name, R.id.birth, R.id.gift});
        list.setAdapter(simpleAdapter);
    }
    void setOnListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 0){}
                else {
                    LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                    final View layout = factor.inflate(R.layout.dialoglayout, null);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this); alertDialog.setView(layout);
                    ((TextView) layout.findViewById(R.id.name_input)).setText(data.get(position).get("name"));
                    ((EditText) layout.findViewById(R.id.birth_input)).setText(data.get(position).get("birth"));
                    ((EditText) layout.findViewById(R.id.gift_input)).setText(data.get(position).get("gift"));
                    Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
                    int flag = 0;
                    String Number = "";
                    while(cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));
                        int isHas = Integer. parseInt (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts. HAS_PHONE_NUMBER )));
                        if(name.equals(data.get(position).get("name")) && isHas == 1){
                            String contactId = cursor.getString(cursor.getColumnIndex(
                                    ContactsContract.Contacts._ID));
                            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                                    null, null);
                                    if(phone.moveToFirst()) {
                                        Number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    }
                                    flag = 1;
                                    phone.close();
                                    break;
                        }
                    }
                    cursor.close();
                    if(flag == 0){
                        ((TextView) layout.findViewById(R.id.number)).setText("无");
                    } else{
                        ((TextView) layout.findViewById(R.id.number)).setText(Number);
                    }
                    alertDialog.setNegativeButton("放弃修改", cancelClick);
                    alertDialog.setPositiveButton("保存修改",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String new_birth = ((EditText) layout.findViewById(R.id.birth_input)).getText().toString();
                                    String new_gift = ((EditText) layout.findViewById(R.id.gift_input)).getText().toString();
                                    data.get(position).put("birth", new_birth);
                                    data.get(position).put("gift", new_gift);
                                    simpleAdapter.notifyDataSetChanged();
                                    myDbHelper.update(data.get(position).get("name"),new_birth, new_gift);
                                }
                            });
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                }
            }
            private DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface arg0, int arg1){
                    arg0.cancel();
                    Toast.makeText(MainActivity.this, "修改已放弃", Toast.LENGTH_SHORT).show();
                }
            };
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);
                dialogbuilder.setTitle("是否删除？");
                dialogbuilder.setNegativeButton("否", cancelClick);
                dialogbuilder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDbHelper.delete(data.get(arg2).get("name"));
                                data.remove(arg2);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog dialog = dialogbuilder.create();
                dialog.show();
                return true;
            }
            private DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface arg0, int arg1){
                    arg0.cancel();
                }
            };
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent != null && intent.hasExtra("name")) {
            String name_str = intent.getStringExtra("name");
            String birth_str = intent.getStringExtra("birth");
            String gift_str = intent.getStringExtra("gift");
            Map<String, String> temp = new LinkedHashMap<String, String>();
            temp.put("name", name_str);
            temp.put("birth", birth_str);
            temp.put("gift", gift_str);
            data.add(temp);
            simpleAdapter.notifyDataSetChanged();
            myDbHelper.insert(name_str, birth_str, gift_str);
        }
    }
}
