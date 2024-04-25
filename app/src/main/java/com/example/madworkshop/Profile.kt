package com.example.madworkshop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Profile : AppCompatActivity() {
    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private var name: EditText? = null
    private var email: EditText? = null
    private var imageView: ImageView? = null
    private var bestFriend: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //To find the context of the app, we can call 'getApplicationContext'
        context = applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
        }
        FindViews()
        SetViews()
    }

    private fun FindViews() {
        //We are instantiating our views, so we can assign and read from them.
        name = findViewById<View>(R.id.et_name) as EditText
        email = findViewById<View>(R.id.et_email) as EditText
        imageView = findViewById<View>(R.id.iv_profile) as ImageView
        bestFriend = findViewById<View>(R.id.tv_best_friend) as TextView
    }

    private fun SetViews() {
        //Here we get values from a shared preference file called 'Profile' and assign them into our EditText views.
        val myPrefsFile = getSharedPreferences("Profile", MODE_PRIVATE)

        //name.setText("Your name");
        //email.setText("Your mail");
        val profileName = myPrefsFile.getString("Name", null)
        val profileMail = myPrefsFile.getString("Email", null)
        if (profileName != null && !profileName.isEmpty()) {
            name!!.setText(profileName)
        }
        if (profileMail != null && !profileMail.isEmpty()) {
            email!!.setText(profileMail)
        }
        bestFriend!!.text = myPrefsFile.getString("BestFriend", resources.getString(R.string.bestFriend))
        LoadImage()
        bestFriend!!.setOnClickListener { v: View? -> pickContact(bestFriend) }
    }

    fun SaveProfile(view: View?) {
        //We open the 'Profile' file with our preferences.
        val myPrefsFile = getSharedPreferences("Profile", MODE_PRIVATE)
        //We instantiate an editor based in our 'Profile' file.
        val editor = myPrefsFile.edit()
        //Here we get the values we need to save.
        editor.putString("Name", name!!.text.toString())
        editor.putString("Email", email!!.text.toString())
        editor.putString("BestFriend", bestFriend!!.text.toString())
        //Now we can commit the files to our file, and they are saved.
        editor.commit()
        //We show the user a message that the profile have been saved and then close the activity to return to our previous activity.
        Toast.makeText(context, resources.getText(R.string.profileSavedToast), Toast.LENGTH_LONG).show()
        finish()
    }

    //This is a method assigned from out XML file for the onClick method to our take picture button.
    fun takePicture(view: View?) {
        //We start the activity here, and when it finishes it will cal the onActivityResult.
        val myCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(myCameraIntent, CAMERA_REQUEST)
    }

    public override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)
        when (reqCode) {
            CAMERA_REQUEST -> {
                run {
                    if (resCode == RESULT_OK) {
                        //BitMap is a datatype used for images. We get the data returned by our previous intent.
                        val NewFace = data!!.extras!!["data"] as Bitmap?
                        //Here we will set the image in our ImageView
                        imageView!!.setImageBitmap(NewFace)

                        //Since we already assigned the context in our onCreate, we will use it to get a dir called imageDir related to our application.
                        val directory = context!!.getDir("imageDir", MODE_PRIVATE)
                        //In the folder we will create a new file called profile.jpg
                        val mypath = File(directory, "profile.jpg")
                        var outputstream: FileOutputStream? = null
                        //In case we fail saving the image, we don't want to crash. That's why we are using a try clause. Here, if we fail, we will go into our catch
                        try {
                            //Here we start saving the file. It's important to remember to close the outputstream, as it will stay open until we close it.
                            outputstream = FileOutputStream(mypath)
                            NewFace!!.compress(Bitmap.CompressFormat.JPEG, 90, outputstream)
                            outputstream.close()
                        } //In the catch clause the app will show us what went wrong. If you code ends up in a catch clause, you are trying to do something that is not working. Put a debug step here and check the stack trace.
                        catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                if (resCode == RESULT_OK) {
                    //This is the path to our contacts as returned by our selectionResult
                    val contactUri = data!!.data
                    //We create a cursor and
                    val cursor = contentResolver.query(contactUri!!, null, null, null, null)
                    //We move the cursor to the first row that satisfies that we want our contact information.
                    cursor!!.moveToFirst()
                    //Get the index of where we have the display name of the contact.
                    val column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    //assign the name to a String.
                    val name = cursor.getString(column)
                    //Add the name to our textview.
                    bestFriend!!.text = resources.getText(R.string.bestFriend).toString() + name
                }
            }

            PICK_CONTACT -> if (resCode == RESULT_OK) {
                val contactUri = data!!.data
                val cursor = contentResolver.query(contactUri!!, null, null, null, null)
                cursor!!.moveToFirst()
                val column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val name = cursor.getString(column)
                bestFriend!!.text = resources.getText(R.string.bestFriend).toString() + name
            }

            else -> {}
        }
    }

    private fun LoadImage() {
        //As before, here we open the folder where our image is saved.
        val directory = context!!.getDir("imageDir", MODE_PRIVATE)
        val mypath = File(directory, "profile.jpg")
        try {
            //We start loading the image and will set it on our ImageView
            val inputStream = FileInputStream(mypath)
            val b = BitmapFactory.decodeStream(inputStream)
            imageView!!.setImageBitmap(b)
            inputStream.close()
        } //You might see that the exception name here is different. There's a lot of different exceptions to catch, i.e. OutOfBound, IOException and so on.
        //This specific clause will be invoked the first time you run the app, since we have not saved anything yet.
        catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Intent to select a contact. Works as the other intent to take an image.
    fun pickContact(view: View?) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT)
    }

    //Here we request permission for reading contacts. If it's not given, we will hide the view.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Here you could add some logic if the permission was granted.
                } else {
                    //We are just going to hide the selection view if the permission is not given.
                    bestFriend!!.visibility = View.GONE
                }
                return
            }
        }
    }

    companion object {
        private var context: Context? = null
        private const val CAMERA_REQUEST = 1888
        private const val PICK_CONTACT = 0
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }
}