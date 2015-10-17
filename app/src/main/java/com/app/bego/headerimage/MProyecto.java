package com.app.bego.headerimage;


public class MProyecto {

    public String titulo;

    public MLabQ labQ;
    public MLabB labB;
    public MLabF labF;

    public MLabTele labTL;
    public MLabSecTec labST;
    public MLabSecGen labSG;



    //public ArrayList<Elementos> listaCotejo;

    public String folder = "";

    public MProyecto(String t)
    {
        titulo = t;
        //listaCotejo = new ArrayList<>();
        folder = titulo.replace(" ", "_");

        labQ = new MLabQ();
        labB = new MLabB();
        labF = new MLabF();

        labTL = new MLabTele();
        labST = new MLabSecTec();
        labSG = new MLabSecGen();
    }

}


