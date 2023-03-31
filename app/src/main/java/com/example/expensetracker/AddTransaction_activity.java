package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.expensetracker.databinding.ActivityAddTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransaction_activity extends AppCompatActivity {
    ActivityAddTransactionBinding binding;
    String checkBoxType="";
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        binding.expenseCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxType="Expense";
                binding.expenseCheckbox.setChecked(true);
                binding.incomeCheckbox.setChecked(false);

            }
        });

        binding.incomeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxType="Income";
                binding.incomeCheckbox.setChecked(true);
                binding.expenseCheckbox.setChecked(false);
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=binding.userAmount.getText().toString().trim();
                String note=binding.userNote.getText().toString().trim();
                if(amount.length() <= 0 ){
                    Toast.makeText(getApplicationContext(),"Enter amount",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkBoxType.length() <=0){
                    Toast.makeText(AddTransaction_activity.this, "click any of the checkbox", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDateTime=sdf.format(new Date());

                String id= UUID.randomUUID().toString();
                Map<String,Object>transaction = new HashMap<>();
                transaction.put("id",id);
                transaction.put("amount",amount);
                transaction.put("note",note);
                transaction.put("type",checkBoxType);
                transaction.put("date",currentDateTime);

                firebaseFirestore.collection("transaction details").document(firebaseAuth.getUid())
                        .collection("Notes").document(id).set(transaction).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddTransaction_activity.this, "Added", Toast.LENGTH_SHORT).show();
                                binding.userAmount.setText("");
                                binding.userNote.setText("");
                                binding.incomeCheckbox.setChecked(false);
                                binding.expenseCheckbox.setChecked(false);
                            }
                        }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}