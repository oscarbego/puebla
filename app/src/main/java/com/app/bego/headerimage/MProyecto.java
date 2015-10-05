package com.app.bego.headerimage;


import java.util.ArrayList;

public class MProyecto {

    public String titulo;

    public ArrayList<Elementos> listaCotejo;
    public String folder;

    public MProyecto(String t)
    {
        titulo = t;
        listaCotejo = new ArrayList<>();
        folder = titulo.replace(" ", "_");
    }

}

class Elementos
{
    String nombre;
    boolean esta = false;
    String foto = ""; // puede ser una lista

    public Elementos(String e)
    {
        nombre = e;
    }

    public void addFoto()
    {
        esta = true;
        foto = nombre + ".jpg";
    }

    public void delFoto()
    {
        esta = false;
        foto = "";
    }

}
