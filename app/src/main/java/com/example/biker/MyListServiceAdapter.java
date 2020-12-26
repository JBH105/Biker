package com.example.biker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biker.user.user_book_service;
import com.google.android.material.textfield.TextInputEditText;

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
    public void onBindViewHolder(final MyListServiceAdapter.ViewHolder holder, int position) {
        final MyListServiceData myListData = listdata.get(position);
        holder.itemServiceId.setText("SERVICE ID: SVC"+myListData.getServiceId().trim());

        if (myListData.getCancelServicer()) {
            holder.itemCancelBy.setVisibility(View.VISIBLE);
            if (getIsServicer(myListData.getContext()))
                holder.itemCancelBy.setText("Cancelled by You!!");
            else
                holder.itemCancelBy.setText("Cancelled by " + myListData.getServicerName());
            holder.itemCancel.setEnabled(false);
            holder.rlitemShowIfNotCancel.setVisibility(View.GONE);
        } else {
            holder.itemCancelBy.setVisibility(View.GONE);
            holder.itemCancel.setEnabled(true);
            holder.rlitemShowIfNotCancel.setVisibility(View.VISIBLE);
        }
        if (myListData.getCancelUser()) {
            holder.itemCancelBy.setVisibility(View.VISIBLE);
            if (getIsServicer(myListData.getContext()))
                holder.itemCancelBy.setText("Cancelled by " + myListData.getUserName());
            else
                holder.itemCancelBy.setText("Cancelled by You!!");
            holder.itemCancel.setEnabled(false);
            holder.rlitemShowIfNotCancel.setVisibility(View.GONE);
        } else {
            holder.itemCancelBy.setVisibility(View.GONE);
            holder.itemCancel.setEnabled(true);
            holder.rlitemShowIfNotCancel.setVisibility(View.VISIBLE);
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
        if (getIsServicer(myListData.getContext())) {
            holder.llitemAddress.setVisibility(View.VISIBLE);
            holder.itemAddress.setText(myListData.getAddress());
        } else {
            holder.llitemAddress.setVisibility(View.GONE);
        }
        holder.itemVehicleNumber.setText(myListData.getVehicleNumber());
        holder.itemModel.setText(myListData.getModel());
        holder.itemBrand.setText(myListData.getBrand());
        holder.itemProblemExplanation.setText(myListData.getProblemExplanation());

        if (getIsServicer(myListData.getContext())) {
            holder.itemAccept.setEnabled(true);
            if (holder.itemAccept.isChecked()) {
                holder.itemAccept.setEnabled(false);
                holder.itemSolved.setEnabled(true);
                holder.itemRemarks.setEnabled(true);
            }
            holder.itemReview.setEnabled(false);

            holder.itemRemarks.setText("Click Here to write Remark");
            holder.itemReview.setText("N/A");

        } else {
            holder.itemAccept.setEnabled(false);
            holder.itemSolved.setEnabled(false);
            holder.itemRemarks.setEnabled(false);
            if (holder.itemSolved.isChecked())
                holder.itemReview.setEnabled(true);

            holder.itemRemarks.setText("N/A");
            holder.itemReview.setText("Click Here to give Review");

        }

        holder.itemAccept.setChecked(myListData.getAccept());
        holder.itemSolved.setChecked(myListData.getSolved());
        if (holder.itemAccept.isChecked()) {
            if (!myListData.getRemarks().trim().isEmpty() || (myListData.getRemarks() != null)) {
                holder.itemRemarks.setText(myListData.getRemarks());
            }
        }
        if (holder.itemSolved.isChecked()) {
            if (!myListData.getReview().trim().isEmpty() || (myListData.getReview() != null)) {
                holder.itemReview.setText(myListData.getReview());
                holder.itemReview.setEnabled(false);
            }
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
                                holder.itemCancel.setEnabled(false);
                                if (getIsServicer(view.getContext()))
                                    new MyListServiceMethods().CancelServiceMethod(view.getContext(), myListData, myListData.getJsonObjectFirstMethod(), "servicer");
                                else
                                    new MyListServiceMethods().CancelServiceMethod(view.getContext(), myListData, myListData.getJsonObjectFirstMethod(), "user");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.itemCancel.setEnabled(true);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        holder.itemAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(myListData.getContext());

                    // Layout Inflator
                    LayoutInflater layoutInflater = (LayoutInflater) myListData.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = layoutInflater.inflate(R.layout.accept_alertdialog, null);

                    builder.setCancelable(true)
                            .setTitle("Confirm Accept")
                            .setView(view)
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.itemRemarks.setEnabled(true);
                                    String remarkToSend;
                                    TextInputEditText acceptRemarks = view.findViewById(R.id.acceptRemarks);
                                    if (acceptRemarks.getText().toString().trim().isEmpty())
                                        remarkToSend = "";
                                    else
                                        remarkToSend = acceptRemarks.getText().toString().trim();
                                    if (getIsServicer(myListData.getContext()))
                                        new MyListServiceMethods().AcceptServiceMethod(myListData.getContext(), myListData, myListData.getJsonObjectFirstMethod(), remarkToSend);
                                    holder.itemAccept.setEnabled(false);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.itemAccept.setChecked(false);
                                    holder.itemAccept.setEnabled(true);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });

        holder.itemSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(myListData.getContext())
                            .setCancelable(true)
                            .setTitle("Confirm Solved")
                            .setMessage("Is Service Solved" + " ??")
                            .setPositiveButton("Solved", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.itemSolved.setEnabled(false);
                                    if (getIsServicer(myListData.getContext()))
                                        new MyListServiceMethods().SolvedServiceMethod(myListData.getContext(), myListData, myListData.getJsonObjectFirstMethod());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.itemSolved.setChecked(false);
                                    holder.itemSolved.setEnabled(true);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });

        holder.itemRemarks.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(myListData.getContext());

                // Layout Inflator
                LayoutInflater layoutInflater = (LayoutInflater) myListData.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewll = layoutInflater.inflate(R.layout.remarks_alertdialog, null);

                builder.setCancelable(true)
                        .setTitle("Servicer Remark")
                        .setView(viewll)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String remarkToSend;
                                TextInputEditText remarkRemarks = viewll.findViewById(R.id.remarkRemarks);
                                if (remarkRemarks.getText().toString().trim().isEmpty())
                                    remarkToSend = "";
                                else
                                    remarkToSend = remarkRemarks.getText().toString().trim();
                                if (getIsServicer(myListData.getContext()))
                                    new MyListServiceMethods().RemarkServiceMethod(myListData.getContext(), myListData, myListData.getJsonObjectFirstMethod(), remarkToSend);
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

        holder.itemReview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(myListData.getContext());

                // Layout Inflator
                LayoutInflater layoutInflater = (LayoutInflater) myListData.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewll = layoutInflater.inflate(R.layout.review_alertdialog, null);

                builder.setCancelable(true)
                        .setTitle("Review")
                        .setView(viewll)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String reviewToSend;
                                TextInputEditText reviewReviews = viewll.findViewById(R.id.reviewReviews);
                                if (reviewReviews.getText().toString().trim().isEmpty())
                                    reviewToSend = "";
                                else
                                    reviewToSend = reviewReviews.getText().toString().trim();
                                if (!getIsServicer(myListData.getContext()))
                                    new MyListServiceMethods().ReviewServiceMethod(myListData.getContext(), myListData, myListData.getJsonObjectFirstMethod(), reviewToSend);
                                holder.itemReview.setEnabled(false);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.itemReview.setEnabled(true);
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
        public TextView itemCancelBy, itemDate, itemServicer, itemUser, itemMobile, itemAddress;
        public TextView itemVehicleNumber, itemModel, itemBrand;
        public TextView itemProblemExplanation;
        public CheckBox itemAccept, itemSolved;
        public TextView itemRemarks;
        public TextView itemReview;
        public LinearLayout llitemServicer, llitemUser, llitemAddress;
        public RelativeLayout rlitemShowIfNotCancel;

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

            this.itemAddress = itemView.findViewById(R.id.itemaddress);
            this.llitemAddress = itemView.findViewById(R.id.llitemAddress);

            this.rlitemShowIfNotCancel = itemView.findViewById(R.id.rlitemShowIfNotCancel);
        }
    }
}
