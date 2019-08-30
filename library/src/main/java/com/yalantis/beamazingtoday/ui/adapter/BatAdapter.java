package com.yalantis.beamazingtoday.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.MoveAnimationListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.util.List;

/**
 * Created by galata on 15.07.16.
 */
public class BatAdapter extends RecyclerView.Adapter<BatAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener, MoveAnimationListener {

    private List<BatModel> mItems;
    private BatListener mListener;
    private BatItemAnimator mAnimator;
    private boolean mIsBusy;

    @DrawableRes
    private int mRadioButtonRes = R.drawable.selector_radio_button;
    @ColorRes
    private int mDividerColor = R.color.colorDivider;
    @ColorRes
    private int mTextColor = R.color.colorTexts;
    private boolean mIsDividerVisible = true;
    private Typeface mTypeface;
    private OnItemClickListener mItemClickListener;
    private OnOutsideClickedListener mOnOutsideClickedListener;

    public BatAdapter(List<BatModel> goals, @Nullable BatListener listener, @Nullable BatItemAnimator animator) {
        mItems = goals;
        mListener = listener;
        mAnimator = animator;

        if (mAnimator != null) {
            mAnimator.setListener(this);
        }
    }

    public BatAdapter(List<BatModel> goals, @Nullable BatListener listener) {
        mItems = goals;
        mListener = listener;
    }

    public BatAdapter setRadioButtonColor(@DrawableRes int drawable) {
        mRadioButtonRes = drawable;
        return this;
    }

    public BatAdapter setDividerColor(@ColorRes int color) {
        mDividerColor = color;
        mIsDividerVisible = true;
        return this;
    }

    public BatAdapter setOnOutsideClickListener(OnOutsideClickedListener listener) {
        mOnOutsideClickedListener = listener;
        return this;
    }

    public BatAdapter setTextColor(@ColorRes int color) {
        mTextColor = color;
        return this;
    }

    public BatAdapter setTypeface(Typeface typeface) {
        mTypeface = typeface;
        return this;
    }

    public BatAdapter setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
        return this;
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
        holder.radioButton.setBackgroundResource(mRadioButtonRes);
        holder.divider.setBackgroundResource(mDividerColor);
        holder.textView.setTextColor(getColor(holder.rootView.getContext(), mTextColor));
        holder.textView.setTypeface(mTypeface != null ? mTypeface : TypefaceUtil.getTypeface(holder.rootView.getContext()));
        holder.divider.setVisibility(mIsDividerVisible ? View.VISIBLE : View.GONE);
    }

    private int getColor(Context context, @ColorRes int color) {
        return ContextCompat.getColor(context, color);
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

        if (mAnimator != null) {
            mAnimator.setAnimationType(animationType);
        }
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


        public View rootView;
        TextView textView;
        AppCompatCheckBox radioButton;
        View divider;
        View clickeableView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView.findViewById(R.id.root);
            this.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem();
                }
            });
            this.textView = itemView.findViewById(R.id.text_view);
            this.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem();
                }
            });
            this.radioButton = itemView.findViewById(R.id.radio_button);
            this.divider = itemView.findViewById(R.id.divider);
            this.clickeableView = itemView.findViewById(R.id.clickable_view);
            this.clickeableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheck();
                }
            });
        }


        void onCheck() {
            if (!mIsBusy) {
                radioButton.toggle();
            }
        }

        void onClickItem() {
            if (mItemClickListener != null) {
                BatModel item = (BatModel) radioButton.getTag();
                mItemClickListener.onClick(item, mItems.indexOf(item));
            }
        }

        //@OnClick(R2.id.full_list_item)
        void onOutsideClicked() {
            if (mOnOutsideClickedListener != null) {
                mOnOutsideClickedListener.onOutsideClicked();
            }
        }

        public int getItemPosition() {
            return mItems.indexOf(radioButton.getTag());
        }
    }
}
