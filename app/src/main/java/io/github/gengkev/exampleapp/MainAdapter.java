package io.github.gengkev.exampleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ClickListener mClickListener;
    private String[] mPrimaryText;
    private String[] mSecondaryText;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public TextView mPrimaryView;
        public TextView mSecondaryView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
            mPrimaryView = (TextView) v.findViewById(android.R.id.text1);
            mSecondaryView = (TextView) v.findViewById(android.R.id.text2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter(String[] primaryText, String[] secondaryText) {
        mPrimaryText = primaryText;
        mSecondaryText = secondaryText;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mPrimaryView.setText(mPrimaryText[position]);
        holder.mSecondaryView.setText(mSecondaryText[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(position, v);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Math.min(mPrimaryText.length, mSecondaryText.length);
    }

    public void setClickListener(ClickListener listener) {
        mClickListener = listener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}