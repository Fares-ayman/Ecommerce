package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView forgot_pass_txt=(TextView)findViewById(R.id.forgot_pass_id);
        forgot_pass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                finish();
                startActivity(intent);
            }
        });

        final EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);

        Button login=(Button)findViewById(R.id.enter_id);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username_edit=(EditText)findViewById(R.id.username_id);
                String username=username_edit.getText().toString();

                EditText pass_edit=(EditText)findViewById(R.id.pass_id);
                String pass=pass_edit.getText().toString();

                int count=0;
                Cursor cursor=ecommerceDBHelper.login_customer(username,pass);
                while(!cursor.isAfterLast())
                {
                    count++;
                    cursor.moveToNext();
                }
                cursor.moveToFirst();

                if (count==0)
                    Toast.makeText(getApplicationContext(),"Username or Password May be wrong",Toast.LENGTH_LONG).show();
                else
                {
                    Customer customer=new Customer(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                    ecommerceDBHelper.insert_in_rememberme(customer.username,customer.pass);
                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    intent.putExtra("customer",customer);
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
        TextView register=(TextView)findViewById(R.id.register_txt_id);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
