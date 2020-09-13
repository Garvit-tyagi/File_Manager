package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
private RecyclerView recview_main;
ArrayList<ItemData> mainData;
private TextView nofiles;

String filePath;
String fileName;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
        }
        nofiles=findViewById(R.id.text_nofiles);

        path= Environment.getExternalStorageDirectory().getAbsolutePath();
        if(getIntent().hasExtra("path")){
            path=getIntent().getStringExtra("path");
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
            File f=new File(path);
            if(f.listFiles().length==0) {
                nofiles.setVisibility(View.VISIBLE);
            }
        }
//            if(getIntent().hasExtra("from")){
//                if(getIntent().getStringExtra("from").equals("moveButton")){
//                    actionButton.setText("MOVE HERE");
//                    actionButton.setVisibility(View.VISIBLE);
//                    fileName=getIntent().getStringExtra("filename");
//                    filePath=getIntent().getStringExtra("filepath");
//                }
//            }
         recview_main=findViewById(R.id.recview_main);
         mainData=new ArrayList<>();

        File root = new File(path);
        Log.i(TAG,path);
        final File[] files=root.listFiles();

        if(files!=null){
        Log.i(TAG,""+files.length+"");

        for(int i=0;i<files.length;i++){
            mainData.add(new ItemData(files[i].getName(),files[i].getAbsolutePath(),(files[i].isDirectory())? R.drawable.ic_folder : R.drawable.ic_file,files[i].isDirectory()));
        }
      recview_main.setLayoutManager(new LinearLayoutManager(this));
        final ItemAdapter adapter=new ItemAdapter(mainData,this);
        recview_main.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemAdapter.OnIemClickListener() {
            @Override
            public void onItemClick(int position) {

                ItemData currentItem =mainData.get(position);
                if(currentItem.isDirectory()){
//                    Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(MainActivity.this,MainActivity.class);
                    i.putExtra("path",currentItem.getPath());
                    i.putExtra("name",currentItem.getName());
                    startActivity(i);
                }else
                {
                    File file=new File(currentItem.getPath());
                    Uri uri= FileProvider.getUriForFile(MainActivity.this,getPackageName()+".provider",file);
                    String  mime=get_mime_type(uri.toString());
                    // Open file with user selected app
                    try{
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, mime);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    }
                    catch (Exception e){
                        Toast t = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,0);
                        t.show();
                    }
//                    Toast.makeText(getApplicationContext(),"not directory",Toast.LENGTH_SHORT).show();
                }

            }
        });
        adapter.setOnItemLongClickListener(new ItemAdapter.OnIemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                ItemData currentitem=mainData.get(position);

                bottom_sheet bs=new bottom_sheet(currentitem.getPath(),currentitem.getName(),position,mainData);
                bs.show(getSupportFragmentManager(),"bottomSheet");




            }
        });

        }else
        {

            nofiles.setVisibility(View.VISIBLE);
        }
//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionButton.setVisibility(View.INVISIBLE);
//                moveFile(filePath,fileName,path);
//            }
//        });
    }



    public Boolean checkPermission(String perm)
    {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                nofiles.setVisibility(View.INVISIBLE);
                recreate();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Add permission",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_newFolder:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Title");

                // Set up the input
                final EditText m_edtinput = new EditText(this);
                // Specify the type of input expected;
                m_edtinput.setInputType(InputType.TYPE_CLASS_TEXT);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s=m_edtinput.getText().toString();
                        File folder = new File(path +
                                File.separator + s);
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdirs();
                        }
                        if (success) {
                            // Do something on success
                            Toast.makeText(getApplicationContext(),"created",Toast.LENGTH_SHORT).show();
                        } else {
                            // Do something else on failure
                            Toast.makeText(getApplicationContext(),"cannot create",Toast.LENGTH_SHORT).show();
                        }
                        recreate();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setView(m_edtinput);
                builder.show();



        }
        return super.onOptionsItemSelected(item);
    }
    //    private void moveFile(String inputPath, String inputFile, String outputPath) {
//
//        InputStream in ;
//        OutputStream out ;
//        try {
//
//            //create output directory if it doesn't exist
//            File dir = new File (outputPath);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }
//
//
//            in = new FileInputStream(inputPath);
//            out = new FileOutputStream(outputPath +"/"+ inputFile);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//
//
//            // write the output file
//            out.flush();
//            out.close();
//
//
//            // delete the original file
//            new File(inputPath + "/"+inputFile).delete();
//
//
//        }
//
//        catch (FileNotFoundException fnfe1) {
//            Log.e("tag", fnfe1.getMessage());
//        }
//        catch (Exception e) {
//            Log.e("tag", e.getMessage());
//        }
//
//    }


}
