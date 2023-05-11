package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customercategories);
    }

    public void Meat(View view) {
        String id = "Meat";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }
    public void Dairy(View view) {
        String id = "Dairy";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void Vegetable(View view) {
        String id = "Vegetable";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void Snacks(View view) {
        String id = "Snacks";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }
    public void PersonalCare(View view) {
        String id = "PersonalCare";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void Beverages(View view) {
        String id = "Beverages";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void Furniture(View view) {
        String id = "Furniture";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void BabyProducts(View view) {
        String id = "BabyProducts";
        Bundle bundle = new Bundle();
        Intent i = new Intent(CustomerCategories.this, AdminBrowseProducts.class);
        bundle.putString("Category",id);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void BackArrowC(View view) {
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
    }
}