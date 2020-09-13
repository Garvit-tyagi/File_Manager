package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class popupActivity extends AppCompatActivity {
    private static final String TAG = "popupActivity";
    private RecyclerView recview_popup;
    ArrayList<ItemData> mainData;

    private Button actionButton;

    String filePath;
    String fileName;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Log.i(TAG,"popup");

        actionButton=findViewById(R.id.btn_action);
        path= Environment.getExternalStorageDirectory().getAbsolutePath();
        if(getIntent().hasExtra("path")) {
            Log.i(TAG,getIntent().getStringExtra("path"));
            path = getIntent().getStringExtra("path");
            fileName=getIntent().getStringExtra("filename");
            filePath=getIntent().getStringExtra("filepath");
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
            actionButton.setText(getIntent().getStringExtra("button"));
        }

            if(getIntent().hasExtra("from")){
                if(getIntent().getStringExtra("from").equals("moveButton")){
                    actionButton.setText("MOVE HERE");

                    fileName=getIntent().getStringExtra("filename");
                    filePath=getIntent().getStringExtra("filepath");
                }else{
                    actionButton.setText("COPY HERE");

                    fileName=getIntent().getStringExtra("filename");
                    filePath=getIntent().getStringExtra("filepath");
                }
            }
        Log.i(TAG,fileName);
        Log.i(TAG,filePath);


        recview_popup=findViewById(R.id.recview_popup);
        mainData=new ArrayList<>();

        File root = new File(path);
        Log.i(TAG,path);
        final File[] files=root.listFiles();

        if(files!=null){
            Log.i(TAG,""+files.length+"");

            for(int i=0;i<files.length;i++){
                mainData.add(new ItemData(files[i].getName(),files[i].getAbsolutePath(),(files[i].isDirectory())? R.drawable.ic_folder : R.drawable.ic_file,files[i].isDirectory()));
            }
            recview_popup.setLayoutManager(new LinearLayoutManager(this));
            final ItemAdapter adapter=new ItemAdapter(mainData,this);
            recview_popup.setAdapter(adapter);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"buttonClicked");
                    if(actionButton.getText().equals("MOVE HERE")){
                        moveFile(filePath,fileName,path);}
                    else{
                        Log.i(TAG,"copying");
                        copyFile(filePath,fileName,path);
                    }

                }
            });

            adapter.setOnItemClickListener(new ItemAdapter.OnIemClickListener() {
                @Override
                public void onItemClick(int position) {

                    ItemData currentItem =mainData.get(position);
                    if(currentItem.isDirectory()){
//                    Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(popupActivity.this,popupActivity.class);
                        i.putExtra("path",currentItem.getPath());
                        i.putExtra("name",currentItem.getName());
                        i.putExtra("filename",fileName);
                        i.putExtra("filepath",filePath);
                        i.putExtra("button",actionButton.getText());
                        startActivity(i);
                    }else
                    {

                    }

                }
            });


        }else
        {


        }

    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {
        Log.i(TAG,inputPath);
        Log.i(TAG,inputFile);
        Log.i(TAG,outputPath);
        InputStream in ;
        OutputStream out ;
        try {

            //create output directory if it doesn't exist
//            File dir = new File (outputPath);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath +"/"+ inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();


            // write the output file
            out.flush();
            out.close();


        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        Toast.makeText(getApplicationContext(),"Copied Successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(popupActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        popupActivity.this.finish();


    }


    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in ;
        OutputStream out ;
        try {

            //create output directory if it doesn't exist
//            File dir = new File (outputPath);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath +"/"+ inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();


            // write the output file
            out.flush();
            out.close();


            // delete the original file
            new File(inputPath).delete();


        }


        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        Toast.makeText(getApplicationContext(),"Moved Successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(popupActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        popupActivity.this.finish();

    }



}
