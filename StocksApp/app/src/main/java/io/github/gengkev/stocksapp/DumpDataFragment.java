package io.github.gengkev.stocksapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DumpGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DumpDataFragment extends Fragment {
    private static final String ARG_TICKER = "ticker";
    private static final String ARG_PERIOD = "period";

    private String mTicker;
    private String mPeriod;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ticker Ticker to display data for.
     * @param period Period at which to retrieve data.
     * @return A new instance of fragment DumpDataFragment.
     */
    public static BlankFragment newInstance(String ticker, String period) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TICKER, ticker);
        args.putString(ARG_PERIOD, period);
        fragment.setArguments(args);
        return fragment;
    }

    public DumpDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTicker = args.getString(ARG_TICKER);
            mPeriod = args.getString(ARG_PERIOD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dump_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView textView = (TextView) view.findViewById(R.id.textView);

        // Get the data!
        new GetDataTask(new BetterAsyncTask.ResultListener<ArrayList<StockPrice>>() {
            @Override
            public void onSuccess(ArrayList<StockPrice> stockPrices) {
                StringBuilder out = new StringBuilder();
                out.append("Date,Open,Close\n");
                for (StockPrice sp : stockPrices) {
                    out.append(sp.date).append(',');
                    out.append(sp.open).append(',');
                    out.append(sp.close).append('\n');
                }
                textView.setText(out.toString());
            }

            @Override
            public void onError(Exception e) {
                textView.setText(getString(R.string.exception_dump, e));
            }
        }).execute(mTicker, mPeriod);
    }
}
