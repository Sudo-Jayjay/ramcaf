package com.example.ramcaf;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchShortSeatStatuses extends AsyncTask {

    //After creating this class click the red bulb to implement methods then click ok

    @Override
    protected Object doInBackground(Object[] objects) {

        String result = null;


        try {
            //Call the URL here
            URL url = new URL (
                    "https://ramcaf.000webhostapp.com/ramcaf/getShortStatuses.php"
            );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
            //GET / POST are the only values here.
            //Since we're just getting data we will use GET
            //POST is use for submitting data like registration, etc
            connection.setRequestMethod ("GET");

            //FROM HERE we will read the printed data from the URL

            //Get the printed data from the URL
            InputStream inputStream = connection.getInputStream ();

            // Create input stream reader based on url connection input stream.
            InputStreamReader isReader = new InputStreamReader (inputStream);

            // Create buffered reader.
            BufferedReader bufReader = new BufferedReader (isReader);

            // Read line of text from server response.
            String line = bufReader.readLine ();

            // Save server response text.
            StringBuilder readTextBuf = new StringBuilder ();

            // Loop while return line is not null.
            while (line != null) {
                // Append the text to string buffer.
                readTextBuf.append (line);

                // Continue to read text line.
                line = bufReader.readLine ();
            }

            //Convert StringBuffer to String
            result = readTextBuf.toString ();

        } catch (Exception ex) {
            //log the error if occurs / happens for tracing purposes
            Log.d ("ERROR", ex.getMessage ());
        }

        //return the result
        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute (o);
    }

}

