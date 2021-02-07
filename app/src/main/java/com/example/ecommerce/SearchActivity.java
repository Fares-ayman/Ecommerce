package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    ArrayList<String> barcode;
    String customer_username;
    String customer_pass;
    EcommerceDBHelper ecommerceDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        barcode=new ArrayList<String>();
        barcode.add("123456789012");
        barcode.add("025192270147");
        barcode.add("792266086200");
        barcode.add("725272730706");
        barcode.add("025192381690");
        barcode.add("824302385616");
        barcode.add("691768001820");
        customer_username=getIntent().getExtras().getString("username");
        customer_pass=getIntent().getExtras().getString("pass");
        ecommerceDBHelper=new EcommerceDBHelper(getApplicationContext());

        Button  search_btn=(Button)findViewById(R.id.enter_search_id);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText search_edit_text=(EditText)findViewById(R.id.search_edittext_id);
                ArrayList<Product> pro_items=new ArrayList<Product>();
                Cursor cursor =ecommerceDBHelper.get_product_using_name(search_edit_text.getText().toString());

                if (cursor.getCount()==0)
                {
                    Toast.makeText(getApplicationContext(),"There is no item with this name",Toast.LENGTH_LONG).show();
                }

                ArrayList<String> pro_id_shop_cart=new ArrayList<String>();
                Cursor cursor1=ecommerceDBHelper.get_shop_cart();

                while (!cursor1.isAfterLast())
                {
                    if (customer_username.equals(cursor1.getString(2)) && customer_pass.equals(cursor1.getString(3)))
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

                MyProductsAdapter myProductsAdapter=new MyProductsAdapter(pro_items,customer_username,customer_pass);
                ListView pro_listview=(ListView) findViewById(R.id.search_listview_id);
                pro_listview.setAdapter(myProductsAdapter);
            }
        });
    }

    void insert_in_cart(String id, String username, String pass)
    {
        EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);
        ecommerceDBHelper.insert_shop_cart(id,username,pass,"1");
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EditText search_edit_text=(EditText)findViewById(R.id.search_edittext_id);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_edit_text.setText(result.get(0));
                }
                break;
            default:
                IntentResult result =IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
                if (result!=null)
                {
                    if (result.getContents()!=null)
                    {
                        int index=-1;
                        for (int i=0;i<barcode.size();i++)
                        {
                            if (barcode.get(i).equals(result.getContents()))
                            {
                                index=i+1;
                                break;
                            }
                        }
                        ArrayList<Product> pro_items=new ArrayList<Product>();
                        Cursor cursor =ecommerceDBHelper.get_product_using_pro_id(String.valueOf(index));

                        if (cursor.getCount()==0)
                        {
                            Toast.makeText(getApplicationContext(),"There is no item with this Barcode",Toast.LENGTH_LONG).show();
                        }

                        ArrayList<String> pro_id_shop_cart=new ArrayList<String>();
                        Cursor cursor1=ecommerceDBHelper.get_shop_cart();

                        while (!cursor1.isAfterLast())
                        {
                            if (customer_username.equals(cursor1.getString(2)) && customer_pass.equals(cursor1.getString(3)))
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

                        MyProductsAdapter myProductsAdapter=new MyProductsAdapter(pro_items,customer_username,customer_pass);
                        ListView pro_listview=(ListView) findViewById(R.id.search_listview_id);
                        pro_listview.setAdapter(myProductsAdapter);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Results",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    super.onActivityResult(requestCode,resultCode,data);
                }
                break;
        }
    }

    public void getcameraInput(View view) {
        scancode();
    }
    private void scancode()
    {
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("scanning code");
        integrator.initiateScan();
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
}
