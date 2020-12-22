package com.example.biker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.biker.user.user_book_service;

import java.util.List;

import static com.example.biker.Urls.getIsServicer;

public class MyListServiceAdapter extends RecyclerView.Adapter<MyListServiceAdapter.ViewHolder> {
    private List<MyListServiceData> listdata;

    // RecyclerView recyclerView;
    public MyListServiceAdapter(List<MyListServiceData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public MyListServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_list_service, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyListServiceAdapter.ViewHolder holder, int position) {
        final MyListServiceData myListData = listdata.get(position);
        holder.itemServiceId.setText("SERVICE ID: SVC"+myListData.getServiceId().trim());
        if (myListData.getCancelServicer()) {
            holder.itemCancelBy.setVisibility(View.VISIBLE);
            if (getIsServicer(myListData.getContext()))
                holder.itemCancelBy.setText("Cancelled by You!!");
            else
                holder.itemCancelBy.setText("Cancelled by " + myListData.getServicerName());
            holder.itemCancel.setEnabled(false);
        } else {
            holder.itemCancelBy.setVisibility(View.GONE);
        }
        if (myListData.getCancelUser()) {
            holder.itemCancelBy.setVisibility(View.VISIBLE);
            if (getIsServicer(myListData.getContext()))
                holder.itemCancelBy.setText("Cancelled by " + myListData.getUserName());
            else
                holder.itemCancelBy.setText("Cancelled by You!!");
            holder.itemCancel.setEnabled(true);
        } else {
            holder.itemCancelBy.setVisibility(View.GONE);
        }
        holder.itemDate.setText(myListData.getDate());
        if (getIsServicer(myListData.getContext())) {
            holder.llitemServicer.setVisibility(View.GONE);
            holder.llitemUser.setVisibility(View.VISIBLE);
            holder.itemUser.setText(myListData.getUserName());
        } else {
            holder.llitemUser.setVisibility(View.GONE);
            holder.llitemServicer.setVisibility(View.VISIBLE);
            holder.itemServicer.setText(myListData.getServicerName());
        }
        holder.itemMobile.setText(myListData.getMobile());
        holder.itemVehicleNumber.setText(myListData.getVehicleNumber());
        holder.itemModel.setText(myListData.getModel());
        holder.itemBrand.setText(myListData.getBrand());
        holder.itemProblemExplanation.setText(myListData.getProblemExplanation());

        if (getIsServicer(myListData.getContext())) {
            holder.itemAccept.setEnabled(true);
            holder.itemAccept.setEnabled(true);
            if (holder.itemAccept.isChecked())
                holder.itemRemarks.setEnabled(true);
            holder.itemReview.setEnabled(true);
        } else {
            holder.itemAccept.setEnabled(false);
            holder.itemAccept.setEnabled(false);
            holder.itemRemarks.setEnabled(false);
            if (holder.itemSolved.isChecked())
                holder.itemReview.setEnabled(true);
        }


        holder.itemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Toast.makeText(view.getContext(), "click on item: " + myListData.getUser_id(), Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setCancelable(true)
                        .setTitle("Confirm")
                        .setMessage("Cancel Service" + " ??")
                        .setPositiveButton("Confirm Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (getIsServicer(view.getContext()))
                                    new MyListServiceMethods().CancelServiceMethod(view.getContext(), myListData, "servicer");
                                else
                                    new MyListServiceMethods().CancelServiceMethod(view.getContext(), myListData, "user");
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

    public void add(MyListServiceData addData) {
        listdata.add(addData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemServiceId;
        public ImageView itemCancel;
        public TextView itemCancelBy, itemDate, itemServicer, itemUser, itemMobile;
        public TextView itemVehicleNumber, itemModel, itemBrand;
        public TextView itemProblemExplanation;
        public CheckBox itemAccept, itemSolved;
        public EditText itemRemarks;
        public EditText itemReview;
        public LinearLayout llitemServicer, llitemUser;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemServiceId = itemView.findViewById(R.id.itemServiceId);
            this.itemCancel = itemView.findViewById(R.id.itemCancel);
            this.itemCancelBy = itemView.findViewById(R.id.itemCancelBy);
            this.itemDate = itemView.findViewById(R.id.itemDate);
            this.itemServicer = itemView.findViewById(R.id.itemServicer);
            this.itemUser = itemView.findViewById(R.id.itemUser);
            this.itemMobile = itemView.findViewById(R.id.itemMobile);
            this.itemVehicleNumber = itemView.findViewById(R.id.itemVehicleNumber);
            this.itemModel = itemView.findViewById(R.id.itemModel);
            this.itemBrand = itemView.findViewById(R.id.itemBrand);
            this.itemProblemExplanation = itemView.findViewById(R.id.itemProblemExplanation);
            this.itemAccept = itemView.findViewById(R.id.itemAccept);
            this.itemSolved = itemView.findViewById(R.id.itemSolved);
            this.itemRemarks = itemView.findViewById(R.id.itemRemarks);
            this.itemReview = itemView.findViewById(R.id.itemReview);

            this.llitemServicer = itemView.findViewById(R.id.llitemServicer);
            this.llitemUser = itemView.findViewById(R.id.llitemUser);
        }
    }
}
