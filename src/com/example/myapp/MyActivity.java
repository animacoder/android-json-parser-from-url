package com.example.myapp;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class MyActivity extends ListActivity
{


    private ProgressDialog pDialog;


    private static String url = "http://bulut.polibis.com/mobil/musteriler/";


    private static final String TAG_MUSTERI = "musteri";
    private static final String TAG_AD = "ad";
    private static final String TAG_HESAP_KODU = "hesap_kodu";

    JSONArray m = null;


    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        contactList = new ArrayList<HashMap<String, String>>();

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyActivity.this);
            pDialog.setMessage("Lütfen Bekleyin...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();


            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);


            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    m = jsonObj.getJSONArray(TAG_MUSTERI);


                    for (int i = 0; i < m.length(); i++) {


                        JSONObject c = m.getJSONObject(i);
                        String as = c.getString(TAG_AD);
                        String no = c.getString(TAG_HESAP_KODU);


                        HashMap<String, String> contact = new HashMap<String, String>();


                        contact.put(TAG_AD, as);
                        contact.put(TAG_HESAP_KODU, no);


                        contactList.add(contact);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "URL'den veri alınamadı.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    MyActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_AD, TAG_HESAP_KODU }, new int[] { R.id.ad,
                    R.id.hesap_kodu });

            setListAdapter(adapter);
        }

    }

}





