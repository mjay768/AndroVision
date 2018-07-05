package com.androvision.app.androvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    String imagefilepath = null;
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button textread = (Button) findViewById(R.id.textrecog);
        Button takepicture = (Button) findViewById(R.id.takepicture);

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(MainActivity.this,TextRecognize.class);
                //startActivity(i);
                openCameraIntent();
                if(imagefilepath != null) {
                    displayPicturePath(imagefilepath);
                }
            }
        });

        textread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent text = new Intent(MainActivity.this, TextRecognize.class);
                startActivity(text);
            }
        });


    }

    public void openCameraIntent(){
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageIntent.resolveActivity(getPackageManager())!=null){
            File picturefile = null;
            try{
                picturefile = createPictureFile();

            }catch(IOException e){
                Toast.makeText(this, "Error creating image", Toast.LENGTH_SHORT).show();
            }

            if(picturefile != null){
                Uri pictureUri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() +
                                                                                    ".provider",picturefile);
            }
            startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        if(requestcode == REQUEST_IMAGE_CAPTURE && resultcode == RESULT_OK){
            if(data != null && data.getExtras() != null){
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            }
        }
        if(resultcode == RESULT_CANCELED){
            Toast.makeText(this, "User Cancelled", Toast.LENGTH_SHORT).show();
            imagefilepath=null;
            displayPicturePath(imagefilepath);
        }
    }

    private File createPictureFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd+HHmmss",
                                    Locale.getDefault()).format(new Date());

        String picturefilename = "IMG_" + timestamp + "_";
        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File picture = File.createTempFile(picturefilename,".JPG",storagedir);
        imagefilepath = picture.getAbsolutePath();
        return picture;
    }

    public void displayPicturePath(String path){
        TextView picturepath = (TextView) findViewById(R.id.pictureUri);
        picturepath.setText(imagefilepath);
    }

}



