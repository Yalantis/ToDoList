package com.yalantis.beamazingtoday.ui.adapter;

import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.R2;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.MoveAnimationListener;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by galata on 15.07.16.
 */
public class BatAdapter extends RecyclerView.Adapter<BatAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener, MoveAnimationListener {

    private List<BatModel> mItems;
    private BatListener mListener;
    private BatItemAnimator mAnimator;
    private boolean mIsBusy;

    @ColorRes private int mRadioButtonColor;
    @ColorRes private int mDividerColor;
    private boolean mIsDividerVisible;


    public BatAdapter(List<BatModel> goals, BatListener listener, BatItemAnimator animator) {
        mItems = goals;
        mListener = listener;
        mAnimator = animator;
        mAnimator.setListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BatModel model = mItems.get(position);
        holder.textView.setText(model.getText());
        setChecked(holder.radioButton, model.isChecked());
        holder.radioButton.setTag(model);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!mIsBusy && mListener != null) {
            BatModel model = (BatModel) buttonView.getTag();
            mListener.move(mItems.indexOf(model), isChecked ? mItems.size() - 1 : 0);
            model.setChecked(isChecked);
        } else {
            setChecked(buttonView, !isChecked);
        }
    }

    private void setChecked(CompoundButton button, boolean checked) {
        button.setOnCheckedChangeListener(null);
        button.setChecked(checked);
        button.setOnCheckedChangeListener(this);
    }

    public void notify(@AnimationType int animationType, int position) {
        notify(animationType, position, -1);
    }

    public void notify(@AnimationType int animationType, int from, int to) {
        switch (animationType) {

            case AnimationType.ADD:
                notifyItemInserted(from);
                break;
            case AnimationType.MOVE:
                notifyItemMoved(from, to);
                break;
            case AnimationType.REMOVE:
                notifyItemRemoved(from);
                break;
        }

        mAnimator.setAnimationType(animationType);
    }

    @Override
    public void onAnimationStarted() {
        mIsBusy = true;
    }

    @Override
    public void onAnimationFinished() {
        mIsBusy = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.root)
        public View rootView;
        @BindView(R2.id.text_view)
        TextView textView;
        @BindView(R2.id.radio_button)
        CheckBox radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.clickable_view)
        void onCheck() {
            if (!mIsBusy) {
                radioButton.toggle();
            }
        }

        public int getItemPosition() {
            return mItems.indexOf((BatModel) radioButton.getTag());
        }
    }
}
