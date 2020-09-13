package com.example.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<ItemData> list;
    private Context mcontext;
    private OnIemClickListener mListener;
    private OnIemLongClickListener onIemLongClickListener;

    public interface OnIemClickListener{
        void onItemClick( int position);
    }
    public interface OnIemLongClickListener{
        void onItemLongClick( int position);
    }
    public void setOnItemClickListener(OnIemClickListener listener){
        mListener=listener;
    }

    public void setOnItemLongClickListener(OnIemLongClickListener listener){
        onIemLongClickListener=listener;
    }

    public ItemAdapter(ArrayList<ItemData> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemData currentItem = list.get(position);
        holder.itemName.setText(currentItem.getName());
        holder.imageView.setImageResource(currentItem.getImgRes());
        if(currentItem.isDirectory()){
        File f=new File(currentItem.getPath());
        holder.NoOfItems.setText(f.listFiles().length+"items");}
        else {
            holder.NoOfItems.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        TextView NoOfItems;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            imageView=itemView.findViewById(R.id.image_folder);
            NoOfItems=itemView.findViewById(R.id.tv_items);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onIemLongClickListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            onIemLongClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });

        }
    }
}
