package com.example.ecommerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EcommerceDBHelper extends SQLiteOpenHelper
{
    private static String databasename="ecommercedatabase";
    SQLiteDatabase ecommercedatabase;

    public EcommerceDBHelper(Context context)
    {
        super(context,databasename,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table customers (custid integer primary key , cutname text not null , username text not null ,password text not null,gender text not null,birthdate text not null,job text not null);");
        sqLiteDatabase.execSQL("create table orders (ordid integer primary key , orddate text not null , address text not null ,cust_id integer , foreign key(cust_id) references customers (custid));");
        sqLiteDatabase.execSQL("create table categories (catid integer primary key , catname text not null);");
        sqLiteDatabase.execSQL("create table products (proid integer primary key , proname text not null , price text not null , quantity text not null ,cat_id integer , foreign key(cat_id) references categories (catid));");
        sqLiteDatabase.execSQL("create table order_details (ord_id integer  , pro_id integer  ,quantity text not null , foreign key(ord_id) references orders (ordid),foreign key(pro_id) references products (proid));");
        sqLiteDatabase.execSQL("create table shop_cart (cart_id integer primary key , pro_id integer, username text not null ,password text not null,number_of_repeat text not null );");
        sqLiteDatabase.execSQL("create table remember_me (remid integer primary key , username text not null ,password text not null);");

        insert_category("Mobiles & Tablets",sqLiteDatabase);
        insert_category("TVs",sqLiteDatabase);
        insert_category("Camera",sqLiteDatabase);

        insert_product("Iphone 12 pro , 128GB , 6GB Ram,Gold","23999","5","1",sqLiteDatabase);
        insert_product("Samsung Galaxy note 10, 256GB, 8GB Ram, Aura Glow","12499","10","1",sqLiteDatabase);
        insert_product("Oppo A93 , 128GB, 8GB Ram, Magic Blue","4938","11","1",sqLiteDatabase);

        insert_product("Context 50inch 4K ultra HDD LED, smart android TV with remote","3999","4","2",sqLiteDatabase);
        insert_product("Samsung 50inch 4K ultra HD, smart LED TV with Built, inreceiver, Titan","6699","3","2",sqLiteDatabase);

        insert_product("Canon EOS 2000D 18-55is, 24.1MP DSLR, camera Black","5999","7","3",sqLiteDatabase);
        insert_product("Nikon Coolpix B500, 16 MP, compact camera Black","4250","9","3",sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists customers");
        sqLiteDatabase.execSQL("drop table if exists orders");
        sqLiteDatabase.execSQL("drop table if exists categories");
        sqLiteDatabase.execSQL("drop table if exists products");
        sqLiteDatabase.execSQL("drop table if exists order_details");
        sqLiteDatabase.execSQL("drop table if exists remember_me");
        sqLiteDatabase.execSQL("drop table if exists shop_cart");
        onCreate(sqLiteDatabase);
    }

    public void update_pro_quantity(String quantity,String pro_id)
    {
        ecommercedatabase=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("quantity",quantity);
        ecommercedatabase.update("products",row,"proid=?",new String[]{pro_id});
        ecommercedatabase.close();
    }

    public Cursor get_order_details(String ord_id)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={ord_id};
        Cursor cursor=ecommercedatabase.rawQuery("select * from order_details where ord_id=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public void insert_order_details(String ord_id,String pro_id,String quantity)
    {
        ContentValues row=new ContentValues();
        row.put("ord_id",ord_id);
        row.put("pro_id",pro_id);
        row.put("quantity",quantity);
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.insert("order_details",null,row);
        ecommercedatabase.close();
    }

    public Cursor get_in_rememberme(String remid)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={remid};
        Cursor cursor=ecommercedatabase.rawQuery("select * from remember_me where remid=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public void delete_in_remeberme(String remid)
    {
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.delete("remember_me","remid=?",new String[]{remid});
        ecommercedatabase.close();
    }

    public void insert_in_rememberme(String username,String pass)
    {
        ContentValues row=new ContentValues();
        row.put("username",username);
        row.put("password",pass);
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.insert("remember_me",null,row);
        ecommercedatabase.close();
    }

    public void update_shop_cart_quantity(String number_of_repeat,String shopcart_id)
    {
        ecommercedatabase=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("number_of_repeat",number_of_repeat);
        ecommercedatabase.update("shop_cart",row,"cart_id=?",new String[]{shopcart_id});
        ecommercedatabase.close();
    }

    public void delete_shop_cart(String shopcart_id)
    {
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.delete("shop_cart","cart_id=?",new String[]{shopcart_id});
        ecommercedatabase.close();
    }

    public void insert_customer(String name,String username,String pass,String birthdate,String gender,String job)
    {
        ContentValues row=new ContentValues();
        row.put("cutname",name);
        row.put("username",username);
        row.put("password",pass);
        row.put("gender",gender);
        row.put("birthdate",birthdate);
        row.put("job",job);
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.insert("customers",null,row);
        ecommercedatabase.close();
    }

    public Cursor get_order(String ord_date,String address)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={ord_date,address};
        Cursor cursor=ecommercedatabase.rawQuery("select * from orders where orddate=? and address=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public Cursor get_order_by_custid(String cust_id)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={cust_id};
        Cursor cursor=ecommercedatabase.rawQuery("select * from orders where cust_id=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public void insert_order(String ord_date,String cust_id,String address)
    {
        ContentValues row=new ContentValues();
        row.put("orddate",ord_date);
        row.put("address",address);
        row.put("cust_id",cust_id);
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.insert("orders",null,row);
        ecommercedatabase.close();
    }

    public void insert_shop_cart(String pro_id,String username,String pass,String number_of_repeat)
    {
        ContentValues row=new ContentValues();
        row.put("pro_id",pro_id);
        row.put("username",username);
        row.put("password",pass);
        row.put("number_of_repeat",number_of_repeat);
        ecommercedatabase=getWritableDatabase();
        ecommercedatabase.insert("shop_cart",null,row);
        ecommercedatabase.close();
    }

    public Cursor get_customer_username(String username)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={username};
        Cursor cursor=ecommercedatabase.rawQuery("select * from customers where username=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public void update_customer_pass(String username,String newpass)
    {
        ecommercedatabase=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("password",newpass);
        ecommercedatabase.update("customers",row,"username=?",new String[]{username});
        ecommercedatabase.update("shop_cart",row,"username=?",new String[]{username});
        ecommercedatabase.close();
    }

    public Cursor login_customer(String username,String pass)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={username,pass};
        Cursor cursor=ecommercedatabase.rawQuery("select * from customers where username=? and password=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public Cursor get_product(String cat_id)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={cat_id};
        Cursor cursor=ecommercedatabase.rawQuery("select * from products where cat_id=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }
    public Cursor get_product_using_pro_id(String pro_id)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={pro_id};
        Cursor cursor=ecommercedatabase.rawQuery("select * from products where proid=?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }
    public Cursor get_product_using_name(String name)
    {
        ecommercedatabase=getReadableDatabase();
        String[] arg={"%"+name+"%"};
        Cursor cursor=ecommercedatabase.rawQuery("select * from products where proname like ?",arg);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public Cursor get_Allproduct()
    {
        ecommercedatabase=getReadableDatabase();
        Cursor cursor=ecommercedatabase.rawQuery("select * from products",null);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public Cursor get_shop_cart()
    {
        ecommercedatabase=getReadableDatabase();
        Cursor cursor=ecommercedatabase.rawQuery("select * from shop_cart",null);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public Cursor get_category()
    {
        ecommercedatabase=getReadableDatabase();
        Cursor cursor=ecommercedatabase.rawQuery("select * from categories",null);
        cursor.moveToFirst();
        ecommercedatabase.close();
        return cursor;
    }

    public void insert_category(String name,SQLiteDatabase sqLiteDatabase)
    {
        ContentValues row=new ContentValues();
        row.put("catname",name);
        sqLiteDatabase.insert("categories",null,row);
    }
    public void insert_product(String proname,String price,String quantity,String catid,SQLiteDatabase sqLiteDatabase)
    {
        ContentValues row=new ContentValues();
        row.put("proname",proname);
        row.put("price",price);
        row.put("quantity",quantity);
        row.put("cat_id",catid);
        sqLiteDatabase.insert("products",null,row);
    }
}
