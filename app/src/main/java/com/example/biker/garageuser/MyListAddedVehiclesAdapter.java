package com.example.biker.garageuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.biker.MyListFindServiceAdater;
import com.example.biker.MyListFindServiceData;
import com.example.biker.R;
import com.example.biker.user.user_book_service;

import java.util.List;

import static com.example.biker.add_vehicles.deletevehicleAddedMethod;

public class MyListAddedVehiclesAdapter extends RecyclerView.Adapter<MyListAddedVehiclesAdapter.ViewHolder> {

    private List<MyListAddedVehiclesData> listdata;

    // RecyclerView recyclerView;
    public MyListAddedVehiclesAdapter(List<MyListAddedVehiclesData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public MyListAddedVehiclesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_vehiclesadded, parent, false);
        MyListAddedVehiclesAdapter.ViewHolder viewHolder = new MyListAddedVehiclesAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyListAddedVehiclesAdapter.ViewHolder holder, int position) {
        final MyListAddedVehiclesData myListData = listdata.get(position);
        holder.srNoTextView.setText(String.valueOf(position+1));
        holder.modelTextView.setText(myListData.getModel_name());
        holder.brandTextView.setText(myListData.getBrand_name());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getId(), Toast.LENGTH_LONG).show();

                // send delete request
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setCancelable(true)
                        .setTitle("Confirm")
                        .setMessage("Delete Vehicle Model "+myListData.getModel_name()+" ??")
                        .setPositiveButton("Confirm Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletevehicleAddedMethod(view.getContext(), myListData);
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

    public void add(MyListAddedVehiclesData addData) {
        listdata.add(addData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView srNoTextView, modelTextView, brandTextView;
        public Button deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.srNoTextView = itemView.findViewById(R.id.srNoTextView);
            this.modelTextView = itemView.findViewById(R.id.modelTextView);
            this.brandTextView = itemView.findViewById(R.id.brandTextView);
            this.deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
