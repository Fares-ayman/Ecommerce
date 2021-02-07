package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(getApplicationContext());
        Cursor cursor=ecommerceDBHelper.get_in_rememberme("1");
        int count=0;
        while(!cursor.isAfterLast())
        {
            count++;
            cursor.moveToNext();
        }
        cursor.moveToFirst();

        if (count==0)
        {
            Button register=(Button)findViewById(R.id.register_id);
            Button login=(Button)findViewById(R.id.login_id);


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                    finish();
                    startActivity(intent);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
        else
        {
            Cursor cursor1 =ecommerceDBHelper.login_customer(cursor.getString(1),cursor.getString(2));
            Customer customer=new Customer(cursor1.getString(1),cursor1.getString(2),cursor1.getString(3),cursor1.getString(4),cursor1.getString(5),cursor1.getString(6));
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            intent.putExtra("customer",customer);
            finish();
            startActivity(intent);
        }

    }
}
