package com.example.nutrikids.clases;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id_usuario;
    private String nombre;
    private String apellidos;
    private char sexo;
    private String correo;
    private String clave;

    public Usuario() {
    }

    public Usuario(int id_usuario, String nombre, String apellidos, char sexo, String correo, String clave) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.correo = correo;
        this.clave = clave;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
