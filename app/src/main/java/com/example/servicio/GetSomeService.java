package com.example.servicio;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetSomeService extends IntentService {
	//private final String urlClima = "http://api.openweathermap.org/data/2.5/weather?q=Quito&units=metric&lang=es";
	//private final String urlServicio = "http://www.thomas-bayer.com/sqlrest/PRODUCT/5";
	private final String urlServicio = "https://ingenieria.ute.edu.ec/enfoqueute/public/journals/1/html_v12n1/1390-6542-enfoqueute-12-01-00001.xml";

	public GetSomeService() {
		super("GetSomeService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			URL url = new URL(urlServicio);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true); 		
	        conn.connect();	        
	        Log.d("SERVICIO", conn.getResponseMessage());
	        
	        Scanner scanner  = new Scanner(conn.getInputStream());
	        StringBuffer clima = new StringBuffer();
	        while(scanner.hasNextLine()) {
	        	clima.append(scanner.nextLine());
	        }
	        scanner.close();
	        
	        notificar(clima.toString());
	        
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}

	private void notificar(String clima) {
		final int NOTIF_ID = 1;
		final int REQ_CODE = 2;

		Notification.Builder notifB =  new Notification.Builder(this, "IdPrueba");
		notifB.setSmallIcon(R.drawable.ic_stat_name);
		notifB.setContentTitle("Actualización!!!");
		notifB.setContentText("Hay nueva información del servicio");
		notifB.setAutoCancel(true);
		
		Intent intent = new Intent(this, Notificado.class);
		intent.putExtra("clima", clima);
		notifB.setContentIntent(PendingIntent.getActivity(this, REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT));
		
		Notification notif = notifB.build();
		
		NotificationManager notifMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifMgr.notify(NOTIF_ID, notif);		
	}

}
