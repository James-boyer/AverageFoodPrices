package com.example.jum.averagefoodprices;

import java.io.Serializable;
import java.util.List;

/**
 * Created by james
 */
public class Wrapper implements Serializable{
    public List<Item> list;

    public Wrapper(List<Item> a){
        list = a;
    }
    public List<Item> getList(){
        return list;
    }

}
