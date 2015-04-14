package com.example.jum.averagefoodprices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity {
    public final String dataseturl = "http://www.statcan.gc.ca/cgi-bin/sum-som/fl/cstsaveascsv.cgi?filename=econ153a-eng.htm&lan=eng";
    public String datasetdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final TextView text = (TextView) findViewById(R.id.textView);


        //Network task in separate thread because android application would crash if it was on main thread
        Thread networkthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String path = Environment.getExternalStorageDirectory() + "/fooddata";
                    File dir = new File(path);
                    File bir;
                    dir.mkdirs();
                    path += "/food.txt";
                    bir = new File(path);


                    URL site = new URL(dataseturl);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(site.openStream()));
                    FileWriter fo = new FileWriter(bir);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        fo.write(line + "\n");
                    }

                    fo.close();


                } catch (MalformedURLException e) {
                    text.setText(e.toString());
                } catch (IOException e) {
                    text.setText(e.toString());
                }


            }
        });

        networkthread.start();
        while (networkthread.isAlive()) {

        }
        final List<Item> items = datatoitems();
        //text.setText(items.elementAt(42)+"");
        text.setText("Use the text box to find a food item, or browse all items by clicking the button below to go to the list view ");
        final AutoCompleteTextView ac = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<Item> itemAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
        ac.setAdapter(itemAdapter);

        ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemact = new Intent(getApplicationContext(), ItemActivity.class);
                Item obj = (Item) parent.getItemAtPosition(position);
                itemact.putExtra("Item", obj);
                Wrapper wrap = new Wrapper(items);
                itemact.putExtra("Items", wrap);
                startActivity(itemact);
            }
        });
        Button toList = (Button)findViewById(R.id.toList);
        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listact = new Intent(getApplicationContext(), ListActivity.class);
                Wrapper wrap = new Wrapper(items);
                listact.putExtra("Items", wrap);
                startActivity(listact);
            }
        });
    }
    public List<Item> datatoitems(){
        List<Item> items = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("/storage/sdcard/fooddata/food.txt"));
            String line;
            String tmp[];
            String monthnames[] = new String[1];
            int months;
            int count = 0;
            while((line = reader.readLine()) != null){

                if(line.contains("2014")){
                    tmp = line.split(",");
                    monthnames = new String[tmp.length];
                    for(int i = 2; i<tmp.length;i++) {
                        monthnames[i - 2] = tmp[i];
                    }
                }else if(monthnames.length >5){
                    tmp = line.split(",");
                    if(tmp.length >= monthnames.length && tmp[3].matches("[0-9]*\\.?[0-9]+")){
                        double p[] = new double[monthnames.length-2];
                        if(tmp[0].contains("\"")) {
                            for(int i = 0;i<monthnames.length-2;i++){

                                p[i] = Double.parseDouble(tmp[i+3]);


                            }
                            items.add(new Item((tmp[0] +","+ tmp[1]).replace("\"",""),monthnames,p,monthnames.length - 2, tmp[2]));
                        }else{
                            for(int i = 0;i<monthnames.length-2;i++){
                                p[i] = Double.parseDouble(tmp[i+2]);
                            }
                            items.add(new Item(tmp[0], monthnames,p, monthnames.length - 2, tmp[1]));
                        }
                        count++;

                    }
                }

            }

        }catch (IOException e){

        }
        return items;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_about) {
            new AlertDialog.Builder(this).setMessage(R.string.menu_about_msg).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }else if (id==R.id.search_help){
            new AlertDialog.Builder(this).setMessage(R.string.search_menu_help).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
