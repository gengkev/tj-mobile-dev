package io.github.gengkev.stocksapp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class GoogleStocksReader {
    public static final String TAG = GoogleStocksReader.class.getName();

    public static ArrayList<StockPrice> getStockData(String ticker, String period) throws IOException {
        Uri uri = Uri.parse("http://www.google.com/finance/getprices?").buildUpon()
                .appendQueryParameter("i", "60")
                .appendQueryParameter("p", period)
                .appendQueryParameter("f", "d,o,h,l,c,v")
                .appendQueryParameter("df", "cpct")
                .appendQueryParameter("q", ticker)
                .build();

        URL url = new URL(uri.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream is = conn.getInputStream();
        ArrayList<StockPrice> out = null;
        try {
            out = parseStream(is);
        } finally {
            is.close();
        }

        return out;
    }

    private static ArrayList<StockPrice> parseStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // hardcoded :(
        long interval = 60;

        String line = "";
        while (!line.startsWith("DATA")) {
            line = br.readLine();
        }

        String tzLine = br.readLine();
        Log.i(TAG, "Timezone info: " + tzLine);

        ArrayList<StockPrice> prices = new ArrayList<>();

        long lastDate = 0;
        while ((line = br.readLine()) != null) {
            String[] segments = line.split(",");

            StockPrice sp = new StockPrice();

            // read date
            if (segments[0].startsWith("a")) {
                lastDate = Long.parseLong(segments[0].substring(1));
                sp.date = new Date(1000 * lastDate);
            } else {
                int num = Integer.parseInt(segments[0]);
                sp.date = new Date(1000 * (lastDate + interval * num));
            }

            // hahaha
            sp.duration = interval;

            // read prices
            sp.close = Float.parseFloat(segments[1]);
            sp.high = Float.parseFloat(segments[2]);
            sp.low = Float.parseFloat(segments[3]);
            sp.open = Float.parseFloat(segments[4]);

            // read volume
            sp.volume = Integer.parseInt(segments[5]);

            prices.add(sp);
        }

        return prices;
    }

}
