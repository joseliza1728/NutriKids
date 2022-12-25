package com.example.nutrikids.clases;


    public class Receta {

        private int id_receta;
        private String nombre;
        private String descripcion;
        private String ingredientes;
        private String preparacion;
        private String foto;

        public Receta(int id_receta, String nombre, String descripcion, String ingredientes, String preparacion, String foto) {
            this.id_receta = id_receta;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.ingredientes = ingredientes;
            this.preparacion = preparacion;
            this.foto = foto;
        }

        public int getId_receta() {
            return id_receta;
        }

        public void setId_receta(int id_receta) {
            this.id_receta = id_receta;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getIngredientes() {
            return ingredientes;
        }

        public void setIngredientes(String ingredientes) {
            this.ingredientes = ingredientes;
        }

        public String getPreparacion() {
            return preparacion;
        }

        public void setPreparacion(String preparacion) {
            this.preparacion = preparacion;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }
    }
