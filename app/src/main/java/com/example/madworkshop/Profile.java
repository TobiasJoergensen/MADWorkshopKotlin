package com.example.madworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {

    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private EditText name;
    private EditText email;
    private ImageView imageView;
    private TextView bestFriend;
    private static Context context;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_CONTACT = 0;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //To find the context of the app, we can call 'getApplicationContext'
        this.context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        FindViews();
        SetViews();
    }

    private void FindViews() {
        //We are instantiating our views, so we can assign and read from them.
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        imageView = (ImageView) findViewById(R.id.iv_profile);
        bestFriend = (TextView) findViewById(R.id.tv_best_friend);
    }

    private void SetViews() {
        //Here we get values from a shared preference file called 'Profile' and assign them into our EditText views.
        SharedPreferences myPrefsFile = getSharedPreferences("Profile", Activity.MODE_PRIVATE);

        //name.setText("Your name");
        //email.setText("Your mail");

        String profileName = myPrefsFile.getString("Name", null);
        String profileMail = myPrefsFile.getString("Email", null);
        if(profileName != null && !profileName.isEmpty()) {
            name.setText(profileName);
        }
        if(profileMail != null && !profileMail.isEmpty()) {
            email.setText(profileMail);
        }

        bestFriend.setText(myPrefsFile.getString("BestFriend", getResources().getString(R.string.bestFriend)));
        LoadImage();
        bestFriend.setOnClickListener(v -> {
            pickContact(bestFriend);
        });
    }

    public void SaveProfile(View view) {
        //We open the 'Profile' file with our preferences.
        SharedPreferences myPrefsFile = getSharedPreferences("Profile", Activity.MODE_PRIVATE);
        //We instantiate an editor based in our 'Profile' file.
        SharedPreferences.Editor editor = myPrefsFile.edit();
        //Here we get the values we need to save.
        editor.putString("Name", name.getText().toString());
        editor.putString("Email", email.getText().toString());
        editor.putString("BestFriend", bestFriend.getText().toString());
        //Now we can commit the files to our file, and they are saved.
        editor.commit();
        //We show the user a message that the profile have been saved and then close the activity to return to our previous activity.
        Toast.makeText(context, getResources().getText(R.string.profileSavedToast), Toast.LENGTH_LONG).show();
        finish();
    }

    //This is a method assigned from out XML file for the onClick method to our take picture button.
    public void takePicture(View view) {
        //We start the activity here, and when it finishes it will cal the onActivityResult.
        Intent myCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(myCameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);

        //Each intent have a unique reqCode as an integer with a reference of a name such as 'CAMERA_REQUEST'.
        switch (reqCode){
            case CAMERA_REQUEST : {
                if(resCode == Activity.RESULT_OK){
                    //BitMap is a datatype used for images. We get the data returned by our previous intent.
                    Bitmap NewFace = (Bitmap) data.getExtras().get("data");
                    //Here we will set the image in our ImageView
                    imageView.setImageBitmap(NewFace);

                    //Since we already assigned the context in our onCreate, we will use it to get a dir called imageDir related to our application.
                    File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
                    //In the folder we will create a new file called profile.jpg
                    File mypath = new File(directory,"profile.jpg");
                    FileOutputStream outputstream = null;
                    //In case we fail saving the image, we don't want to crash. That's why we are using a try clause. Here, if we fail, we will go into our catch
                    try{
                        //Here we start saving the file. It's important to remember to close the outputstream, as it will stay open until we close it.
                        outputstream = new FileOutputStream(mypath);
                        NewFace.compress(Bitmap.CompressFormat.JPEG, 90, outputstream);
                        outputstream.close();
                    }
                    //In the catch clause the app will show us what went wrong. If you code ends up in a catch clause, you are trying to do something that is not working. Put a debug step here and check the stack trace.
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            case PICK_CONTACT :
                if (resCode == Activity.RESULT_OK) {
                    //This is the path to our contacts as returned by our selectionResult
                    Uri contactUri = data.getData();
                    //We create a cursor and
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                    //We move the cursor to the first row that satisfies that we want our contact information.
                    cursor.moveToFirst();
                    //Get the index of where we have the display name of the contact.
                    int column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    //assign the name to a String.
                    String name = cursor.getString(column);
                    //Add the name to our textview.
                    bestFriend.setText(getResources().getText(R.string.bestFriend) + name);
                }
            default:
                break;
        }
    }

    private void LoadImage() {
        //As before, here we open the folder where our image is saved.
        File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");
        try {
            //We start loading the image and will set it on our ImageView
            FileInputStream inputStream = new FileInputStream(mypath);
            Bitmap b = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(b);
            inputStream.close();
        }
        //You might see that the exception name here is different. There's a lot of different exceptions to catch, i.e. OutOfBound, IOException and so on.
        //This specific clause will be invoked the first time you run the app, since we have not saved anything yet.
        catch (FileNotFoundException e) {
            e.printStackTrace(); }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Intent to select a contact. Works as the other intent to take an image.
    public void pickContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    //Here we request permission for reading contacts. If it's not given, we will hide the view.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Here you could add some logic if the permission was granted.
                }
                else {
                    //We are just going to hide the selection view if the permission is not given.
                    bestFriend.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

}