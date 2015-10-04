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
    boolean esta;
    String foto; // puede ser una lista

    public Elementos(String e)
    {

        nombre = e;
    }

}
