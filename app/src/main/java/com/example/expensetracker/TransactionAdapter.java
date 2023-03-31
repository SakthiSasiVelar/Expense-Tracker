package com.example.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    ArrayList<TransactionModal>transactionModalArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModal> transactionModalArrayList) {
        this.context = context;
        this.transactionModalArrayList = transactionModalArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         TransactionModal modal=transactionModalArrayList.get(position);
         String circle=modal.getType();
         if(circle.equals("Expense")){
             holder.circle.setBackgroundResource(R.drawable.circle_red);
         }else{
             holder.circle.setBackgroundResource(R.drawable.circle_green);
         }
         holder.amount.setText(modal.getAmount());
         holder.note.setText(modal.getNote());
         holder.date.setText(modal.getDate());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(context,Update_activity.class);
                 intent.putExtra("id",transactionModalArrayList.get(holder.getAdapterPosition()).getId());
                 intent.putExtra("amount",transactionModalArrayList.get(holder.getAdapterPosition()).getAmount());
                 intent.putExtra("note",transactionModalArrayList.get(holder.getAdapterPosition()).getNote());
                 intent.putExtra("type",transactionModalArrayList.get(holder.getAdapterPosition()).getType());
                 context.startActivity(intent);

             }
         });
    }

    @Override
    public int getItemCount() {
        return transactionModalArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView amount,date,note;
        View circle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amount);
            note=itemView.findViewById(R.id.note);
            date=itemView.findViewById(R.id.date);
            circle=itemView.findViewById(R.id.circle);
        }
    }
}
