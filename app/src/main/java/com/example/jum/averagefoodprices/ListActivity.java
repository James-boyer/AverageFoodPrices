package com.example.jum.averagefoodprices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Comparator;
import java.util.List;

/**
 * Created by James boyer
 */
public class ListActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final Intent here = getIntent();
        Wrapper wrap = (Wrapper)here.getSerializableExtra("Items");
        List<Item> items = wrap.getList();
        ListView foodList = (ListView) findViewById(R.id.foodList);
        final ArrayAdapter<Item> itemAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
        foodList.setAdapter(itemAdapter);
        itemAdapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item l, Item r) {
                return l.getItemName().compareTo(r.getItemName());
            }
        });

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemact = new Intent(getApplicationContext(), ItemActivity.class);
                Item obj = (Item) parent.getItemAtPosition(position);
                itemact.putExtra("Item", obj);
                itemact.putExtra("Items",(Wrapper)here.getSerializableExtra("Items"));
                startActivity(itemact);
            }
        });
        final Button sortbyName = (Button)findViewById(R.id.SortByName);
        sortbyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemAdapter.getItem(1).getItemName().compareToIgnoreCase("m") > 0) {
                    itemAdapter.sort(new Comparator<Item>() {
                        @Override
                        public int compare(Item l, Item r) {
                            return l.getItemName().compareTo(r.getItemName());
                        }
                    });
                } else {
                    itemAdapter.sort(new Comparator<Item>() {
                        @Override
                        public int compare(Item l, Item r) {
                            return r.getItemName().compareTo(l.getItemName());
                        }
                    });
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.list_about) {
            new AlertDialog.Builder(this).setMessage(R.string.menu_about_msg).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }else if (id==R.id.list_help){
            new AlertDialog.Builder(this).setMessage(R.string.list_menu_help).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
