package io.github.gengkev.stocksapp;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DumpGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DumpGraphFragment extends Fragment {
    private static final String TAG = DumpGraphFragment.class.getName();
    private static final String ARG_TICKER = "ticker";
    private static final int MOVING_AVG_PERIOD = 10;

    private static final int AUTO_UPDATE_INTERVAL = 30;
    private static final String AUTO_UPDATE_PERIOD = "5m";
    private static final String INIT_UPDATE_PERIOD = "1d";

    private String mTicker;
    private LineChart mChart;
    private TextView mStatusView;

    private LineData mChartData;
    private TreeMap<Date, StockPrice> allStockPrices = new TreeMap<>();

    private LineDataSet mSetHigh;
    private LineDataSet mSetLow;
    private LineDataSet mSetClose;
    private LineDataSet mSetAvg;

    private ScheduledThreadPoolExecutor mExecutor;
    private Handler handler = new Handler(Looper.getMainLooper());


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ticker Ticker to display data for.
     * @return A new instance of fragment DumpDataFragment.
     */
    public static DumpGraphFragment newInstance(String ticker) {
        DumpGraphFragment fragment = new DumpGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TICKER, ticker);
        fragment.setArguments(args);
        return fragment;
    }

    public DumpGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTicker = getArguments().getString(ARG_TICKER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dump_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChart = (LineChart) view.findViewById(R.id.chart);
        mStatusView = (TextView) view.findViewById(R.id.textView);

        mExecutor = new ScheduledThreadPoolExecutor(1);
        mExecutor.schedule(
                new GetDataRunnable(INIT_UPDATE_PERIOD),
                0, TimeUnit.SECONDS);
        mExecutor.scheduleAtFixedRate(
                new GetDataRunnable(AUTO_UPDATE_PERIOD),
                0, AUTO_UPDATE_INTERVAL, TimeUnit.SECONDS);

        initChart();
    }


    private void initChart() {
        ArrayList<Entry> highPrices = new ArrayList<>();
        mSetHigh = new LineDataSet(highPrices, "High");
        mSetHigh.setAxisDependency(YAxis.AxisDependency.LEFT);
        mSetHigh.setColor(0xff00ff00);
        mSetHigh.setDrawCircles(false);

        ArrayList<Entry> lowPrices = new ArrayList<>();
        mSetLow = new LineDataSet(lowPrices, "Low");
        mSetLow.setAxisDependency(YAxis.AxisDependency.LEFT);
        mSetLow.setColor(0xffff0000);
        mSetLow.setDrawCircles(false);

        ArrayList<Entry> closePrices = new ArrayList<>();
        mSetClose = new LineDataSet(closePrices, "Close");
        mSetClose.setAxisDependency(YAxis.AxisDependency.LEFT);
        mSetClose.setColor(0xff00ffff);
        mSetClose.setDrawCircles(false);

        ArrayList<Entry> avgPrices = new ArrayList<>();
        mSetAvg = new LineDataSet(avgPrices, "Moving avg");
        mSetAvg.setAxisDependency(YAxis.AxisDependency.LEFT);
        mSetAvg.setColor(0xffffff00);
        mSetAvg.setDrawCircles(false);

        ArrayList<ILineDataSet> datasets = new ArrayList<>();
        datasets.add(mSetHigh);
        datasets.add(mSetLow);
        datasets.add(mSetClose);
        datasets.add(mSetAvg);

        ArrayList<String> xvals = new ArrayList<>();
        mChartData = new LineData(xvals, datasets);
        mChart.setData(mChartData);
    }

    private void updateChart() {
        mChartData.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private void recalcAverages() {
        mSetAvg.clear();

        Deque<Float> deque = new ArrayDeque<>();
        double sum = 0;
        int i = 0;
        for (Date date : allStockPrices.keySet()) {
            StockPrice sp = allStockPrices.get(date);
            deque.addLast(sp.close);
            sum += sp.close;
            if (i >= MOVING_AVG_PERIOD) {
                sum -= deque.removeFirst();
                mSetAvg.addEntry(new Entry((float) (sum / MOVING_AVG_PERIOD), i));
            }
            i++;
        }
    }

    private void pushNewData(ArrayList<StockPrice> stockPrices) {
        for (StockPrice sp : stockPrices) {
            if (allStockPrices.containsKey(sp.date)) {
                continue;
            }
            allStockPrices.put(sp.date, sp);
            mChartData.addXValue(sp.date.toString());

            mSetHigh.addEntry(new Entry(sp.high, mSetHigh.getEntryCount()));
            mSetLow.addEntry(new Entry(sp.low, mSetLow.getEntryCount()));
            mSetClose.addEntry(new Entry(sp.close, mSetClose.getEntryCount()));
        }
        recalcAverages();
    }

    private class GetDataRunnable implements Runnable {
        private String mPeriod;

        public GetDataRunnable(String period) {
            mPeriod = period;
        }

        @Override
        public void run() {
            ArrayList<StockPrice> stockPrices;
            try {
                stockPrices = GoogleStocksReader.getStockData(mTicker, mPeriod);
                pushNewData(stockPrices);
            } catch (final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStatusView.setText("NO EXCEPTINOSN j sdkl" + e);
                    }
                });
                return;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateChart();
                    mStatusView.setText("Last updated: " + new Date());
                }
            });
        }
    }

}
