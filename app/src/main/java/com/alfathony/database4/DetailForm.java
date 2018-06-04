package com.alfathony.database4;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends AppCompatActivity {

    EditText nama = null;
    EditText alamat = null;
    EditText hp = null;
    RadioGroup jekel = null;
    AlmagHelper almagId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new AlmagHelper((this));

        nama = (EditText) findViewById(R.id.nama);
        alamat = (EditText) findViewById(R.id.alamat);
        hp = (EditText) findViewById(R.id.hp);
        jekel = (RadioGroup) findViewById(R.id.jekel);
        Button save = (Button) findViewById(R.id.save);

        save.setOnClickListener(onSave);

        almagId = getIntent().getStringExtra(database4.ID_EXTRA);

        if (almagId != null) {
            load();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        helper.close();
    }

    private void load() {
        Cursor c = helper.getByID(almagId);

        c.moveToFirst();
        nama.setText(helper.getNama(c));
        alamat.setText(helper.getAlamat(c));
        hp.setText(helper.getHp(c));

        if (helper.getJekel(c).equals("Perempuan")) {
            jekel.check(R.id.perempuan);
        }

        c.close();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            String type = null;

            switch (jekel.getCheckedRadioButtonId()) {
                case R.id.pria:
                    type = "Pria";
                    break;
                case R.id.perempuan:
                    type = "Perempuan";
                    break;
            }

            if (almagId == null) {
                helper.insert(nama.getText().toString(), alamat.getText().toString(), type,
                        hp.getText().toString());
            } else {
                helper.update(almagId, nama.getText().toString(), alamat.getText().toString(), type,
                        hp.getText().toString());

            }

            finish();
        }
    };
}

