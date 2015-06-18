package it.ep.qrsafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class StartActivity extends ActionBarActivity implements View.OnClickListener {

    private String LOG_TAG = "QRCard";

    private SharedPreferences prefs;

    private Button button;
    private EditText name;
    private EditText surname;
    private EditText organization;
    private EditText telephone;
    private EditText address;
    private EditText zipCode;
    private EditText city;
    private Spinner country;
    private EditText email;
    private EditText note;

    private String qrName;
    private String qrSurname;
    private String qrOrganization;
    private String qrTelephone;
    private String qrAddress;
    private String qrZipCode;
    private String qrCity;
    private String qrCountry;
    private String qrEmail;
    private String qrNote;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        name = (EditText) findViewById(R.id.EditTextName);
        surname = (EditText) findViewById(R.id.EditTextSurname);
        organization = (EditText) findViewById(R.id.EditTextOrganization);
        telephone = (EditText) findViewById(R.id.EditTextTelephone);
        address = (EditText) findViewById(R.id.EditTextAddress);
        zipCode = (EditText) findViewById(R.id.EditTextZipCode);
        city = (EditText) findViewById(R.id.EditTextCity);
        country = (Spinner) findViewById(R.id.SpinnerCountry);
        email = (EditText) findViewById(R.id.EditTextEmail);
        note = (EditText) findViewById(R.id.EditTextNote);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String sCountry;
        for (Locale loc : locale) {
            sCountry = loc.getDisplayCountry();
            if (sCountry.length() > 0 && !countries.contains(sCountry)) {
                countries.add(sCountry);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        country.setAdapter(adapter);
        country.setSelection(adapter.getPosition(Locale.getDefault().getDisplayCountry()));

        // load prefs
        prefs = this.getSharedPreferences("it.ep.qrsafe", Context.MODE_PRIVATE);

        qrName = prefs.getString("it.ep.qrsafe.qrName", null);
        name.setText(qrName, TextView.BufferType.EDITABLE);

        qrSurname = prefs.getString("it.ep.qrsafe.qrSurname", null);
        surname.setText(qrSurname, TextView.BufferType.EDITABLE);

        qrOrganization = prefs.getString("it.ep.qrsafe.qrOrganization", null);
        organization.setText(qrOrganization, TextView.BufferType.EDITABLE);

        qrTelephone = prefs.getString("it.ep.qrsafe.qrTelephone", null);
        telephone.setText(qrTelephone, TextView.BufferType.EDITABLE);

        qrAddress = prefs.getString("it.ep.qrsafe.qrAddress", null);
        address.setText(qrAddress, TextView.BufferType.EDITABLE);

        qrZipCode = prefs.getString("it.ep.qrsafe.qrZipCode", null);
        zipCode.setText(qrZipCode, TextView.BufferType.EDITABLE);

        qrCity = prefs.getString("it.ep.qrsafe.qrCity", null);
        city.setText(qrCity, TextView.BufferType.EDITABLE);

        qrCountry = prefs.getString("it.ep.qrsafe.qrCountry", null);
        if (qrCountry != null && !qrCountry.isEmpty()) {
            country.setSelection(adapter.getPosition(qrCountry));
        }

        qrEmail = prefs.getString("it.ep.qrsafe.qrEmail", null);
        email.setText(qrEmail, TextView.BufferType.EDITABLE);

        qrNote = prefs.getString("it.ep.qrsafe.qrNote", null);
        note.setText(qrNote, TextView.BufferType.EDITABLE);

        button = (Button) findViewById(R.id.CreateQRCode);
        button.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_about));
            alertDialog.setMessage(getString(R.string.about_qrsafe));
            alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.CreateQRCode:
                Intent intent = new Intent(this, ShowQRCodeActivity.class);

                name = (EditText) findViewById(R.id.EditTextName);
                surname = (EditText) findViewById(R.id.EditTextSurname);
                organization = (EditText) findViewById(R.id.EditTextOrganization);
                telephone = (EditText) findViewById(R.id.EditTextTelephone);
                address = (EditText) findViewById(R.id.EditTextAddress);
                zipCode = (EditText) findViewById(R.id.EditTextZipCode);
                city = (EditText) findViewById(R.id.EditTextCity);
                country = (Spinner) findViewById(R.id.SpinnerCountry);
                email = (EditText) findViewById(R.id.EditTextEmail);
                note = (EditText) findViewById(R.id.EditTextNote);

                qrName = name.getText().toString();
                qrSurname = surname.getText().toString();
                qrOrganization = organization.getText().toString();
                qrTelephone = telephone.getText().toString();
                qrAddress = address.getText().toString();
                qrZipCode = zipCode.getText().toString();
                qrCity = city.getText().toString();
                qrCountry = country.getSelectedItem().toString();
                qrEmail = email.getText().toString();
                qrNote = note.getText().toString();

                String simpleAddress = qrAddress;

                // check data

                // name mandatory
                if (qrName == null || qrName.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle(getString(R.string.warning));
                    alertDialog.setMessage(getString(R.string.name_mandatory));
                    alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                    return;
                }

                // surname mandatory
                if (qrSurname == null || qrSurname.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle(getString(R.string.warning));
                    alertDialog.setMessage(getString(R.string.surname_mandatory));
                    alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                    return;
                }

                // email validation
                if (qrEmail != null && !qrEmail.isEmpty()) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(qrEmail).matches()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle(getString(R.string.warning));
                        alertDialog.setMessage(getString(R.string.email_not_valid));
                        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                        return;
                    }
                }


                //Find screen size
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;

                Bundle b = new Bundle();

                b.putString(ContactsContract.Intents.Insert.NAME, qrName + " " + qrSurname);

                if (qrOrganization != null && !qrOrganization.isEmpty()) {
                    b.putString(Contents.Type.ORG, qrOrganization);
                } else {
                    qrOrganization = "";
                }

                if (qrTelephone != null && !qrTelephone.isEmpty()) {
                    b.putString(Contents.PHONE_KEYS[0], qrTelephone);
                } else {
                    qrTelephone = "";
                }

                if (qrAddress != null && !qrAddress.isEmpty()) {
                    b.putString(ContactsContract.Intents.Insert.POSTAL, qrAddress);
                }

                if (qrZipCode != null && !qrZipCode.isEmpty()) {
                    String tmpAdr = b.getString(ContactsContract.Intents.Insert.POSTAL);
                    tmpAdr = tmpAdr == null || tmpAdr.isEmpty() ? qrZipCode : tmpAdr + "\n" + qrZipCode;
                    qrAddress = tmpAdr;
                    b.putString(ContactsContract.Intents.Insert.POSTAL, qrAddress);
                }

                if (qrCity != null && !qrCity.isEmpty()) {
                    String tmpAdr = b.getString(ContactsContract.Intents.Insert.POSTAL);
                    tmpAdr = tmpAdr == null || tmpAdr.isEmpty() ? qrCity : tmpAdr + "\n" + qrCity;
                    qrAddress = tmpAdr;
                    b.putString(ContactsContract.Intents.Insert.POSTAL, qrAddress);
                }

                if (qrCountry != null && !qrCountry.isEmpty()) {
                    String tmpAdr = b.getString(ContactsContract.Intents.Insert.POSTAL);
                    tmpAdr = tmpAdr == null || tmpAdr.isEmpty() ? qrCountry : tmpAdr + "\n" + qrCountry;
                    qrAddress = tmpAdr;
                    b.putString(ContactsContract.Intents.Insert.POSTAL, qrAddress);
                }

                if (qrEmail != null && !qrEmail.isEmpty()) {
                    b.putString(Contents.EMAIL_KEYS[0], qrEmail);
                } else {
                    qrEmail = "";
                }


                if (qrNote != null && !qrNote.isEmpty()) {
                    b.putString(Contents.NOTE_KEY, qrNote);
                } else {
                    qrNote = "";
                }


                // Go to show qr code screen
                StringBuffer sb = new StringBuffer();
                sb.append(qrName);
                sb.append("\n");
                sb.append(qrSurname);
                sb.append("\n");
                sb.append(qrOrganization);
                sb.append("\n");
                sb.append(qrTelephone);
                sb.append("\n");
                sb.append(qrAddress);
                sb.append("\n");
                sb.append(qrEmail);
                sb.append("\n");
                sb.append(qrNote);
                sb.append("\n");

                intent.putExtra("qrCode", sb.toString());

                startActivity(intent);

                //Encode with a QR Code image
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(getContentResolver(),
                        sb.toString(),
                        b,
                        Contents.Type.CONTACT,
                        BarcodeFormat.QR_CODE.toString(),
                        smallerDimension);

                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    FileOutputStream out = null;
                    try {
                        out = openFileOutput("qrcode.png", Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.close();
                        Log.v(LOG_TAG, "QRCode Saved");
                    } catch (Exception e) {
                        // handle exception
                        Log.v(LOG_TAG, "Error while saving QRCode: " + e.getMessage());
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                // Save prefs
                prefs = this.getSharedPreferences("it.ep.qrsafe", Context.MODE_PRIVATE);
                prefs.edit().putString("it.ep.qrsafe.qrName", qrName).apply();
                prefs.edit().putString("it.ep.qrsafe.qrSurname", qrSurname).apply();
                prefs.edit().putString("it.ep.qrsafe.qrOrganization", qrOrganization).apply();
                prefs.edit().putString("it.ep.qrsafe.qrTelephone", qrTelephone).apply();
                prefs.edit().putString("it.ep.qrsafe.qrAddress", simpleAddress).apply();
                prefs.edit().putString("it.ep.qrsafe.qrZipCode", qrZipCode).apply();
                prefs.edit().putString("it.ep.qrsafe.qrCity", qrCity).apply();
                prefs.edit().putString("it.ep.qrsafe.qrCountry", qrCountry).apply();
                prefs.edit().putString("it.ep.qrsafe.qrEmail", qrEmail).apply();
                prefs.edit().putString("it.ep.qrsafe.qrNote", qrNote).apply();

                break;


        }
    }

}
