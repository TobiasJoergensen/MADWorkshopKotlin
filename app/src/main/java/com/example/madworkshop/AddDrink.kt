package com.example.madworkshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddDrink : AppCompatActivity() {
    //This is global variables of this class. You can access these anywhere in this class, but only here because they are private.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private var etName: EditText? = null
    private var etAge: EditText? = null
    private var etType: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //We will set the UI view here.
        setContentView(R.layout.activity_add_drink)

        //Assign reference from out local variables to the UI elements
        etName = findViewById<View>(R.id.et_name) as EditText
        etAge = findViewById<View>(R.id.et_age) as EditText
        etType = findViewById<View>(R.id.et_type) as EditText
        val button = findViewById<Button>(R.id.add_btn)

        //Add an onClick listener for when we press the save button.
        button.setOnClickListener { view: View? ->
            val replyIntent = Intent()

            //If any of the textFields are empty, we will cancel the intent.
            if (!(!TextUtils.isEmpty(etName!!.text)
                            && !TextUtils.isEmpty(etAge!!.text)
                            && !TextUtils.isEmpty(etType!!.text))) {
                //We set the result as not OK
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                //Here we put all of our text into information that we can use, when we return to where the intent was called.
                val name = etName!!.text.toString()
                replyIntent.putExtra("name", name)
                val age = Integer.getInteger(etName!!.text.toString())
                replyIntent.putExtra("age", age)
                val type = etName!!.text.toString()
                replyIntent.putExtra("type", type)

                //We set the result as OK
                setResult(RESULT_OK, replyIntent)
            }
            //We finish the intent. This means we return to the previous page we were at.
            finish()
        }
    }
}
