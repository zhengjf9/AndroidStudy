package com.example.zhengjiafeng.lab2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhengjiafeng on 2017/10/22.
 */

public class NewActivity extends AppCompatActivity {
    private int flag = 0;
    private String passname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ListView moreInfo = (ListView) findViewById(R.id.moreInfo);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] infoItem = new String[]{"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        for(int i = 0; i < infoItem.length; i++){
            Map<String, String> temp = new LinkedHashMap<String, String>();
            temp.put("infoItem", infoItem[i]);
            data.add(temp);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(NewActivity.this, data, R.layout.more_info,
                new String[]{"infoItem"}, new int[]{R.id.infoItem});
        moreInfo.setAdapter(simpleAdapter);

        final ImageView star = (ImageView) findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0){
                    star.setImageResource(R.mipmap.full_star);
                    flag = 1;
                }
                else if (flag == 1){
                    star.setImageResource(R.mipmap.empty_star);
                    flag = 0;
                }
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Map<String, String> name_price = new LinkedHashMap<String, String>();
        name_price.put("Enchated Forest", "¥5.00");
        name_price.put("Arla Milk", "¥59.00");
        name_price.put("Devondale Milk", "¥79.00 ");
        name_price.put("Kindle Oasis", "¥2399.00");
        name_price.put("waitrose 早餐麦片", "¥179.00");
        name_price.put("Mcvitie's 饼干", "¥14.90");
        name_price.put("Ferrero Rocher", "¥132.59 ");
        name_price.put("Maltesers", "¥141.43");
        name_price.put("Lindt", "¥139.43");
        name_price.put("Borggreve", "¥28.90 ");
        Map<String, String> name_info = new LinkedHashMap<String, String>();
        name_info.put("Enchated Forest", "作者 Johanna Basford");
        name_info.put("Arla Milk", "产地 德国");
        name_info.put("Devondale Milk", "产地 澳大利亚");
        name_info.put("Kindle Oasis", "版本 8GB");
        name_info.put("waitrose 早餐麦片", "重量 2Kg");
        name_info.put("Mcvitie's 饼干", "产地 英国");
        name_info.put("Ferrero Rocher", "重量 300g");
        name_info.put("Maltesers", "重量 118g");
        name_info.put("Lindt", "重量 249g");
        name_info.put("Borggreve", "重量 640g");
        Map<String, String> name_pic = new LinkedHashMap<String, String>();
        name_pic.put("Enchated Forest", String.valueOf(R.mipmap.enchatedforest));
        name_pic.put("Arla Milk", String.valueOf(R.mipmap.arla));
        name_pic.put("Devondale Milk", String.valueOf(R.mipmap.devondale));
        name_pic.put("Kindle Oasis", String.valueOf(R.mipmap.kindle));
        name_pic.put("waitrose 早餐麦片", String.valueOf(R.mipmap.waitrose));
        name_pic.put("Mcvitie's 饼干", String.valueOf(R.mipmap.mcvitie));
        name_pic.put("Ferrero Rocher", String.valueOf(R.mipmap.ferrero));
        name_pic.put("Maltesers", String.valueOf(R.mipmap.maltesers));
        name_pic.put("Lindt", String.valueOf(R.mipmap.lindt));
        name_pic.put("Borggreve", String.valueOf(R.mipmap.borggreve));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            passname = bundle.getString("passname");
        }
        TextView shopname = (TextView) findViewById(R.id.shopName);
        shopname.setText(passname);
        String price = name_price.get(passname);
        TextView shopprice = (TextView) findViewById(R.id.shopPrice);
        shopprice.setText(price);
        String info = name_info.get(passname);
        TextView shopinfo = (TextView) findViewById(R.id.shopInfo);
        shopinfo.setText(info);
        ImageView photo = (ImageView) findViewById(R.id.photo);
        photo.setImageResource(Integer.parseInt(name_pic.get(passname)));

        ImageView shopbus = (ImageView) findViewById(R.id.shopBus);
        shopbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewActivity.this, "商品已加入购物车", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(
                        new FirstEvent(passname, name_price.get(passname)));
            }
        });
























    }
}
