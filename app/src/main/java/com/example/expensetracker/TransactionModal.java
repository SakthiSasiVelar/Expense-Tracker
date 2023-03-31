package com.example.expensetracker;

public class TransactionModal {
    private  String id,type,amount,note,date;
    public  TransactionModal(){

    }

    public TransactionModal(String id,String type,String amount, String note,String date){
            this.id=id;
            this.type=type;
            this.amount=amount;
            this.note=note;
            this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
