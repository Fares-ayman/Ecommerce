package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);

        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final TextView date_edit=(TextView) findViewById(R.id.date_id);
        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date_edit.setText(i+"/"+i1+"/"+i2);
                    }
                },year,month,day);
                datePickerDialog.setTitle("Select Your Birthdate");
                datePickerDialog.show();
            }
        });

        Button register=(Button)findViewById(R.id.enter_register_id);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstname=(EditText)findViewById(R.id.firstname_id);
                EditText lastname=(EditText)findViewById(R.id.lastname_id);
                String name=firstname.getText().toString()+" "+lastname.getText().toString();

                EditText username_edit=(EditText)findViewById(R.id.username_register_id);
                String username=username_edit.getText().toString();

                EditText pass_edit=(EditText)findViewById(R.id.pass_register_id);
                String pass=pass_edit.getText().toString();

                String birthdate=date_edit.getText().toString();

                String gender="";
                Spinner genderoption=(Spinner)findViewById(R.id.gender_id);
                gender=genderoption.getSelectedItem().toString();

                EditText job_edit=(EditText)findViewById(R.id.job_id);
                String job=job_edit.getText().toString();

                ecommerceDBHelper.insert_customer(name,username,pass,birthdate,gender,job);

                Customer customer=new Customer(name,username,pass,gender,birthdate,job);

                ecommerceDBHelper.insert_in_rememberme(customer.username,customer.pass);
                Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                intent.putExtra("customer", customer);
                finish();
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Register Completed..",Toast.LENGTH_LONG).show();
            }
        });
        TextView login=(TextView)findViewById(R.id.login_txt_id);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

}
