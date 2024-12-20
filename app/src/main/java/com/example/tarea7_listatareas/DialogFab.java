package com.example.tarea7_listatareas;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class DialogFab extends androidx.fragment.app.DialogFragment {

    private Spinner spinner;
    private EditText textDescripcion;
    private EditText textDate;
    private EditText textTime;
    private Button accept;
    private Button cancel;

    private OnTaskCreatedListener listener;


    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view, null);

        setupComponents(view);


        builder.setView(view);

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskCreatedListener) {
            listener = (OnTaskCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }



    private void setupComponents(View view){

        textDate = view.findViewById(R.id.editTextDate);
        textTime = view.findViewById(R.id.editTextTime);
        textDescripcion= view.findViewById(R.id.textAsignatura);
        accept = view.findViewById(R.id.buttonAceptar);
        cancel = view.findViewById(R.id.buttonCancelar);
        spinner = view.findViewById(R.id.spinner);

        textDate.setOnClickListener(v -> showDatePicker(textDate));
        textTime.setOnClickListener(v -> showTimePicker(textTime));
        accept.setOnClickListener(v -> acceptAction(accept));
        cancel.setOnClickListener(v -> cancelAction(cancel));

    }



    private void showDatePicker(EditText textDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textDate.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(EditText textTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    textTime.setText(formattedTime);
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void acceptAction(Button button) {
        boolean isValid = true;

        if (textDescripcion.getText().toString().trim().isEmpty()) {
            textDescripcion.setError("Este campo es obligatorio");
            isValid = false;
        }

        if (textDate.getText().toString().trim().isEmpty()) {
            textDate.setError("Seleccione una fecha");
            isValid = false;
        }

        if (textTime.getText().toString().trim().isEmpty()) {
            textTime.setError("Seleccione una hora");
            isValid = false;
        }

        if (isValid) {
            Tarea tarea = new Tarea(
                    spinner.getSelectedItem().toString(),
                    textDescripcion.getText().toString(),
                    textDate.getText().toString(),
                    textTime.getText().toString(),
                    false
            );

            listener.onTaskCreated(tarea);
            dismiss();
        }
    }

    private void cancelAction(Button cancel) {
        dismiss();
    }


    //interfaz para notificar de la nueva tarea y actualizar el recyclerview
    public interface OnTaskCreatedListener {
        void onTaskCreated(Tarea tarea);
    }


}
