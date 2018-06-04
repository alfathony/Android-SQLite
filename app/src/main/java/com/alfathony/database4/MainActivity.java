package com.alfathony.database4;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    public final static String ID_EXTRA="com.wilis.database4._ID"; Cursor model=null;

    AlmagAdapter adapter=null;
    EditText nama=null;
    EditText alamat=null;
    EditText hp=null;
    RadioGroup jekel=null;
    AlmagHelper helper=null;
    SharedPreferences prefs=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new AlmagHelper(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        nama = (EditText) findViewById(R.id.nama);
        alamat = (EditText) findViewById(R.id.alamat);
        hp = (EditText) findViewById(R.id.alamat);
        jekel = (RadioGroup) findViewById(R.id.jekel);
        initList();

        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override

    public void onDestroy() {
        super.onDestroy();

        helper.close();

    }
    @Override

    public void onListItemClick(ListView list, View view, int position, long id) {

        Intent i=new Intent(database4.this, DetailForm.class); i.putExtra(ID_EXTRA, String.valueOf(id)); startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        new MenuInflater(this).inflate(R.menu.option, menu); return(super.onCreateOptionsMenu(menu));
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add) {
            startActivity(new Intent(database4.this, DetailForm.class)); return(true);
        } else if (item.getItemId()==R.id.prefs) {
            startActivity(new Intent(this, EditTextPreference.class));
            return(true);
        }
        return(super.onOptionsItemSelected(item));

    }

    private void initList() {
        if (model!=null) {

            stopManagingCursor(model);

            model.close();
        }

        model=helper.getAll(prefs.getString("sort_order", "nama")); startManagingCursor(model);

        adapter=new AlmagAdapter(model);

        setListAdapter(adapter);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener=

            new SharedPreferences.OnSharedPreferenceChangeListener() { public void onSharedPreferenceChanged(SharedPreferences
                                                                                                                     sharedPrefs,String key) {

                if (key.equals("sort_order")) {

                    initList();
                }

            }

            };

    class AlmagAdapter extends CursorAdapter { AlmagAdapter(Cursor c) {
        super(database4.this, c);
    }

        @Override

        public void bindView(View row, Context ctxt, Cursor c) { AlmagHolder holder=(AlmagHolder)row.getTag(); holder.populateFrom(c, helper);
        }

        @Override

        public View newView(Context ctxt, Cursor c, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false); AlmagHolder holder=new AlmagHolder(row); row.setTag(holder);
            return(row);
        }
    }

    static class AlmagHolder {

        private TextView nama=null;
        private TextView alamat=null;

        private ImageView icon=null;

        private View row=null;
        AlmagHolder(View row) {

            this.row=row;

            nama=(TextView)row.findViewById(R.id.title); alamat=(TextView)row.findViewById(R.id.alamat); icon=(ImageView)row.findViewById(R.id.icon);
        }
        void populateFrom(Cursor c, AlmagHelper helper) { nama.setText(helper.getNama(c)); alamat.setText(helper.getAlamat(c));
            if (helper.getJekel(c).equals("Pria")) { icon.setImageResource(R.drawable.pria);
            }

            else if (helper.getJekel(c).equals("Perempuan")) { icon.setImageResource(R.drawable.perempuan);
            }
        }
    }



}
