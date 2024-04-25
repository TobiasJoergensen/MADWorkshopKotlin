package com.example.madworkshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.madworkshop.Model.Drink;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private List<Drink> drinks;
    private OnItemClickListener listener;

    //This is the constructor. This is used, when we want to create a new adapter.
    //Hint: See the menu activity, to see how we use this constructor.
    public MenuAdapter(List<Drink> drinks, OnItemClickListener listener)
    {
        //Here we have the same name for drinks in our global scope and our local scope. The 'this' keyword will allow us to assign to the global scope.
        this.drinks = drinks;
        this.listener = listener;
    }

    @Override
    //This is method from the RecyclerView class we are extending.
    //This is called, when we want to create a new item in our RecyclerView.
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //This is the view, we will use as a template for each item in our RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //This is method from the RecyclerView class we are extending.
    //This is called after we have created a new item and are going to assign to it.
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        //The position is provided by the RecyclerView class, and it allows us to know what position we are currently at.
        //I.e. the first time it's position 0, then 1 and so on.
        Drink drink = drinks.get(position);

        //The holder have some variables we can assign to.
        //See the class ViewHolder in this activity to understand it.
        holder.name.setText(drink.Name);
        holder.price.setText(String.valueOf(drink.Price));

        //We might want to click on our holder, so we add a listener on them.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(drink);
            }
        });
    }

    @Override
    //This is method from the RecyclerView class we are extending.
    //This is called once to tell Android how many items it should create..
    public int getItemCount() {
        return drinks.size();
    }

    //We are creating an interface to use when adding a clickListener
    public interface OnItemClickListener {
        void onItemClick(Drink item);
    }

    public void setMyDrinks(List<Drink> drinks){
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    //This class is our logical template for how the items will look.
    //Think of it as when we make a new activity we also have code that can set the text and so on.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView price;

        public ViewHolder(final View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.drinkName);
            price = (TextView) itemView.findViewById(R.id.drinkPrice);
        }
    }

}


