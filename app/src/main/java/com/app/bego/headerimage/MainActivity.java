package com.app.bego.headerimage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    Gson g = new Gson();

    private MainActivity I;
    private TextView stickyView;
    private ListView listView;
    private View heroImageView;
    private View stickyViewSpacer;
    public static MProyecto mProyecto;

    public static ArrayList<MProyecto> lista; // = new ArrayList<>();

    public VersionAdapter va;
    public String[] archivos;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        I = this;


        //-----


        File mPath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/AppPuebla/");

        if(!mPath.exists())
            mPath.mkdir();
        else
            System.out.println("El folder ya existe");




        //-----

        //archivos = fileList();

        //for(int i = 0; i < archivos.length; i++)
        //    System.out.println(archivos[i]);

        //if (existe(archivos, "notas.txt"))
        //    System.out.println(" 74 -- Lectura ---> " + readFile("notas.txt"));


        //    System.out.println("Hay notas");


                Type listOfTestObject = new TypeToken<List<MProyecto>>(){}.getType();
                //String s = g.toJson(lista, listOfTestObject);

                if (fileExstSD("notas.txt"))
                {
                    System.out.println("ok notas");
                    lista = g.fromJson(readFromSd("notas.txt"), listOfTestObject);
                }

                else
                {
                    System.out.println("no hay notas");
                    lista = new ArrayList<>();
                }



                //else
                //{
                //    System.out.println("lista nula");
                //    lista = new ArrayList<>();
                //}



        /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
        heroImageView = findViewById(R.id.heroImageView);
        stickyView = (TextView) findViewById(R.id.stickyView);


        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header, null);

        stickyViewSpacer  = listHeader.findViewById(R.id.stickyViewPlaceholder);



        /* Add list view header */
        listView.addHeaderView(listHeader);

        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY ));



                    /* Set the image to scroll half of the amount that of ListView */
                    heroImageView.setY(topY * 0.5f);
                }
            }
        });


        /* Populate the ListView with sample data */
        //List<String> modelList = new ArrayList<>();

        //for (int i = 0; i < MAX_ROWS; i++) {
        //    lista.add("List item " + i);
        //}

        //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_row, modelList);
        //listView.setAdapter(adapter);

        va = new VersionAdapter();

        listView.setAdapter(va);

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        if (position > 0) {


                            mProyecto = lista.get(position - 1);

                            AlertDialog.Builder alert = new AlertDialog.Builder(I);
                            alert.setTitle("Eliminar Proyecto!");

                            alert.setMessage("Estas seguro de eleiminar el proyecto " + mProyecto.titulo + " ?");

                            // Make an "OK" button to save the name
                            alert.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            lista.remove(mProyecto);
                                            va.notifyDataSetChanged();
                                            String cade = g.toJson(lista);

                                            saveSD("notas.txt", cade);
                                        }
                                    }
                            );

                            alert.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }
                            );

                            alert.show();

                        }

                            return false;
                    }

                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    mProyecto = lista.get(position - 1);
                    //Intent i = new Intent(getBaseContext(), Proyecto.class);
                    Intent i = new Intent(getBaseContext(), Laboratorios.class);
                    startActivity(i);
                }

            }
        });


        setTitle("ChckLabs");

        System.out.println("------------ DS -------------");

        //saveSD("");
        System.out.println("------> " + readFromSd("notas.txt"));

        System.out.println("------------ DS -------------");
    }


    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }


    public String readFile(String file)
    {
        String texto = null;
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(file)));

            texto = fin.readLine();
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }

        return texto;
    }


    public void save2(String str)
    {

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("notas.txt", Context.MODE_PRIVATE));

            fout.write(str);
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }


    }


    public boolean checkEdoSd()
    {
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            System.out.println("Environment.MEDIA_MOUNTED");
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            System.out.println("Environment.MEDIA_MOUNTED_READ_ONLY");
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else
        {
            System.out.println("False False");
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

        return sdDisponible && sdAccesoEscritura;
    }

    public boolean fileExstSD(String fileName)
    {

        File ruta_sd = Environment.getExternalStorageDirectory();

        boolean bande;


        //File f = new File(getExternalFilesDir(null), fileName);
        File f = new File(ruta_sd + "/AppPuebla/", fileName);

        System.out.println("----------> " + getExternalFilesDir(null));
        //File file = new File(Environment.getExternalStorageDirectory() + "/book1/page2.html");
        //File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
        bande = f.exists();
        if (bande) {
            /*...*/
        }

        return bande;
    }

    public void saveSD(String file, String data)
    {
        if (checkEdoSd())
        {
            System.out.println("Save SD");
            try
            {


                File ruta_sd = Environment.getExternalStorageDirectory();


                //File f = new File(ruta_sd.getAbsolutePath(), "datos\\prueba.txt");
                //File f = new File(getExternalFilesDir(null) + "/AppPuebla/", file);
                File f = new File(ruta_sd + "/AppPuebla/", file);

                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write(data);
                fout.close();
            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }

        }

    }


    public String readFromSd(String file)
    {
        String texto = null;
        if (checkEdoSd()) {

            System.out.println("readFromSd");
            try {
                File ruta_sd = Environment.getExternalStorageDirectory();

                //File f = new File(ruta_sd.getAbsolutePath(), "prueba.txt");
                //File f = new File(getExternalFilesDir(null) , file);
                File f = new File(ruta_sd + "/AppPuebla/" , file);

                BufferedReader fin =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)));

                texto = fin.readLine();
                fin.close();
            } catch (Exception ex) {
                Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
            }
        }

        return texto;
    }


    public void grabar(View v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "notas.txt", Activity.MODE_PRIVATE));

            //guarda un texto

            archivo.write("");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        Toast t = Toast.makeText(this, "Los datos fueron grabados",
                Toast.LENGTH_SHORT);
        t.show();
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.add) {

            //----------

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Agragar Proyecto!");
            alert.setMessage("Escribe el nombre del Proyecto");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    //SharedPreferences.Editor e = mSharedPreferences.edit();
                    //e.putString(PREF_NAME, inputName);
                    //e.commit();

                    // Welcome the new user
                    //Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!", Toast.LENGTH_LONG).show();

                    if(!inputName.equals(""))
                    {

                        mProyecto = new MProyecto(inputName);

                        //----------

                            mProyecto.labQ.elementos.add(new Elementos("Q", "Vasos de precipitado"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Embudos"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Cristalizadores (Pirex)"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Matraces de fondo plano"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Matraces Erlenmeyer"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Cajas de Petri"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Vidrios de reloj"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Frascos para reactivos, tapón esmerilado"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Campanas de cristal"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Cajas de Portaobjetos"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Cajas de CubObjetos"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Tubos de Ensayo con Labio"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Frascos Gotero color Ámbar"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Gotero con Bulbo de Hule"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Probetas Graduadas"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Pipetas Graduadas"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "1/2 Kg. De Tubo de Vidrio de  6mm"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Lámparas de Alcohol"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Alcohol de 96°"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Ácido Clorhídico"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Ácidi Nítrico"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Ácido Acético"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Agua Oxigenada"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Aldehído Fórmico"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Azul de Metileno"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Bálsamo de  Canadá"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Éter Sulfúrico"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Licor de Fehling \"A\"."));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Lico de Fehiling  \"B\""));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Grenetina en Polvo"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Hematoxilina Preparada"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Hidróxido de Calcio"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Lugol"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Rojo Neutro"));
                        mProyecto.labQ.elementos.add(new Elementos("Q", "Xilol"));

                        //----------

                        lista.add(mProyecto);

                        String cade = g.toJson(lista);
                        System.out.println(" 367 --------> " + cade);
                        saveSD("notas.txt", cade);

                        File mPath = new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/AppPuebla/" + mProyecto.folder + "/");

                        if(!mPath.exists())
                            mPath.mkdir();
                        else
                            System.out.println("El folder ya existe");



                        va.notifyDataSetChanged();
                        //Intent i = new Intent(getBaseContext(), Proyecto.class);
                        Intent i = new Intent(getBaseContext(), Laboratorios.class);
                        System.out.println(" 370 intento 2");
                        startActivity(i);

                    }


                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();

            //----------
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class VersionAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            if(lista == null)
                return 0;

            return lista.size(); // cades.length;
        }

        @Override
        public Object getItem(int position) {
            if(position < lista.size())
                return lista.get(position); //cades[position];

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View listItem = convertView;
            int pos = position;

            if (listItem == null) {
                listItem =  getLayoutInflater().inflate(R.layout.lista_elemento, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.img_miniatura);
            TextView tvTitle = (TextView) listItem.findViewById(R.id.proyecto);
            TextView tvDesc = (TextView) listItem.findViewById(R.id.etiqueta);

            // Set the views in the layout
            iv.setBackgroundResource(R.drawable.folder_search);
            tvTitle.setText(lista.get(pos).titulo);
            //tvDesc.setText(desc[pos]);

            return listItem;


        }
    }




}
