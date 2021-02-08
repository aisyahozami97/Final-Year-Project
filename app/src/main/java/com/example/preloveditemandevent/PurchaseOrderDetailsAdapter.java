package com.example.preloveditemandevent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseOrderDetailsAdapter extends RecyclerView.Adapter<PurchaseOrderDetailsAdapter.MyViewHolder>{

    private Context mCntxItem;
    private DatabaseReference mPurchaseOrderRef;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private Button mBtnAccept, mBtnReject;

    List<String> key;

    ArrayList<PurchaseOrder> listPurchaseOrder;
    public PurchaseOrderDetailsAdapter(Context mCntxItem,ArrayList<PurchaseOrder> listPurchaseOrder){
        this.mCntxItem = mCntxItem;
        this.listPurchaseOrder = listPurchaseOrder;
    }

    public PurchaseOrderDetailsAdapter(ArrayList<PurchaseOrder> listPurchaseOrder) {
        this.listPurchaseOrder = listPurchaseOrder;
        notifyDataSetChanged();

        //notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder_purchaseorderdetails, viewGroup, false);

        mCntxItem = viewGroup.getContext();

        return new PurchaseOrderDetailsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        //myViewHolder.cartImage.setText(listCart.get(i).getUserName());

        myViewHolder.orderId.setText(listPurchaseOrder.get(i).getOrderId());
        //LOAD IMAGE IN RECYCLER VIEW
        Picasso.get().load(listPurchaseOrder.get(i).getPaymentReceipt()).resize(120, 60).into(myViewHolder.imgReceipt);
        myViewHolder.userFirstName.setText(listPurchaseOrder.get(i).getCustFirstName());
        myViewHolder.orderStatus.setText(listPurchaseOrder.get(i).getOrderStatus());
        myViewHolder.orderDate.setText(listPurchaseOrder.get(i).getOrderDate());



        //
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String key = list.get(i).getArtistId();
//                Intent intent = new Intent(context,DetailsHistoryAdminActivity.class);
//                intent.putExtra("key",key);
////                intent.putExtra("price",price);
////                intent.putExtra("location",player);
////                intent.putExtra("country",price);
////                intent.putExtra("location",player);
////                intent.putExtra("country",price);
//                context.startActivity(intent);
//            }
//        });

        myViewHolder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Firebase connection
                mAuth = FirebaseAuth.getInstance();
                String userId = mAuth.getCurrentUser().getUid();
                //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
                mPurchaseOrderRef = FirebaseDatabase.getInstance().getReference().child("PurchaseOrder").child(userId).child("");

                //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
                mPurchaseOrderRef.child("orderStatus").setValue("To Ship");

            }
        });
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listPurchaseOrder.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, userFirstName, orderStatus, orderDate;
        ImageView imgReceipt;
        ImageView mBtnAccept, mBtnReject;
        //private Intent intent;
        //String imgReceipt = intent.getStringExtra("imgurl");
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //cartImage = itemView.findViewById(R.id.cartImageItem);
            orderId = itemView.findViewById(R.id.poTrackingNo);
            imgReceipt = itemView.findViewById(R.id.imageReceipt);
            userFirstName = itemView.findViewById(R.id.poFnamee);
            orderStatus = itemView.findViewById(R.id.poOrderStatus);
            orderDate = itemView.findViewById(R.id.poDate);
            mBtnAccept = itemView.findViewById(R.id.btnAccept);
            mBtnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
