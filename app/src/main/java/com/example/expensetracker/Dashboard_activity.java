package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.expensetracker.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Dashboard_activity extends AppCompatActivity {
   ActivityDashboardBinding binding;
   FirebaseFirestore firebaseFirestore;
   FirebaseAuth firebaseAuth;

   int sumExpense=0;
   int sumIncome=0;
   ArrayList<TransactionModal>transactionModalArrayList;
   TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        transactionModalArrayList=new ArrayList<>();
        transactionAdapter=new TransactionAdapter(this,transactionModalArrayList);

        binding.historyRecycler.setAdapter(transactionAdapter);
        binding.historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecycler.setHasFixedSize(true);

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(Dashboard_activity.this,MainActivity.class));
                    finish();
                }
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(getApplicationContext(), AddTransaction_activity.class));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        loadData();
    }
    public void loadData(){
        firebaseFirestore.collection("transaction details").document(firebaseAuth.getUid()).
                collection("Notes").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot ds : task.getResult()) {
                            TransactionModal modal = new TransactionModal(ds.getString("id"),
                                    ds.getString("type"),
                                    ds.getString("amount"),
                                    ds.getString("note"),
                                    ds.getString("date"));
                            int amount=Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense")){
                                sumExpense=sumExpense+amount;
                            }else{
                                sumIncome=sumIncome+amount;
                            }
                            transactionModalArrayList.add(modal);

                        }
                        binding.totalIncome.setText(String.valueOf(sumIncome));
                        binding.totalExpense.setText(String.valueOf(sumExpense));
                        binding.totalBalance.setText(String.valueOf(sumIncome-sumExpense));

                        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                refresh();
                            }
                        });
                        transactionAdapter=new TransactionAdapter(Dashboard_activity.this,transactionModalArrayList);
                        binding.historyRecycler.setAdapter(transactionAdapter);
                    }
                });
    }

    public void createDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Dashboard_activity.this)
                .setTitle("SignOut")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();

    }
    public void refresh(){
        startActivity(new Intent(Dashboard_activity.this,Dashboard_activity.class));
    }
}