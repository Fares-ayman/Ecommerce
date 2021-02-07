package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderChartActivity extends AppCompatActivity {

    PieChart pieChart;
    String customer_id;
    ArrayList<String> pro_name;
    ArrayList<String> quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_chart);

        pro_name=new ArrayList<String>();
        quantity=new ArrayList<String>();

        pro_name.add("Iphone 12 pro");
        pro_name.add("Samsung Galaxy note 10");
        pro_name.add("Oppo A93");
        pro_name.add("Context 50inch Smart tv");
        pro_name.add("Samsung 50inch Smart tv");
        pro_name.add("Canon EOS 2000D Camera");
        pro_name.add("Nikon Coolpix B500 Camera");

        final String customer_username=getIntent().getExtras().getString("username");
        final String customer_pass=getIntent().getExtras().getString("pass");

        final EcommerceDBHelper ecommerceDBHelper=new EcommerceDBHelper(this);
        Cursor cursor=ecommerceDBHelper.login_customer(customer_username,customer_pass);
        customer_id=cursor.getString(0);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String[] parts =formattedDate.split("-");
        String current_date=parts[1];


        ArrayList<String>order_id=new ArrayList<String>();
        cursor=ecommerceDBHelper.get_order_by_custid(customer_id);
        while (!cursor.isAfterLast())
        {
            String orderdate=cursor.getString(1);
            parts =orderdate.split("-");
            String pro_date=parts[1];
            if (current_date.equals(pro_date))
            {
                order_id.add(cursor.getString(0));
            }
            cursor.moveToNext();
        }

        ArrayList<String>pro_id=new ArrayList<String>();
        for (int i=0;i<order_id.size();i++)
        {
            cursor=ecommerceDBHelper.get_order_details(order_id.get(i));
            while (!cursor.isAfterLast())
            {
                int find=pro_id.indexOf(cursor.getString(1));
                if (find!=-1)
                {
                    pro_id.add(cursor.getString(1));
                    quantity.add(String.valueOf(Integer.parseInt(quantity.get(find))+Integer.parseInt(cursor.getString(2))));
                    quantity.remove(find);
                    pro_id.remove(find);
                }
                else
                {
                    pro_id.add(cursor.getString(1));
                    quantity.add(cursor.getString(2));
                }
                cursor.moveToNext();
            }
        }


        pieChart =(PieChart)findViewById(R.id.chart_id);

        pieChart.setExtraOffsets(5,10,5,20);
//        pieChart.setEntryLabelColor(Color.BLACK);
//        pieChart.setEntryLabelTextSize(10f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setCenterText("Products Quantity");
        pieChart.setCenterTextSize(20f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(30f);

        ArrayList<PieEntry> pieEntries=new ArrayList<PieEntry>();
        for (int i=0;i<pro_id.size();i++)
        {
            int index= Integer.parseInt(pro_id.get(i))-1;
            pieEntries.add(new PieEntry(Integer.parseInt(quantity.get(i)),pro_name.get(index)));
        }

        Description description =new Description();
        description.setText("This pie chart shows the products purchased in a month "+current_date+"\n");
        description.setTextSize(11);
        description.setTextAlign(Paint.Align.RIGHT);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet pieDataSet=new PieDataSet(pieEntries,"");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        ArrayList<Integer>colors=new ArrayList<Integer>();
        colors.add(Color.rgb(217, 80, 138));
        colors.add(Color.rgb(254, 149, 7));
        colors.add(Color.rgb(254, 247, 120));
        colors.add(Color.rgb(106, 167, 134));
        colors.add(Color.rgb(53, 194, 209));
        colors.add(Color.GRAY);
        colors.add(Color.LTGRAY);

        pieDataSet.setColors(colors);

        PieData pieData=new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
    }
}
