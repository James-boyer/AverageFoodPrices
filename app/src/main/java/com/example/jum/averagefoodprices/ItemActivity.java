package com.example.jum.averagefoodprices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by James boyer
 */
public class ItemActivity extends ActionBarActivity {
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        final Intent here =getIntent();
        item = (Item)here.getSerializableExtra("Item");
        TextView tv = (TextView) findViewById(R.id.itemInfo);
        String display = item.getItemName() +"\n";
        display+= "Weight: " +item.getWeightString() +"\n";
        display+=item.getRecentMonth();

        tv.setText(display);

        Button convert = (Button) findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double num = item.getWeightNum();
                String measurement = item.getMeasurement();

                EditText ed = (EditText)findViewById(R.id.priceInput);
                double newWeight = Double.parseDouble(ed.getText().toString());
                double priceperunit = item.getRecentPriceMonth() /num;
                double res = newWeight * priceperunit;
                String result = new DecimalFormat("#.##").format(res);
                TextView tv = (TextView) findViewById(R.id.priceResult);
                tv.setText("Price for "+newWeight+ " " + measurement+ " $" + result);
            }
        });

        //added because items with spaces were not accepted in the site
        String name = item.getItemName().replace(" ","%20");
        //added because longer names were not correctly finding the right picture
        if(name.length() > 20){
            name = name.split(",")[0];
        }

        boolean isconn = false;

        ConnectivityManager m = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo info = m.getActiveNetworkInfo();
        if(info != null && info.isConnected()) isconn = true;
        if(isconn) {
            new DownloadImage((ImageView) findViewById(R.id.imageView)).
                    execute("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=\"" + name + "\"&rsz=4");
        }

        Button ph = (Button) findViewById(R.id.historyView);
        ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pricehistory = new Intent(getApplicationContext(),HistoryActivity.class);
                pricehistory.putExtra("item",item);

                pricehistory.putExtra("Items", (Wrapper) here.getSerializableExtra("Items"));
                startActivity(pricehistory);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_about) {
            new AlertDialog.Builder(this).setMessage(R.string.menu_about_msg).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }else if (id==R.id.item_help){
            new AlertDialog.Builder(this).setMessage(R.string.item_menu_help).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
