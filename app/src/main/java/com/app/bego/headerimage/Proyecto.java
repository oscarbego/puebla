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
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Proyecto extends AppCompatActivity {


    private TextView stickyView;
    private SearchView stickyView1;
    private ListView listView;
    private View heroImageView;
    private View stickyViewSpacer;
    private View stickyViewSpacer1;

    private ArrayList<Elementos> lista; // = new ArrayList<>();

    Gson g = new Gson();

    VersionAdapter va;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);


        lista = MainActivity.mProyecto.listaCotejo;

        listView = (ListView) findViewById(R.id.listViewP);
        heroImageView = findViewById(R.id.heroImageViewP);
        stickyView = (TextView) findViewById(R.id.stickyViewP);
        stickyView1 = (SearchView) findViewById(R.id.stickyView1P);


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header2, null);

        stickyViewSpacer  = listHeader.findViewById(R.id.stickyViewPlaceholder);
        stickyViewSpacer1  = listHeader.findViewById(R.id.stickyViewPlaceholder1);



        listView.addHeaderView(listHeader);

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
                    stickyView1.setY(Math.max(0, heroTopY + topY));

                    heroTopY = stickyViewSpacer1.getTop();
                    //stickyView.setY(Math.max(70, heroTopY  + topY ));
                    stickyView.setY(Math.max(70, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                    heroImageView.setY(topY * 0.5f);
                }
            }
        });

        va = new VersionAdapter();

        listView.setAdapter(va);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getBaseContext(), Proyecto.class);
                startActivity(i);
            }
        });

        setTitle("Prj: " + MainActivity.mProyecto.titulo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proyecto, menu);


        return true;
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
            alert.setTitle("Agragar Etiqueta!");
            alert.setMessage("Escribe el nombre de la Etiqueta");

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

                        lista.add(new Elementos(inputName));

                        String cade = g.toJson(MainActivity.lista);
                        System.out.println(" 367 --------> " + cade);
                        saveSD("notas.txt", cade);

                        va.notifyDataSetChanged();
                        Intent i = new Intent(getBaseContext(), Proyecto.class);
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



    public  void saveSD(String file, String data)
    {
        if (checkEdoSd())
        {
            System.out.println("Save SD");
            try
            {
                File ruta_sd = Environment.getExternalStorageDirectory();


                //File f = new File(ruta_sd.getAbsolutePath(), "datos\\prueba.txt");
                File f = new File(getExternalFilesDir(null), file);

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

    class VersionAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
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
            iv.setBackgroundResource(R.drawable.check_boxes);
            tvTitle.setText(lista.get(pos).nombre);
            //tvDesc.setText(desc[pos]);

            return listItem;


        }
    }




}
