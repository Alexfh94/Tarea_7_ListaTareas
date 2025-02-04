package com.example.tarea7_listatareas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class TareaViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Tarea>> tareas;




    public TareaViewModel() {
        tareas = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<Tarea>> getTareas() {
        return tareas;
    }

    public void setTareas(MutableLiveData<ArrayList<Tarea>> tareas) {
        this.tareas = tareas;
    }
}
