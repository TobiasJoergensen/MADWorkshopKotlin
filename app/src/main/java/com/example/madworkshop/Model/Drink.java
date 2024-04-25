package com.example.madworkshop.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

//This is a model. This is how we will build out drinks.
@Entity(tableName = "Drink")
public class Drink {

    //This is the type of variables we have on our drink. We could also add other things here such as alcohol percentage or an image resource.
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int id;
    public String Name;
    public Integer Price;
    @ColumnInfo(name = "drinkType")
    public String Type;

    //This is the constructor. When we want to make a new drink, we can pass on the name and price.
    //Hint: See method 'createModel' in our Menu activity.
    public Drink(String Name, Integer Price, String Type) {
        this.Name = Name;
        this.Price = Price;
        this.Type = Type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        this.Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

}
