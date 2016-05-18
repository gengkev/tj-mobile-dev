package io.github.gengkev.stocksapp;

import java.io.IOException;
import java.util.ArrayList;

public class GetDataTask extends BetterAsyncTask<String, Void, ArrayList<StockPrice>> {
    public GetDataTask(ResultListener<ArrayList<StockPrice>> resultListener) {
        super(resultListener);
    }

    @Override
    protected ArrayList<StockPrice> doInBackground(String... params) {
        if (params.length != 2) {
            setException(new IllegalArgumentException());
            return null;
        }

        String ticker = params[0];
        String period = params[1];
        try {
            return GoogleStocksReader.getStockData(ticker, period);
        } catch (IOException e) {
            setException(e);
            return null;
        }
    }
}
