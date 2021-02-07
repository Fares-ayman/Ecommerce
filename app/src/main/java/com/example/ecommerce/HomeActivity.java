package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    String username;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Customer customer=(Customer) getIntent().getSerializableExtra("customer");
        username=customer.username;
        pass=customer.pass;

        final EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);

        List<String> SpinnerArray =  new ArrayList<String>();
        Cursor cursor=ecommerceDBHelper.get_category();
        while (!cursor.isAfterLast())
        {
            SpinnerArray.add(cursor.getString(1));
            cursor.moveToNext();
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner cat_Items = (Spinner) findViewById(R.id.category_id);
        cat_Items.setAdapter(adapter);

        cat_Items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<Product> pro_items=new ArrayList<Product>();
                Cursor cursor =ecommerceDBHelper.get_product(String.valueOf(i+1));

                ArrayList<String> pro_id_shop_cart=new ArrayList<String>();
                Cursor cursor1=ecommerceDBHelper.get_shop_cart();
                while (!cursor1.isAfterLast())
                {
                    if (customer.username.equals(cursor1.getString(2)) && customer.pass.equals(cursor1.getString(3)))
                    {
                        pro_id_shop_cart.add(cursor1.getString(1));
                    }
                    cursor1.moveToNext();
                }

                while(!cursor.isAfterLast())
                {
                    int drawable=R.drawable.register_botton_style;
                    for (int j=0;j<pro_id_shop_cart.size();j++)
                    {
                        if (pro_id_shop_cart.get(j).equals(cursor.getString(0)))
                        {
                            drawable=R.drawable.login_botton_style;
                            break;
                        }
                    }
                    Product product=new Product(cursor.getString(1),cursor.getString(2)+" EGY",cursor.getString(3),"a"+cursor.getString(0),cursor.getString(0),drawable,"0","0");
                    pro_items.add(product);
                    cursor.moveToNext();
                }

                MyProductsAdapter myProductsAdapter=new MyProductsAdapter(pro_items,customer.username,customer.pass);
                ListView pro_listview=(ListView) findViewById(R.id.pro_listview_id);
                pro_listview.setAdapter(myProductsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    void insert_in_cart(String id, String username, String pass)
    {
        EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);
        ecommerceDBHelper.insert_shop_cart(id,username,pass,"1");
    }

    class MyProductsAdapter extends BaseAdapter
    {
        ArrayList<Product> pro_items=new ArrayList<Product>();
        String username;
        String pass;
        public MyProductsAdapter(ArrayList<Product> pro_items,String username,String pass)
        {
            this.pro_items=pro_items;
            this.username=username;
            this.pass=pass;
        }

        @Override
        public int getCount() {
            return pro_items.size();
        }

        @Override
        public Object getItem(int i) {
            return pro_items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=getLayoutInflater();
            View view1=inflater.inflate(R.layout.row_item,null);

            ImageView pro_img=(ImageView) view1.findViewById(R.id.pro_img_id);
            TextView pro_name=(TextView) view1.findViewById(R.id.pro_name_id);
            TextView pro_price=(TextView) view1.findViewById(R.id.pro_price_id);
            final Button pro_cart=(Button) view1.findViewById(R.id.pro_cart_id);
            pro_cart.setBackgroundResource(pro_items.get(i).drawable);
            if (pro_items.get(i).drawable==R.drawable.register_botton_style)
                pro_cart.setTextColor(Color.WHITE);
            else
                pro_cart.setTextColor(0xFF677af5);

            pro_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(pro_items.get(i).quantity)==0)
                    {
                        Toast.makeText(getApplicationContext(),"This product over",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        pro_cart.setBackgroundResource(R.drawable.login_botton_style);
                        pro_cart.setTextColor(0xFF677af5);
                        pro_items.get(i).drawable=R.drawable.login_botton_style;
                        insert_in_cart(pro_items.get(i).id,username,pass);
                    }
                }
            });
            pro_img.setImageResource(getResources().getIdentifier(pro_items.get(i).img, "drawable", getPackageName()));
            pro_name.setText(pro_items.get(i).name);
            pro_price.setText(pro_items.get(i).price);
            return view1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.shoppingcartmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.cart_id:
                Intent intent=new Intent(HomeActivity.this,ShoppingCartActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("pass",pass);
//                intent.putExtra("address","null");
                finish();
                startActivity(intent);
                return true;
            case R.id.search_id:
                Intent intent2=new Intent(HomeActivity.this,SearchActivity.class);
                intent2.putExtra("username",username);
                intent2.putExtra("pass",pass);
                startActivity(intent2);
                return  true;
            case R.id.order_chart_id:
                Intent intent3=new Intent(HomeActivity.this,OrderChartActivity.class);
                intent3.putExtra("username",username);
                intent3.putExtra("pass",pass);
                startActivity(intent3);
                return  true;
            case R.id.logout_id:
                EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(getApplicationContext());
                ecommerceDBHelper.delete_in_remeberme("1");
                Intent intent1=new Intent(HomeActivity.this,MainActivity.class);
                finish();
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
