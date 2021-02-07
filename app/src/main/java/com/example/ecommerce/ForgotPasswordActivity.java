package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        Button forgot_pass_btn=(Button)findViewById(R.id.forgot_pass_enter_id);
        final EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(getApplicationContext());

        forgot_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText forgot_pass_username=(EditText)findViewById(R.id.forgot_pass_username_id);
                EditText forgot_pass_newpass=(EditText)findViewById(R.id.forgot_pass_newpass_id);
                int count=0;
                Cursor cursor=ecommerceDBHelper.get_customer_username(forgot_pass_username.getText().toString());
                while(!cursor.isAfterLast())
                {
                    count++;
                    cursor.moveToNext();
                }
                cursor.moveToFirst();

                if (count==0)
                    Toast.makeText(getApplicationContext(),"Username May be wrong",Toast.LENGTH_LONG).show();
                else
                {
                    ecommerceDBHelper.update_customer_pass(forgot_pass_username.getText().toString(),forgot_pass_newpass.getText().toString());
                    Toast.makeText(getApplicationContext(),"The password has been changed successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        });
    }
}
