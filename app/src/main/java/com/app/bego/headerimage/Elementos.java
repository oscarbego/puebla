package com.app.bego.headerimage;


public class Elementos
{
    public String nombre;
    public String lab;
    public boolean esta;
    public String ok;
    public String foto = ""; // puede ser una lista

    public Elementos(String l, String e)
    {
        nombre = e;
        lab = l;
    }

    public Elementos(String nombre, String lab, boolean esta, String foto)
    {
        this.nombre = nombre;
        this.lab = lab;
        this.esta = esta;
        this.foto = foto;

    }

    public void addFoto()
    {
        esta = true;
        foto = lab + "_" + nombre + ".jpg";
    }

    public void delFoto()
    {
        esta = false;
        foto = "";
    }

}
