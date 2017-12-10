package com.example.zhengjiafeng.lab2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private List<String> mDatas;
    private int tag = 0;
     public List<Map<String, String>> data = new ArrayList<Map<String, String>>();
     public SimpleAdapter simpleAdapter;
    Map<String, String> name_price = new LinkedHashMap<String, String>();
    Map<String, String> name_info = new LinkedHashMap<String, String>();
    Map<String, String> name_pic = new LinkedHashMap<String, String>();
    DynamicReceiver  dynamicReceiver = new DynamicReceiver();
     RecyclerView myRecView = null;
     ListView shopList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        initData();

        Random random = new Random();
        int ranNum = random.nextInt(mDatas.size());
        Intent intent = new Intent();
        intent.setAction("MLY");
        intent.putExtra("name",mDatas.get(ranNum));
        intent.putExtra("price", name_price.get(mDatas.get(ranNum)));
        intent.putExtra("picture", name_pic.get(mDatas.get(ranNum)));
        sendBroadcast(intent);

        final HomeAdapter myAdapter = new HomeAdapter();
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(myAdapter);
        animationAdapter.setDuration(1000);
        myRecView = (RecyclerView) findViewById(R.id.id_recyclerview);
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(animationAdapter);
        myRecView.setItemAnimator(new OvershootInLeftAnimator());

        myAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                String extra = mDatas.get(position);
                intent.putExtra("passname", extra);
                MainActivity.this.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                Toast.makeText(MainActivity.this, "移除第" + position + "个商品",
                        Toast.LENGTH_SHORT).show();
                myAdapter.removeData(position);
            }
        });
        shopList = (ListView) findViewById(R.id.shoppinglist);
        String[] goods = new String[]{"购物车"};
        String[] price = new String[]{"价格"};
        for (int i = 0; i < goods.length; i++) {
            Map<String, String> temp = new LinkedHashMap<String, String>();
            temp.put("goods", goods[i]);
            temp.put("price", price[i]);
            if (goods[i] == "购物车"){
                temp.put("shopButton", "*");
            }
            else{
                temp.put("shopButton", goods[i].substring(0, 1));
            }
            data.add(temp);
        }
        simpleAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.item_shop,
                new String[]{"goods", "price","shopButton"}, new int[]{R.id.goods, R.id.price, R.id.shopButton});
        shopList.setAdapter(simpleAdapter);
        shopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){}
                else {
                    Intent intent = new Intent(MainActivity.this, NewActivity.class);
                    Map<String, String> extra = data.get(position);
                    intent.putExtra("passname", extra.get("goods"));
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        shopList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);
                dialogbuilder.setTitle("上传头像");
                String temp = data.get(arg2).get("goods");
                dialogbuilder.setMessage("从购物车移除" + temp +"?");
                dialogbuilder.setNegativeButton("取消", click1);
                dialogbuilder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.remove(arg2);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = dialogbuilder.create();
                dialog.show();
                return true;
            }
            private DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface arg0, int arg1){
                    arg0.cancel();
                }
            };
        });
        shopList.setVisibility(View.INVISIBLE);
        final FloatingActionButton floatBtn = (FloatingActionButton) findViewById(R.id.floatButton);
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag == 0) {
                    myRecView.setVisibility(View.INVISIBLE);
                    shopList.setVisibility(View.VISIBLE);
                    floatBtn.setImageResource(R.mipmap.mainpage);
                    tag = 1;
                }
                else if (tag == 1){
                    shopList.setVisibility(View.INVISIBLE);
                    myRecView.setVisibility(View.VISIBLE);
                    floatBtn.setImageResource(R.mipmap.shoplist);
                    tag = 0;
                }
            }
        });
              IntentFilter filter = new IntentFilter();
              filter.addAction("shop");
             registerReceiver(dynamicReceiver,filter);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            myRecView.setVisibility(View.INVISIBLE);
            shopList.setVisibility(View.VISIBLE);
        }
    }
    private void initData(){
        mDatas = new ArrayList<String>();
        mDatas.add("Enchated Forest");
        mDatas.add("Arla Milk");
        mDatas.add("Devondale Milk");
        mDatas.add("Kindle Oasis");
        mDatas.add("waitrose 早餐麦片");
        mDatas.add("Mcvitie's 饼干");
        mDatas.add("Ferrero Rocher");
        mDatas.add("Maltesers");
        mDatas.add("Lindt");
        mDatas.add("Borggreve");
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
    }
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        unregisterReceiver(dynamicReceiver);
    }



    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("goods", event.getNamemsg());
        temp.put("price", event.getPricemsg());
        temp.put("shopButton", event.getNamemsg().substring(0,1));
        data.add(temp);
        simpleAdapter.notifyDataSetChanged();
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
             MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
            holder.btn.setText(mDatas.get(position).substring(0,1));
            if (mOnItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;
            Button btn;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
                btn = (Button) view.findViewById(R.id.button);
            }
        }
    }




























}
