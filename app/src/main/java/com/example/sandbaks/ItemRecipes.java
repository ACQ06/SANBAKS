package com.example.sandbaks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRecipes extends AppCompatActivity {

    private RecyclerView itemRecipes;
    private SearchView searchItemRecipes;
    private ElementsCombination combinations = new ElementsCombination();
    private HashMap<List<String>, List<String>> allCombinations = new HashMap<>();
    private ArrayList<ItemRecipeCards> itemRecipeCardsArrayList = new ArrayList<>();
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_recipes);
        init();
    }

    void init(){
        itemRecipes = findViewById(R.id.itemRecipe);
        searchItemRecipes = findViewById(R.id.searchItem);

        combinations.paleolithicAge();
        combinations.BronzeAge();
        combinations.IronAge();
        combinations.SpanishEra();
        combinations.AmericanEra();
        combinations.JapaneseEra();
        combinations.SelfRule();

        allCombinations = combinations.getAllCombinations();

        setupItemRecipes();

        ItemRecipeAdapter adapter = new ItemRecipeAdapter(this, itemRecipeCardsArrayList);

        itemRecipes.setAdapter(adapter);

        itemRecipes.setLayoutManager(new LinearLayoutManager(this));


        searchItemRecipes.clearFocus();
        searchItemRecipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToGame();
            }
        });
    }

    void backToGame(){
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }

    void setupItemRecipes(){
        List<Map.Entry<List<String>, List<String>>> entryList = new ArrayList<>(allCombinations.entrySet());
        entryList.sort(Comparator.comparing(entry -> String.join(", ", entry.getValue())));

        itemRecipeCardsArrayList.clear();

        for (Map.Entry<List<String>, List<String>> entry : entryList) {
            List<String> key = entry.getKey();
            List<String> value = entry.getValue();

            StringBuilder combinations = new StringBuilder();
            StringBuilder results = new StringBuilder();

            for (int i = 0; i < key.size(); i++) {
                combinations.append(key.get(i)).append("(").append(Utils.findItemEra(key.get(i))).append(")");

                if (i < key.size() - 1) {
                    combinations.append(", ");
                }
            }

            for (int i = 0; i < value.size(); i++) {
                results.append(value.get(i)).append("(").append(Utils.findItemEra(value.get(i))).append(")");

                if (i < value.size() - 1) {
                    results.append(", ");
                }
            }

            itemRecipeCardsArrayList.add(new ItemRecipeCards(results.toString(), combinations.toString()));
        }
    }

    void filterList(String newText) {
        newText = newText.toLowerCase().trim();

        ArrayList<ItemRecipeCards> filteredList = new ArrayList<>();

        for (Map.Entry<List<String>, List<String>> entry : allCombinations.entrySet()) {
            List<String> key = entry.getKey();
            List<String> value = entry.getValue();

            StringBuilder combinations = new StringBuilder();
            StringBuilder results = new StringBuilder();

            for (int i = 0; i < key.size(); i++) {
                combinations.append(key.get(i)).append("(").append(Utils.findItemEra(key.get(i))).append(")");

                if (i < key.size() - 1) {
                    combinations.append(", ");
                }
            }

            for (int i = 0; i < value.size(); i++) {
                results.append(value.get(i)).append("(").append(Utils.findItemEra(value.get(i))).append(")");

                if (i < value.size() - 1) {
                    results.append(", ");
                }
            }

            if (combinations.toString().toLowerCase().contains(newText) || results.toString().toLowerCase().contains(newText)) {
                filteredList.add(new ItemRecipeCards(results.toString(), combinations.toString()));
            }
        }

        ItemRecipeAdapter adapter = new ItemRecipeAdapter(this, filteredList);
        itemRecipes.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}