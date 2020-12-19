package com.example.biker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyListFindServiceAdater extends RecyclerView.Adapter<MyListFindServiceAdater.ViewHolder> {
    private ArrayList<MyListFindServiceData> listdata;

    // RecyclerView recyclerView;
    public MyListFindServiceAdater(ArrayList<MyListFindServiceData> listdata) {
        Log.e("kk", listdata.toString());
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("kk","create");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_findservice, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("kk", "uu");
        final MyListFindServiceData myListData = listdata.get(position);
        holder.itemName.setText(listdata.get(position).getName());
        holder.itemAddress.setText(listdata.get(position).getAddress());
        holder.itemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemAddress;
        public Button itemBook;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.itemname);
            this.itemAddress = itemView.findViewById(R.id.itemaddress);
            this.itemBook = itemView.findViewById(R.id.itembook);
        }
    }
}