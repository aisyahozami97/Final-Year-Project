package com.example.preloveditemandevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder>{

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth,firebaseAuth;
    private Context context;
    BottomNavigationView bottomNavigationView;

    List<String> key;

    ArrayList<CartItem> listCart;
    public ShoppingCartAdapter(Context context, ArrayList<CartItem> listCart){
        this.context = context;
        this.listCart = listCart;

    }
    //ADD THIS IF no adapter attached; skipping layout
    public ShoppingCartAdapter(ArrayList<CartItem> listCart) {
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder_listcart, viewGroup, false);

        context = viewGroup.getContext();

        return new ShoppingCartAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        //myViewHolder.cartImage.setText(listCart.get(i).getUserName());
        myViewHolder.cartName.setText(listCart.get(i).getCartItemName());
        myViewHolder.cartPrice.setText(listCart.get(i).getCartItemPrice());
        //myViewHolder.cartCategory.setText(listCart.get(i).get());
        myViewHolder.cartAvailability.setText(listCart.get(i).getCartItemAvailability());

        //
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String key = list.get(i).getArtistId();
//
//
//
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

        myViewHolder.btnDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                String user = mAuth.getCurrentUser().getUid();
                //firebaseAuth = FirebaseAuth.getInstance();
                //String userid = firebaseAuth.getCurrentUser().getUid();

                String item_name = listCart.get(i).getCartItemName();
                String item_id = listCart.get(i).getItemId();

                DatabaseReference dbCart = FirebaseDatabase.getInstance().getReference("Cart");
                dbCart.child(user).child(item_id).removeValue();


                //Toolbar
                // Remove the item on remove/button click
                listCart.remove(i);
                /*
                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(i);

                /*
                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(i, listCart.size());

                // Show the removed item label
                Toast.makeText(context, item_name + " has been removed", Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return listCart.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cartName,cartPrice,cartAvailability;
        ImageView btnDeleteCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //cartImage = itemView.findViewById(R.id.cartImageItem);
            cartName = itemView.findViewById(R.id.cartItemName);
            cartPrice = itemView.findViewById(R.id.cartItemPrice);
            //cartCategory = itemView.findViewById(R.id.cartItemCategory);
            cartAvailability = itemView.findViewById(R.id.cartItemAvailability);
            btnDeleteCart = itemView.findViewById(R.id.cartDeleteItem);



        }

    }

}
