package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.expensetracker.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Update_activity extends AppCompatActivity {
   ActivityUpdateBinding binding;
   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   String newType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


        String id=getIntent().getStringExtra("id");
        String amount=getIntent().getStringExtra("amount");
        String note=getIntent().getStringExtra("note");
        String type=getIntent().getStringExtra("type");

        binding.userAmountUpdate.setText(amount);
        binding.userNoteUpdate.setText(note);

        if(type.equals("Expense")){
            newType="Expense";
            binding.expenseCheckboxUpdate.setChecked(true);
        }else if(type.equals("Income")){
            newType="Income";
            binding.incomeCheckboxUpdate.setChecked(true);
        }
        binding.expenseCheckboxUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType="Expense";
                binding.expenseCheckboxUpdate.setChecked(true);
                binding.incomeCheckboxUpdate.setChecked(false);
            }
        });

        binding.incomeCheckboxUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType="Income";
                binding.incomeCheckboxUpdate.setChecked(true);
                binding.expenseCheckboxUpdate.setChecked(false);
            }
        });

        binding.addBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=binding.userAmountUpdate.getText().toString();
                String note=binding.userNoteUpdate.getText().toString();

                firebaseFirestore.collection("transaction details").document(firebaseAuth.getUid())
                        .collection("Notes").document(id)
                        .update("amount",amount,"note",note,"type",type)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //onBackPressed();
                                Toast.makeText(Update_activity.this, "Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Update_activity.this,Dashboard_activity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Update_activity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("transaction details").document(firebaseAuth.getUid())
                        .collection("Notes").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //onBackPressed();
                                Toast.makeText(Update_activity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Update_activity.this,Dashboard_activity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Update_activity.this,"failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}