package com.example.ecommerce;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ShoppingCartActivity extends AppCompatActivity {

    String total_price;
    TextView total_price_txt;
    EcommerceDBHelper ecommerceDBHelper;
    String customer_username;
    String customer_pass;
    String gmail;
    String etsubject;
    String etmessage;
    String semail;
    String spassword;
    ArrayList<Product> pro_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ecommerceDBHelper=new EcommerceDBHelper(this);
        customer_username=getIntent().getExtras().getString("username");
        customer_pass=getIntent().getExtras().getString("pass");

        pro_items=new ArrayList<Product>();
        Cursor cursor =ecommerceDBHelper.get_Allproduct();

        ArrayList<String> id_shop_cart=new ArrayList<String>();
        final ArrayList<String> pro_id_shop_cart=new ArrayList<String>();
        ArrayList<String> pro_quantity_shop_cart=new ArrayList<String>();
        Cursor cursor1=ecommerceDBHelper.get_shop_cart();
        while (!cursor1.isAfterLast())
        {
            if (customer_username.equals(cursor1.getString(2)) && customer_pass.equals(cursor1.getString(3)))
            {
                id_shop_cart.add(cursor1.getString(0));
                pro_id_shop_cart.add(cursor1.getString(1));
               // Toast.makeText(getApplicationContext(),cursor1.getString(1),Toast.LENGTH_LONG).show();
                pro_quantity_shop_cart.add(cursor1.getString(4));
            }
            cursor1.moveToNext();
        }
        int count=0;
        while(!cursor.isAfterLast())
        {
            for (int j=0;j<pro_id_shop_cart.size();j++)
            {
                if (pro_id_shop_cart.get(j).equals(cursor.getString(0)))
                {
                    Product product=new Product(cursor.getString(1),cursor.getString(2)+" EGY",cursor.getString(3),"a"+cursor.getString(0),cursor.getString(0),0,pro_quantity_shop_cart.get(j),id_shop_cart.get(j));
                    pro_items.add(product);
                    count+=Integer.parseInt(cursor.getString(2))*Integer.parseInt(pro_quantity_shop_cart.get(j));
                    break;
                }
            }
            cursor.moveToNext();
        }
        MyProductsshopcartAdapter myProductsAdapter=new MyProductsshopcartAdapter(pro_items,customer_username,customer_pass);
        ListView shopcart_pro_listview=(ListView) findViewById(R.id.shopcart_listview_id);
        shopcart_pro_listview.setAdapter(myProductsAdapter);

        total_price_txt=(TextView)findViewById(R.id.shopcart_price_id);
        Button order_btn=(Button)findViewById(R.id.shopcart_order_id);
        total_price=String.valueOf(count);
        total_price_txt.setText(total_price);

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pro_items.size()==0)
                {
                    Toast.makeText(getApplicationContext(),"Choose the products you want to order",Toast.LENGTH_LONG).show();
                }
                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ShoppingCartActivity.this);
                    builder.setTitle("Enter Your Gmail");
                    final EditText gmail_txt=new EditText(ShoppingCartActivity.this);
                    gmail_txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    gmail_txt.setBackgroundResource(R.drawable.edittext_style);

                    gmail_txt.setSingleLine();
                    FrameLayout container = new FrameLayout(ShoppingCartActivity.this);
                    FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin=40;
                    params.topMargin=30;
                    params.rightMargin=20;
                    gmail_txt.setLayoutParams(params);
                    container.addView(gmail_txt);

                    builder.setView(container);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gmail=gmail_txt.getText().toString();

                            etsubject="Order confirmation";
                            etmessage="The products you purchased are \n";
                            for (int j=0;j<pro_items.size();j++)
                            {
                                etmessage+="* "+pro_items.get(j).name+"\n";
                            }
                            semail="*************@gmail.com";
                            spassword="*************";

                            Properties properties=new Properties();
                            properties.put("mail.smtp.auth","true");
                            properties.put("mail.smtp.starttls.enable","true");
                            properties.put("mail.smtp.host","smtp.gmail.com");
                            properties.put("mail.smtp.port","587");

                            Session session=Session.getInstance(properties, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(semail,spassword);
                                }
                            });

                            try {

                                Message message=new MimeMessage(session);
                                message.setFrom(new InternetAddress(semail));
                                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(gmail.trim()));
                                message.setSubject(etsubject.trim());
                                message.setText(etmessage.trim());

                                new sendemail().execute(message);

                            }catch (MessagingException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"You must enter gmail to request the order",Toast.LENGTH_LONG).show();
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
                }
        });
    }

    private  class sendemail extends AsyncTask<Message,String,String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(ShoppingCartActivity.this,"Please Wait","Sending Mail....",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";

            }catch (MessagingException e)
            {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            if (s.equals("Success"))
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(ShoppingCartActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Success</font>"));
                builder.setMessage("Mail Send Successfully");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent=new Intent(ShoppingCartActivity.this, MapActivity.class);
                        intent.putExtra("username",customer_username);
                        intent.putExtra("pass",customer_pass);
                        intent.putExtra("pro_id",pro_items);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something Went Wrong ?",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Cursor cursor =ecommerceDBHelper.login_customer(customer_username,customer_pass);
        Customer customer=new Customer(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
        Intent intent=new Intent(ShoppingCartActivity.this,HomeActivity.class);
        intent.putExtra("customer",customer);
        finish();
        startActivity(intent);
    }

    public void add_btn(String price)
    {
        int temp=0;
        temp+=Integer.parseInt(price);
        temp+=Integer.parseInt(total_price);
        total_price=String.valueOf(temp);
        total_price_txt.setText(total_price);
    }

    public void sub_btn(String price)
    {
        int temp=0;
        temp+=Integer.parseInt(total_price);
        temp-=Integer.parseInt(price);
        total_price=String.valueOf(temp);
        total_price_txt.setText(total_price);
    }

    class MyProductsshopcartAdapter extends BaseAdapter
    {
        ArrayList<Product> pro_items=new ArrayList<Product>();
        String username;
        String pass;
        public MyProductsshopcartAdapter(ArrayList<Product> pro_items,String username,String pass)
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
            View view1=inflater.inflate(R.layout.activity_shoppingcart_row_item,null);

            ImageView pro_img=(ImageView) view1.findViewById(R.id.shopcart_imageview_id);
            TextView pro_name=(TextView) view1.findViewById(R.id.shopcart_proname_id);
            TextView pro_price=(TextView) view1.findViewById(R.id.shopcart_proprice_id);
            final TextView pro_quantity=(TextView) view1.findViewById(R.id.shopcart_quantity_id);
            Button add_btn=(Button) view1.findViewById(R.id.shopcart_add_id);
            Button sub_btn=(Button) view1.findViewById(R.id.shopcart_sub_id);

            pro_img.setImageResource(getResources().getIdentifier(pro_items.get(i).img, "drawable", getPackageName()));
            pro_name.setText(pro_items.get(i).name);
            pro_price.setText(pro_items.get(i).price);
            pro_quantity.setText(pro_items.get(i).num_of_repeat);

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((Integer.parseInt(pro_items.get(i).num_of_repeat)+1)<=Integer.parseInt(pro_items.get(i).quantity))
                    {
                        pro_items.get(i).num_of_repeat=String.valueOf((Integer.parseInt(pro_items.get(i).num_of_repeat)+1));
                        pro_quantity.setText(pro_items.get(i).num_of_repeat);
                      //  Toast.makeText(getApplicationContext(),pro_items.get(i).id,Toast.LENGTH_LONG).show();
                        ecommerceDBHelper.update_shop_cart_quantity(pro_items.get(i).num_of_repeat,pro_items.get(i).shopcart_id);
                        String [] price=pro_items.get(i).price.split(" ");
                        add_btn(price[0]);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Sorry, you exceeded the available quantity",Toast.LENGTH_LONG).show();
                    }
                }
            });

            sub_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((Integer.parseInt(pro_items.get(i).num_of_repeat)-1)>0)
                    {
                        pro_items.get(i).num_of_repeat=String.valueOf((Integer.parseInt(pro_items.get(i).num_of_repeat)-1));
                        pro_quantity.setText(pro_items.get(i).num_of_repeat);
                        ecommerceDBHelper.update_shop_cart_quantity(pro_items.get(i).num_of_repeat,pro_items.get(i).shopcart_id);
                        String [] price=pro_items.get(i).price.split(" ");
                        sub_btn(price[0]);
                    }
                    else
                    {
                        ecommerceDBHelper.delete_shop_cart(pro_items.get(i).shopcart_id);
                        String [] price=pro_items.get(i).price.split(" ");
                        sub_btn(price[0]);
                        pro_items.remove(i);
                        Toast.makeText(getApplicationContext(),"The item has been deleted",Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
                }
            });
            return view1;
        }
    }
}
