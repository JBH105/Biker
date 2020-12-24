package com.example.biker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.biker.user.user_book_service;

import java.util.ArrayList;
import java.util.List;

import static com.example.biker.bike_service_location.setProgressBarVisibility;

public class MyListFindServiceAdater extends RecyclerView.Adapter<MyListFindServiceAdater.ViewHolder> {
    private List<MyListFindServiceData> listdata;

    // RecyclerView recyclerView;
    public MyListFindServiceAdater(List<MyListFindServiceData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_findservice, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListFindServiceData myListData = listdata.get(position);
        holder.itemName.setText(myListData.getUser_username()+"\n"+myListData.getMobile()+"\n"+myListData.getUser_email());
        holder.itemAddress.setText("  "+myListData.getAddress_fl()+" "+myListData.getAddress_sl()+"\n"+myListData.getCity()+"-"+myListData.getZip());
        holder.itemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getUser_id(), Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setCancelable(true)
                        .setTitle("Confirm")
                        .setMessage("Book Service of "+myListData.getUser_username()+" ??")
                        .setPositiveButton("Confirm Book", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setProgressBarVisibility(View.VISIBLE);
                                // pass selected servicer data to class which will send request to book it
                                new user_book_service().Book_Service(view.getContext(), myListData);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void add(MyListFindServiceData addData) {
        listdata.add(addData);
        notifyDataSetChanged();
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