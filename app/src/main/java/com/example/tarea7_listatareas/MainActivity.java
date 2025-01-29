package com.example.tarea7_listatareas;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements TareaAdapter.OnClickTarea,DialogFab.OnTaskCreatedListener,DialogFab.OnTaskEditedListener{

    private ArrayList<Tarea> coleccion = new ArrayList<>();
    private TareaAdapter tareaAdapter;
    SQLiteDatabase bdLeer;
    SQLiteDatabase bdEscribir;
    ContentValues contentValues;
    TareaViewModel tareaViewModel;


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

        tareaViewModel = new ViewModelProvider(this).get(TareaViewModel.class);
        tareaViewModel.getTareas().observe(this, tareas -> {
            coleccion = tareas;
            ordenar();
            tareaAdapter.notifyDataSetChanged();
        });




        configDatabase();
        generarArrayDatos();
        setupRecyclerViews();
        setupFab();
        ordenar();

    }

    private void generarArrayDatos() {
        String asignatura = "";
        String descripcion ="";
        String fecha ="";
        String hora ="";
        Boolean estado =false;
        Integer id = 0;

        String consulta = "SELECT * FROM tareas";
        Cursor cursor = bdLeer.rawQuery(consulta,
                null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                asignatura = cursor.getString(1);
                descripcion = cursor.getString(2);
                fecha = cursor.getString(3);
                hora = cursor.getString(4);
                estado = cursor.getInt(5) == 1;
                Tarea tarea = new Tarea(asignatura,descripcion,fecha,hora,estado,id);
                coleccion.add(tarea);
                tareaViewModel.getTareas().setValue(coleccion);

            } while (cursor.moveToNext());
            cursor.close();
        }

    }

    private void setupRecyclerViews() {
        RecyclerView rvTareas = findViewById(R.id.rv_Tareas);
        tareaAdapter = new TareaAdapter(new ArrayList<>(), this);
        rvTareas.setAdapter(tareaAdapter);
        rvTareas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Observa los cambios en el LiveData
        tareaViewModel.getTareas().observe(this, tareas -> {
            tareaAdapter.updateData(tareas);  // Actualizar el adaptador
        });
    }


    public void setupFab(){

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogFragment = new DialogFab();
                Bundle bundle = new Bundle();
                bundle.putInt("tamaño", coleccion.size());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "EmptyDialog");
            }
        });


    }

    @Override
    public void onClickTarea(View view, int position) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_tarea, null);

        Tarea tarea = coleccion.get(position);

        bottomSheetView.findViewById(R.id.option_editar).setOnClickListener(v -> {

            DialogFragment dialogFragment = new DialogFab();
            Bundle bundle = new Bundle();
            Tarea tareaActual = coleccion.get(position);

            bundle.putString("asignatura", tareaActual.getAsignatura());
            bundle.putString("descripcion", tareaActual.getDescripcion());
            bundle.putString("fecha", tareaActual.getFecha());
            bundle.putString("hora", tareaActual.getHora());
            bundle.putInt("id", tareaActual.getId());

            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "EmptyDialog");

            bottomSheetDialog.dismiss();

        });

        bottomSheetView.findViewById(R.id.option_eliminar).setOnClickListener(v -> {
            ArrayList<Tarea> tareas = tareaViewModel.getTareas().getValue();

            if (tareas != null) {
                tareas.remove(position);  // Eliminar la tarea
                tareaViewModel.getTareas().setValue(tareas);  // Actualizar el LiveData
            }
            bdEscribir.delete("tareas","id = ?",new String[]{String.valueOf(tarea.getId())});

            bottomSheetDialog.dismiss();
        });

        TextView marcarCompletadoOption = bottomSheetView.findViewById(R.id.option_marcar_completado);
        if (tarea.getEstado()) {
            marcarCompletadoOption.setText("Marcar como pendiente");
            marcarCompletadoOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending_icon_foreground, 0, 0, 0);
        } else {
            marcarCompletadoOption.setText("Marcar como completado");
            marcarCompletadoOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.complete_icon_foreground, 0, 0, 0);
        }

        marcarCompletadoOption.setOnClickListener(v -> {
            if (!tarea.getEstado()) {
                Log.d("BottomSheet", "Marcar como completada: " + tarea.getDescripcion());
                tarea.setEstado(true);
                contentValues.put("estado", 1);
                bdEscribir.update("tareas",contentValues,"id=?",new String[]{String.valueOf(tarea.getId())});
                marcarCompletadoOption.setText("Marcar como pendiente");
                marcarCompletadoOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending_icon_foreground, 0, 0, 0); // Cambiar a "X"
            } else {
                Log.d("BottomSheet", "Marcar como pendiente: " + tarea.getDescripcion());
                tarea.setEstado(false);
                contentValues.put("estado", 0);
                bdEscribir.update("tareas",contentValues,"id=?",new String[]{String.valueOf(tarea.getId())});
                marcarCompletadoOption.setText("Marcar como completado");
                marcarCompletadoOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.complete_icon_foreground, 0, 0, 0); // Cambiar a "check"
            }
            tareaAdapter.notifyItemChanged(position);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetDialog.show();
    }



    @Override
    public void onTaskCreated(Tarea tarea) {
        ArrayList<Tarea> tareas = tareaViewModel.getTareas().getValue();
        if (tareas != null) {
            tareas.add(tarea);  // Añadir la nueva tarea
            tareaViewModel.getTareas().setValue(tareas);  // Actualizar el LiveData
        }

        contentValues.put("asignatura", tarea.getAsignatura());
        contentValues.put("descripcion",tarea.getDescripcion());
        contentValues.put("fecha", tarea.getFecha());
        contentValues.put("hora", tarea.getHora());
        contentValues.put("estado",tarea.getEstado());
        contentValues.put("id",tarea.getId());

        bdEscribir.insert("tareas",null,contentValues);



    }

    @Override
    public void onTaskEdited(Tarea tareaEditada) {
        ArrayList<Tarea> tareas = tareaViewModel.getTareas().getValue();
        if (tareas != null) {
            for (int i = 0; i < tareas.size(); i++) {
                if (tareas.get(i).getId() == tareaEditada.getId()) {
                    tareas.set(i, tareaEditada);  // Reemplazar la tarea editada
                    tareaViewModel.getTareas().setValue(tareas);  // Actualizar el LiveData
                    break;
                }
            }

            // Realizar la actualización en la base de datos
            contentValues.put("asignatura", tareaEditada.getAsignatura());
            contentValues.put("descripcion", tareaEditada.getDescripcion());
            contentValues.put("fecha", tareaEditada.getFecha());
            contentValues.put("hora", tareaEditada.getHora());
            contentValues.put("estado", tareaEditada.getEstado());
            contentValues.put("id", tareaEditada.getId());
            bdEscribir.update("tareas", contentValues, "id = ?", new String[]{String.valueOf(tareaEditada.getId())});


        }
    }


    private void ordenar() {
        coleccion.sort((tarea1, tarea2) -> tarea1.getAsignatura().compareToIgnoreCase(tarea2.getAsignatura()));
    }

    private void configDatabase(){

        bdLeer = new BaseDatos(this).getReadableDatabase();
        bdEscribir = new BaseDatos(this).getWritableDatabase();
        contentValues = new ContentValues();
    }

}