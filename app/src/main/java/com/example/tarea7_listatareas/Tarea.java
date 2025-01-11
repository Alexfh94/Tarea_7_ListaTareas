package com.example.tarea7_listatareas;

public class Tarea {

    private String asignatura;
    private String descripcion;
    private String fecha;
    private String hora;
    private Boolean estado;
    private int id;


    public Tarea(String asignatura, String descripcion, String fecha, String hora, Boolean estado, int id) {
        this.asignatura = asignatura;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.id = id;
    }


    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
