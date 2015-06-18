package it.ep.qrsafe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by eugenio on 01/06/15.
 */
public class QRSafeWidgetProvider extends AppWidgetProvider {

    private String LOG_TAG = "QRCard";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,
                QRSafeWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            FileInputStream fis = null;
            try {
                fis = context.openFileInput("qrcode.png");

                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                remoteViews.setImageViewBitmap(R.id.imageView1, bitmap);

                Intent intent = new Intent(context, MainActivity.class);

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
                fis.close();
            } catch (FileNotFoundException e) {
                Log.v(LOG_TAG, "File not found");
            } catch (IOException ioe) {
                Log.v(LOG_TAG, "Error in closing file");
            }
        }
    }
}

