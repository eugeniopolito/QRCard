package it.ep.qrsafe;

import android.app.AlertDialog;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Eugenio Polito
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class ShowQRCodeActivity extends ActionBarActivity {

    private String LOG_TAG = "QRCard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        try {
            FileInputStream fin = openFileInput("qrcode.png");
            if (fin != null) {
                Drawable d = Drawable.createFromStream(fin, "qrcode.png");
                ImageView myImage = (ImageView) findViewById(R.id.imageView1);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // About...
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

        // Share QR Code
        if (id == R.id.share_qrcode) {
            View content = findViewById(R.id.imageView1);
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
                Log.v(LOG_TAG, "Cannot share image");
            }


        }

        return super.onOptionsItemSelected(item);
    }
}
