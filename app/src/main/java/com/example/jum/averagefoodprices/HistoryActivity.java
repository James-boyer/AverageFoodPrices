package com.example.jum.averagefoodprices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by James boyer
 */
public class HistoryActivity extends ActionBarActivity {
    public Item item;
    public double curRangeMax;
    public double curRangeMin;
    public HashMap<Item,XYSeries> allSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        allSeries = new HashMap<Item,XYSeries>();
        Intent here = getIntent();
        item = (Item)here.getSerializableExtra("item");
        int size = item.getMonthAmount() ;
        double prices[] = item.getPrices();
        String months[] = item.getMonths();
        Number pricearray[] = new Number[size];

        final XYPlot plot = (XYPlot) findViewById(R.id.simpleplot);
        plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        plot.setDomainStepValue(1);
        plot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);

        for(int i = 0; i<size;i++){
            pricearray[i] = prices[i];

        }
        double a = prices[0];
        curRangeMax= a+ a/4;
        curRangeMin = a- a/4;

        plot.setRangeStepValue(a /4);
        plot.setRangeBoundaries(curRangeMin, curRangeMax, BoundaryMode.FIXED);
        plot.getGraphWidget().setGridPadding(25,25,25,25);
        plot.getGraphWidget().setDomainValueFormat(new graphformat(months));
        plot.getGraphWidget().setMargins(25, 25, 100, 100);
        plot.setPlotPadding(30,30,30,30);
        String str = item.getItemName();
        XYSeries itemseries =new SimpleXYSeries(Arrays.asList(pricearray), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,str);
        final Random rand = new Random();
        int colour = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        LineAndPointFormatter seriesformat =  new LineAndPointFormatter(colour,colour,null,new PointLabelFormatter());

        seriesformat.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        seriesformat.getLinePaint().setStrokeWidth(1);
        seriesformat.getVertexPaint().setStrokeJoin(Paint.Join.ROUND);
        seriesformat.getVertexPaint().setStrokeWidth(10);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.addSeries(itemseries, seriesformat);

        allSeries.put(item,itemseries);

        plot.getLegendWidget().setTableModel(new DynamicTableModel(1,4));

        plot.getLegendWidget().setSize(new SizeMetrics(100,SizeLayoutType.ABSOLUTE,150, SizeLayoutType.ABSOLUTE));

        Paint bg = new Paint();
        bg.setColor(Color.BLACK);
        bg.setStyle(Paint.Style.FILL);
        bg.setAlpha(140);
        plot.getLegendWidget().setBackgroundPaint(bg);

        plot.getLegendWidget().setPadding(10,1,1,1);
        plot.getLegendWidget().position(20, XLayoutStyle.ABSOLUTE_FROM_RIGHT,35, YLayoutStyle.ABSOLUTE_FROM_TOP, AnchorPosition.RIGHT_TOP);


        AutoCompleteTextView additem = (AutoCompleteTextView) findViewById(R.id.AddItem);
        Wrapper wrap = (Wrapper)here.getSerializableExtra("Items");
        ArrayAdapter<Item> itemAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, wrap.getList());
        additem.setAdapter(itemAdapter);
        additem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item itemadd = (Item) parent.getItemAtPosition(position);

                int size = itemadd.getMonthAmount() ;
                double prices[] = itemadd.getPrices();
                String months[] = itemadd.getMonths();
                Number pricearray[] = new Number[size];
                for(int i = 0; i<size;i++){
                    pricearray[i] = prices[i];

                }
                XYSeries item =new SimpleXYSeries(Arrays.asList(pricearray), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,itemadd.getItemName());

                int colour = Color.rgb(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
                LineAndPointFormatter line =  new LineAndPointFormatter(colour,colour,null,new PointLabelFormatter());

                line.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
                line.getLinePaint().setStrokeWidth(1);
                line.getVertexPaint().setStrokeJoin(Paint.Join.ROUND);
                line.getVertexPaint().setStrokeWidth(10);


                double a = prices[0];
                double max = a + a/4;
                double min = a- a/4;
                double curStep = plot.getRangeStepValue();
                if(a/4 >= curStep) {
                    plot.setRangeStepValue(a / 4);
                }
                if(max >=curRangeMax){
                    curRangeMax = max;
                }
                if(min <=curRangeMin){
                    curRangeMin = min;
                }

                plot.setRangeBoundaries(curRangeMin, curRangeMax, BoundaryMode.FIXED);
                if(allSeries.containsKey(itemadd)) {
                    plot.removeSeries(allSeries.get(itemadd));

                    allSeries.remove(itemadd);
                    //added so the graph resizes when an item is deleted
                    double newmin,newmax,step;
                    newmin = 1000;
                    newmax = 0;
                    step = 0;
                    Item[] items  = allSeries.keySet().toArray(new Item[allSeries.size()]);

                    for(Item loopitem :items){

                        double price = loopitem.getPrices()[0];
                        min = price -price/4;
                        max = price+price/4;
                        double curstep = price/4;
                        if(min<newmin){
                            newmin = min;
                        }
                        if(max > newmax){
                            newmax = max;
                        }
                        if(curstep > step){
                            plot.setRangeStepValue(curstep);
                        }
                    }
                    curRangeMax = newmax;
                    curRangeMin = newmin;
                    plot.setRangeBoundaries(curRangeMin, curRangeMax, BoundaryMode.FIXED);

                }else{
                    plot.addSeries(item, line);
                    allSeries.put(itemadd,item);
                }


                plot.redraw();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.history_about) {
            new AlertDialog.Builder(this).setMessage(R.string.menu_about_msg).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }else if (id==R.id.history_help){
            new AlertDialog.Builder(this).setMessage(R.string.history_menu_help).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //return to activity
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
