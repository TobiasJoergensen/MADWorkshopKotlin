package com.example.madworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private Button btTitle;
    public int someNumber = 1;

    //We use the override keyword when we want to expand on a method already defined. In this case, AppCompatActivity contains a method called 'onCreate' but we want to expand on it, so we use the 'Override' keyword
    @Override
    //onCreate is part of the Android lifecycle. This method is called when the acitvity is created. It will never be called again, until this activity have to be rebuild.
    //If you need to reload some data, when the app is returning from the background, use the onStart for instance. If you move away from this view, and later come back, it would be the onResume.
    //Hint: Google 'Android lifecycle' to understand how it works when what is called.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This method sets the view of the app. If you create a new view and call it 'my_cafe_view', you could rename this layout to that name.
        setContentView(R.layout.activity_main);

        //Since we are overriding the onCreate method, we can add our owned methods. Here I added something called 'assignViews' where I reference my button.
        // Then I dont have to reference everytime I need to change it.
        AssignViews();
    }

    //A method to reference a view to one of my local variables of this class
    private void AssignViews() {
        btTitle = (Button) findViewById(R.id.btTitle);
    }

    //This is a method that you can use in your design file.
    //This is how you would write a function to be used in the XML.
    //Hint: Find a button view in your xml file and look at the methods you can assign in the onClick attribute
    public void FromXML(View v) {
        //Here I send some text with my method. This text is hardcoded, but in an ideal world you should always use the strings.xml to handle your text.
        //Hint: See the 'FromAnonFunction' to see how you reference a string from the strings.xml file
        AddToText("Button was clicked: " + someNumber);
        someNumber = someNumber + 1;
    }

    //This method assigns an anonymous function to a button.
    //This is how you would add a click listener from the code.
    //Hint: Try to assign the buttons onClick from the design, as we always want to minimize the amount of code we write.
    public void FromAnonFunction() {
        btTitle.setOnClickListener(v -> {
            //You can see we can't use the R.string.counterButtonClicked directly and it would tell us it's an int.
            //This is because it's a reference. The actualy text is stored in the memory, and the resource just know the address.
            //getRessources and getString will dig into the memory and get us the string. Very cool!
            AddToText(getResources().getString(R.string.counterButtonClicked) + someNumber);
            someNumber = someNumber + 1;
        });
    }

    //A simple method to assign new text to our button. It takes the new text as a input.
    //Hint: Always break logic down into methods and functions. It helps greatly with readability.
    private void AddToText(String newText) {
        btTitle.setText(newText);
    }

    public void GoToMenu(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void GoToAboutUs(View v) {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    public void GoToProfile(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}