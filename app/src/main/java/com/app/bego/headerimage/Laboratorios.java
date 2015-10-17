package com.app.bego.headerimage;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;


import java.util.ArrayList;

public class Laboratorios extends AppCompatActivity {



    VersionAdapter va;

    LayoutInflater inflater;

    Gson g = new Gson();

    private ListView listView;
    private TextView stickyView;
    private View heroImageView;
    private View stickyViewSpacer;

    private ArrayList<String> labs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto2);

        labs.add("Química");
        labs.add("Biología");
        labs.add("Física");

        labs.add("Telesecundaria");
        labs.add("Secundarias Tecnicas");
        labs.add("Secundarias Generales");



        listView = (ListView) findViewById(R.id.listViewP);

        heroImageView = findViewById(R.id.heroImageViewP);
        stickyView = (TextView) findViewById(R.id.stickyViewP);


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header3, null);

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

                if(position == 1)
                    i.putExtra("lab","Q");

                if(position == 2)
                    i.putExtra("lab","B");

                if(position == 3)
                    i.putExtra("lab","F");

                if(position == 4)
                    i.putExtra("lab","TL");

                if(position == 5)
                    i.putExtra("lab","ST");

                if(position == 6)
                    i.putExtra("lab","SG");


                startActivity(i);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_laboratorios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
            return labs.size();
        }

        @Override
        public Object getItem(int position) {
            if(position < labs.size())
                return labs.get(position);

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
                listItem =  getLayoutInflater().inflate(R.layout.row4, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.img_miniatura);
            TextView tvTitle = (TextView) listItem.findViewById(R.id.proyecto);
            TextView tvDesc = (TextView) listItem.findViewById(R.id.etiqueta);


            tvTitle.setText(labs.get(pos));

            return listItem;


        }
    }

}
