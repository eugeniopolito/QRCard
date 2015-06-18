package it.ep.qrsafe;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private String LOG_TAG = "QRCard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FileInputStream fin = openFileInput("qrcode.png");
            if (fin != null) {
                Drawable d = Drawable.createFromStream(fin, "qrcode.png");
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageDrawable(d);
                fin.close();
                Log.v(LOG_TAG, "QRCode successfully loaded");
            }
        } catch (FileNotFoundException e) {
            Log.v(LOG_TAG, "QRCode not exists");
        } catch (IOException ioe) {
            Log.v(LOG_TAG, "Error in closing file");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Edit
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }

        // Scan
        if (id == R.id.action_scan) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt(getString(R.string.qr_prompt));
            integrator.initiateScan();
        }

        // About...
        if (id == R.id.action_about) {
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

        // Share QR Code
        if (id == R.id.action_share_qrcode) {
            View content = findViewById(R.id.imageView);
            content.setDrawingCacheEnabled(true);

            Bitmap bitmap = content.getDrawingCache();

            try {
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/qrcode.jpg");
                FileOutputStream ostream = openFileOutput("qrcode.jpg", Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                startActivity(Intent.createChooser(share, getString(R.string.share_qrcode)));

                if (cachePath != null) {
                    cachePath.deleteOnExit();
                }
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getString(R.string.warning));
                alertDialog.setMessage(getString(R.string.qr_code_missing));
                alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                Log.v(LOG_TAG, "Cannot share image");
            }


        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String vcard = scanResult.getContents();
            if (vcard != null && !vcard.isEmpty() && vcard.startsWith(Contents.Type.CARD_TYPE)) {
                final Contact c = Utils.parseVcard(vcard);
                Log.v(LOG_TAG, c.toString());
                DialogFragment newFragment = CreateContactDialog.newInstance(getApplicationContext(), c,
                        R.string.add_contact);
                newFragment.show(getFragmentManager(), "dialog");
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.qr_code_wrong), Toast.LENGTH_LONG).show();
            }
        }
    }
}
