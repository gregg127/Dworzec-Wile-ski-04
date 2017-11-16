package com.example.grzesiek.oktorejodjazd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzesiek on 2017-11-15.
 */

public class AboutActivity extends AppCompatActivity {

    private TextView textView;
    private ListView listView;
    private IconAdapter adapter;
    //private List<String> listItems;
    String[] items = {"Kontakt","GitHub"};
    int[] icons = {R.mipmap.icon_email, R.mipmap.github_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_lay);
        init();
    }

    private void init(){
        textView = (TextView)findViewById(R.id.aboutTextView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.app_info)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        listView = (ListView)findViewById(R.id.aboutListView);
        listView.setAdapter( new IconAdapter(this, items, icons));
        listView.setOnItemClickListener((par, view, pos, id) -> {
            if(items[pos].equals("Kontakt")){
                Log.i("Send email","");
                String[] address = {"grzegorz.golebiowski127@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "O ktorej odjazd - feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            } else if(items[pos].equals("GitHub")){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gregg127"));
                startActivity(browserIntent);
            }
        } );
    }

    class IconAdapter extends BaseAdapter {
       private Context con;
       private String[] title;
       private int[] imgs;

        public IconAdapter(Context context, String[] text1, int[] imageIds) {
            con = context;
            title = text1;
            imgs = imageIds;
        }
        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.about_list_row_lay, parent, false);
            TextView txt = (TextView) row.findViewById(R.id.txtTitle);
            ImageView img = (ImageView) row.findViewById(R.id.imgIcon);
            txt.setText(title[position]);
            img.setImageResource(imgs[position]);
            return row;
        }

    }
}
