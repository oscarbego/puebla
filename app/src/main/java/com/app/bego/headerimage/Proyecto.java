package com.app.bego.headerimage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Proyecto extends AppCompatActivity {

    Proyecto P;

    private TextView stickyView;
    //private SearchView stickyView1;
    private ListView listView;
    private View heroImageView;
    private View stickyViewSpacer;





    private ArrayList<Elementos> lista; // = new ArrayList<>();

    Gson g = new Gson();

    VersionAdapter va;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);



        lista = MainActivity.mProyecto.listaCotejo;

        listView = (ListView) findViewById(R.id.listViewP);
        heroImageView = findViewById(R.id.heroImageViewP);
        stickyView = (TextView) findViewById(R.id.stickyViewP);
        //stickyView1 = (SearchView) findViewById(R.id.stickyView1P);


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header2, null);

        stickyViewSpacer  = listHeader.findViewById(R.id.stickyViewPlaceholder);



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
                    stickyView.setY(Math.max(0, heroTopY + topY));

                    //heroTopY = stickyViewSpacer1.getTop();
                    //stickyView.setY(Math.max(70, heroTopY  + topY ));
                    //stickyView.setY(Math.max(70, heroTopY + topY));

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


            }
        });

        setTitle("Prj: " + MainActivity.mProyecto.titulo);

        P = this;
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
                //File f = new File(getExternalFilesDir(null), file);
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





    //final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


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
            final int pos = position;

            if (listItem == null) {
                listItem =  getLayoutInflater().inflate(R.layout.row3, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.img_miniatura);
            TextView tvTitle = (TextView) listItem.findViewById(R.id.proyecto);
            TextView tvDesc = (TextView) listItem.findViewById(R.id.etiqueta);
            Switch mySwitch = (Switch) listItem.findViewById(R.id.mySwitch);
            Button btnVerFoto = (Button) listItem.findViewById(R.id.btnVerFoto);


            btnVerFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Ver Foto");

                    //Intent intent = new Intent(Intent.ACTION_PICK,
                    //        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    //code = SELECT_PICTURE;
                    //startActivity(intent);
                    //startActivityForResult(intent, code);


                    // dialog
                    if (MainActivity.mProyecto.listaCotejo.get(pos).esta)
                    {
                        final AlertDialog.Builder imageDialog = new AlertDialog.Builder(P);
                        View layout = inflater.inflate(R.layout.fullimage_dialog,
                            (ViewGroup) findViewById(R.id.layout_root));
                        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);

                        //String strPath = "/mnt/sdcard/picture/"; //+arrData[position][2].toString();
                        String strPath = Environment.getExternalStorageDirectory() + "/AppPuebla/" + MainActivity.mProyecto.folder + "/"
                            + lista.get(pos).nombre + ".jpg";

                        Bitmap bm = BitmapFactory.decodeFile(strPath);
                        int width = 200;
                        int height = 200;
                        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bm, width, height, true);
                        image.setImageBitmap(resizedbitmap);

                        //imageDialog.setIcon(android.R.drawable.btn_star_big_on);

                        imageDialog.setTitle("Imagen : " + lista.get(pos).nombre + ".jpg");
                        imageDialog.setView(layout);
                        imageDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });

                        imageDialog.create();
                        imageDialog.show();
                    }
                    //------------------------------------

                }
            });

            mySwitch.setChecked(lista.get(pos).esta);

            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        String output = getExternalFilesDir(null).getAbsolutePath() + "/"
                                + lista.get(pos).nombre + ".jpg";


                        System.out.println(Environment.getExternalStorageDirectory() + "/AppPuebla/" + MainActivity.mProyecto.folder + "/"
                                + lista.get(pos).nombre + ".jpg");


                        output = Environment.getExternalStorageDirectory() + "/AppPuebla/" + MainActivity.mProyecto.folder + "/"
                                + lista.get(pos).nombre + ".jpg";

                        File file = new File(output);
                        Uri outputFileUri = Uri.fromFile(file);

                        System.out.println("Foto");
                        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        System.out.println("..........--> " + outputFileUri);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        intent.putExtra("return-data", true);
                        //startActivity(intent);
                        startActivityForResult(intent, code);

                        lista.get(pos).addFoto();

                    }
                    else{
                        System.out.println("No Foto");

                        lista.get(pos).delFoto();

                        String cade = g.toJson(MainActivity.lista);
                        saveSD("notas.txt", cade);
                    }
                }
            });




            // Set the views in the layout
            //iv.setBackgroundResource(R.drawable.check_boxes);
            tvTitle.setText("Nombre: " + lista.get(pos).nombre);
            //tvDesc.setText(desc[pos]);

            return listItem;


        }
    }



    Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    int code = TAKE_PICTURE;


    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;


    private String name = "";

    /**
     * Funci—n que se ejecuta cuando concluye el
     * intent en el que se solicita una imagen
     * ya sea de la c‡mara o de la galer’a
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String cade = g.toJson(MainActivity.lista);
        saveSD("notas.txt", cade);


        /**
         * Se revisa si la imagen viene de la c‡mara (TAKE_PICTURE)
         * o de la galer’a (SELECT_PICTURE)
         */
        if (requestCode == TAKE_PICTURE) {
            /**
             * Si se reciben datos en el intent tenemos una
             * vista previa (thumbnail)
             */
            if (data != null) {
                /**
                 * En el caso de una vista previa, obtenemos el
                 * extra ÒdataÓ del intent y
                 * lo mostramos en el ImageView
                 */
                if (data.hasExtra("data")) {
                    //ImageView iv = (ImageView)findViewById(R.id.imgView);
                    //iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
                /**
                 * De lo contrario es una imagen completa
                 */
            } else {
                /**
                 * A partir del nombre del archivo ya definido lo
                 * buscamos y creamos el bitmap
                 * para el ImageView
                 */
                ////ImageView iv = (ImageView)findViewById(R.id.imgView);
                ////iv.setImageBitmap(BitmapFactory.decodeFile(name));
                /**
                 * Para guardar la imagen en la galer’a, utilizamos
                 * una conexi—n a un MediaScanner
                 */
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    private MediaScannerConnection msc = null; {
                        msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
                    }
                    public void onMediaScannerConnected() {
                        msc.scanFile(name, null);
                    }
                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                    }
                };
            }
            /**
             * Recibimos el URI de la imagen y construimos un Bitmap
             * a partir de un stream de Bytes
             */
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ////ImageView iv = (ImageView)findViewById(R.id.imgView);
                ////iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {}
        }
    }


}
