package com.example.tarea7_listatareas;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements TareaAdapter.OnClickTarea{

    ArrayList<Tarea> coleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        coleccion= generarArrayDatos();
        setupRecyclerViews();
    }

    private ArrayList<Tarea> generarArrayDatos() {
        return new ArrayList<>(Arrays.asList(
                new Tarea("ADAT","Tarea Json","25-12-2024", "12:00", true),
                new Tarea("ADAT","Tarea XML","25-12-2024","12:00", true ),
                new Tarea("PSP", "Tarea cena filósolfos","25-12-2024","12:00", false)

        ));
    }

    private void setupRecyclerViews() {
        try {

            RecyclerView rvTareas = findViewById(R.id.rv_Tareas);
            TareaAdapter tareaAdapter = new TareaAdapter(coleccion, this);
            rvTareas.setAdapter(tareaAdapter);
            rvTareas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickTarea(View view, int position) {

    }
}