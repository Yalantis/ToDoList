package com.yalantis.beamazingtoday.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yalantis.beamazingtoday.listeners.AddItemListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galata on 20.07.16.
 */
public class ExampleActivity extends Activity implements AddItemListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<String> mGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        mRecyclerView = (BatRecyclerView) findViewById(R.id.bat_recycler_view);

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.getView().setAdapter(mAdapter = new BatAdapter(mGoals = new ArrayList<String>() {{
            add("first");
            add("second");
            add("third");
            add("fourth");
            add("fifth");
            add("sixth");
            add("seventh");
            add("eighth");
            add("ninth");
            add("tenth");
        }}));

        mRecyclerView.getView().setItemAnimator(new BatItemAnimator());
        mRecyclerView.setAddItemListener(this);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });
    }

    @Override
    public void add(String string) {
        mGoals.add(0, string);
        mAdapter.notifyItemInserted(0);
    }
}
