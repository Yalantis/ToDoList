package com.yalantis.beamazingtoday.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galata on 20.07.16.
 */
public class ExampleActivity extends AppCompatActivity implements BatListener, OnItemClickListener, OnOutsideClickedListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ((TextView) findViewById(R.id.text_title)).setTypeface(TypefaceUtil.getAvenirTypeface(this));

        mRecyclerView = (BatRecyclerView) findViewById(R.id.bat_recycler_view);
        mAnimator = new BatItemAnimator();

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.getView().setAdapter(mAdapter = new BatAdapter(mGoals = new ArrayList<BatModel>() {{
            add(new Goal("first"));
            add(new Goal("second"));
            add(new Goal("third"));
            add(new Goal("fourth"));
            add(new Goal("fifth"));
            add(new Goal("sixth"));
            add(new Goal("seventh"));
            add(new Goal("eighth"));
            add(new Goal("ninth"));
            add(new Goal("tenth"));
        }}, this, mAnimator).setOnItemClickListener(this).setOnOutsideClickListener(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
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
        mGoals.add(0, new Goal(string));
        mAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int position) {
        mGoals.remove(position);
        mAdapter.notify(AnimationType.REMOVE, position);
    }

    @Override
    public void move(int from, int to) {
        if (from >= 0 && to >= 0) {
            mAnimator.setPosition(to);
            BatModel model = mGoals.get(from);
            mGoals.remove(model);
            mGoals.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);

            if (from == 0 || to == 0) {
                mRecyclerView.getView().scrollToPosition(Math.min(from, to));
            }
        }
    }

    @Override
    public void onClick(BatModel item, int position) {
        Toast.makeText(this, item.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }
}
