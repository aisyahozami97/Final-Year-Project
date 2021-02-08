package com.example.preloveditemandevent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListPurchaseOrderAdapter extends RecyclerView.Adapter<ListPurchaseOrderAdapter.ItemViewHolder>  {
    Context mCntxItem;
    private ArrayList<PurchaseOrder> purchaseOrderList;

    public ListPurchaseOrderAdapter(Context mCntxItem, ArrayList<PurchaseOrder> purchaseOrderList) {
        this.mCntxItem = mCntxItem;
        this.purchaseOrderList = purchaseOrderList;
    }

    public ListPurchaseOrderAdapter(ArrayList<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_listpurchaseorder, parent, false);
        mCntxItem = parent.getContext();
        //NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int i) {

        final PurchaseOrder purchaseOrder = purchaseOrderList.get(i);

        holder.orderId.setText(purchaseOrder.getOrderId());
        holder.userName.setText(purchaseOrder.getCustFirstName());
        holder.orderStatus.setText(purchaseOrder.getOrderStatus());
        holder.date.setText(purchaseOrder.getOrderDate());

        Picasso.get().load(purchaseOrder.getPaymentReceipt()).into(holder.imageReceipt);//using picasso to load image

        holder.imgAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final String userId = purchaseOrder.getUserId();
                final String orderId = purchaseOrder.getOrderId();
                final String userName = purchaseOrder.getCustFirstName();
                String orderStatus = purchaseOrder.getOrderStatus();
                String date = purchaseOrder.getOrderDate();
                String imageReceipt = purchaseOrder.getPaymentReceipt();

                final DatabaseReference mPurchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder");

                if(mPurchaseOrderRef != null){
                    mPurchaseOrderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                //purchaseOrderList = new ArrayList<>();
                                for(DataSnapshot ds: dataSnapshot.getChildren())
                                {
                                    for(DataSnapshot ds2: ds.getChildren()) {
                                        purchaseOrderList.add(ds2.getValue(PurchaseOrder.class));
                                        mPurchaseOrderRef.child(userId).child(orderId).child("orderStatus").setValue("To Ship");
                                        //finish();
                                        //Intent intent = new Intent(ListPurchaseOrderAdapter.this, adminHomePage.class);
                                        //startActivity(intent);
                                    }
                                }
                            }
                            //listPurchaseOrderAdapter = new ListPurchaseOrderAdapter(purchaseOrderList);
                           // recyclerView.setAdapter(listPurchaseOrderAdapter);
                            //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
                            //mPurchaseOrderRef.child("orderStatus").setValue("To Ship");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mCntxItem,  "Update Status Successful",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        holder.imgReject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final String userId = purchaseOrder.getUserId();
                final String orderId = purchaseOrder.getOrderId();
                final String userName = purchaseOrder.getCustFirstName();
                String orderStatus = purchaseOrder.getOrderStatus();
                String date = purchaseOrder.getOrderDate();
                String imageReceipt = purchaseOrder.getPaymentReceipt();

                final DatabaseReference mPurchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder");

                if(mPurchaseOrderRef != null){
                    mPurchaseOrderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                //purchaseOrderList = new ArrayList<>();
                                for(DataSnapshot ds: dataSnapshot.getChildren())
                                {
                                    for(DataSnapshot ds2: ds.getChildren()) {
                                        purchaseOrderList.add(ds2.getValue(PurchaseOrder.class));
                                        mPurchaseOrderRef.child(userId).child(orderId).child("orderStatus").setValue("Reject");
                                        //finish();
                                        //Intent intent = new Intent(ListPurchaseOrderAdapter.this, adminHomePage.class);
                                        //startActivity(intent);
                                    }
                                }
                            }
                            //listPurchaseOrderAdapter = new ListPurchaseOrderAdapter(purchaseOrderList);
                            // recyclerView.setAdapter(listPurchaseOrderAdapter);
                            //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
                            //mPurchaseOrderRef.child("orderStatus").setValue("To Ship");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mCntxItem,  "Sorry",Toast.LENGTH_SHORT).show();
                        }
                    });
                }




            }
        });


    }

    @Override
    public int getItemCount()
    {
        return purchaseOrderList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, userName, orderStatus, date;
        LinearLayout parentLayout;
        CardView item_cv;
        ImageView imgAccept, imgReject, imageReceipt;

        public ItemViewHolder(View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            userName = itemView.findViewById(R.id.userName);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            date = itemView.findViewById(R.id.date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //item_cv = itemView.findViewById(R.id.item_cv);
            imageReceipt = itemView.findViewById(R.id.imageReceipt);
            imgAccept = itemView.findViewById(R.id.accept);
            imgReject = itemView.findViewById(R.id.reject);

        }
    }

}

