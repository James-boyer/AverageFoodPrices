package com.example.jum.averagefoodprices;
import java.io.Serializable;
/**
 * Created by James boyer
 */
public class Item implements Serializable{
    public String itemName;
    public int monthcount;
    public String month[];
    public double prices[];
    public String measurement;
    public double weight;
    public String type;

    public Item(){
        itemName = "";
        monthcount = 0;
        month = null;
        weight = 0;
        measurement = "";
        prices = null;
        type = "";
    }
    public Item(String n, String m[], double p[], int ms, String w){

        setItemName(n);
        setMonths(m,p,ms);
        setWeight(w);

    }

    public void setMonths(String m[],double p[],int ms){
        month = new String[ms];
        prices = new double[ms];
        for(int i = 0;i<ms;i++){
            month[i] = m[i];
            prices[i] =p[i];
        }
        monthcount = ms;
    }
    public void setItemName(String name){
        itemName = name;
    }
    public void setWeight(String w){
        //weight = w;
        weight = 0.0;
        measurement = "";

        if(w.contains(" ")) {
            String mass[] = w.split(" ");
            weight = Double.parseDouble(mass[0]);
            measurement = mass[1];
        }else{

            weight = Double.parseDouble(w);
            measurement = itemName;
        }
    }
    public String getWeightString(){
        return weight +" "+ measurement;
    }
    public double getWeightNum(){
        return weight;
    }
    public String getMeasurement(){
        return measurement;
    }
    public String getItemName(){
        return itemName;
    }
    public String getRecentMonth(){
        return month[monthcount - 1] +": $"+prices[monthcount-1];
    }
    public double getRecentPriceMonth(){
        return prices[monthcount-1];
    }
    public String[] getMonths(){
        return month;
    }
    public double[] getPrices(){
        return prices;
    }
    public int getMonthAmount(){
        return monthcount;
    }
    @Override
    public String toString(){

        return itemName;
    }
    @Override
    public boolean equals(Object item){
        try{
            Item newitem = (Item) item;
            return itemName.equals(newitem.getItemName());
        }catch(Exception e){
            return false;
        }

    }

}
