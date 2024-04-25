package com.example.madworkshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madworkshop.Model.Drink
import java.lang.String
import kotlin.Int

class MenuAdapter(
    //Here we have the same name for drinks in our global scope and our local scope. The 'this' keyword will allow us to assign to the global scope. //This is the constructor. This is used, when we want to create a new adapter.
    //Hint: See the menu activity, to see how we use this constructor.
    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private var drinks: List<Drink>,
    private val listener: (Any) -> Unit) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
        //This is method from the RecyclerView class we are extending.
        //This is called, when we want to create a new item in our RecyclerView.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //This is the view, we will use as a template for each item in our RecyclerView
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.drink_view_row, parent, false)
            return ViewHolder(view)
        }

        //This is method from the RecyclerView class we are extending.
        //This is called after we have created a new item and are going to assign to it.
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //The position is provided by the RecyclerView class, and it allows us to know what position we are currently at.
            //I.e. the first time it's position 0, then 1 and so on.
            val drink = drinks[position]

            //The holder have some variables we can assign to.
            //See the class ViewHolder in this activity to understand it.
            holder.name.setText(drink.name)
            holder.price.setText(String.valueOf(drink.price))

            //We might want to click on our holder, so we add a listener on them.

            holder.itemView.setOnClickListener {
            }
        }

        //This is method from the RecyclerView class we are extending.
        //This is called once to tell Android how many items it should create..
        override fun getItemCount(): Int {
            return drinks.size
        }

        //We are creating an interface to use when adding a clickListener
        interface OnItemClickListener {
            fun onItemClick(item: Drink?)
        }

        fun setMyDrinks(drinks: List<Drink>) {
            this.drinks = drinks
            notifyDataSetChanged()
        }

        //This class is our logical template for how the items will look.
        //Think of it as when we make a new activity we also have code that can set the text and so on.
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView
            val price: TextView

            init {
                name = itemView.findViewById<View>(R.id.drinkName) as TextView
                price = itemView.findViewById<View>(R.id.drinkPrice) as TextView
            }
        }
}


