package com.example.filemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class bottom_sheet extends BottomSheetDialogFragment {
    private static final String TAG = "bottom_sheet";
    private String filePath;
    private String fileName;
    int currentItem;
    ArrayList<ItemData> refresh;

    public bottom_sheet(String filePath, String fileName, int currentItem, ArrayList<ItemData> refresh) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.currentItem = currentItem;
        this.refresh = refresh;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        LinearLayout btn = view.findViewById(R.id.delete);
        LinearLayout btn2 = view.findViewById(R.id.copy);
        LinearLayout btn3 = view.findViewById(R.id.move);
        LinearLayout btn4 = view.findViewById(R.id.rename);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    // delete the original file

                    new File(filePath).delete();
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                refresh.remove(currentItem);

                getActivity().recreate();
                dismiss();

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), popupActivity.class);
                i.putExtra("from", "copyButton");
                i.putExtra("filepath", filePath);
                i.putExtra("filename", fileName);
                startActivity(i);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), popupActivity.class);
                i.putExtra("from", "moveButton");
                i.putExtra("filepath", filePath);
                i.putExtra("filename", fileName);
                startActivity(i);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");

                // Set up the input
                final EditText m_edtinput = new EditText(getContext());
                // Specify the type of input expected;
                m_edtinput.setInputType(InputType.TYPE_CLASS_TEXT);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                  Log.i(TAG,filePath);
                  File f=new File(filePath);
                  String s=f.getParent();
                  Log.i(TAG,s);
                        Log.i(TAG,filePath+File.separator+m_edtinput.getText().toString());
                        File sdcard = new File(s);
                        File from = new File(sdcard,fileName);
                        File to = new File(sdcard,m_edtinput.getText().toString());
                        from.renameTo(to);
                        boolean renamed = from.renameTo(to);

                        if (renamed) {
                            Log.i(TAG,"File renamed...");
                        }else {
                            Log.i(TAG,"File not renamed...");
                        }
                Toast.makeText(getContext(),"name changed",Toast.LENGTH_SHORT).show();

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
        });

        return view;
    }


}
