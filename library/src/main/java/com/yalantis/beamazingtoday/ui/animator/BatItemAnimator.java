package com.yalantis.beamazingtoday.ui.animator;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.yalantis.beamazingtoday.Constant;
import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.listeners.MoveAnimationListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galata on 28.06.16.
 */
public class BatItemAnimator extends SimpleItemAnimator {
    private static final long ANIM_DURATION = 250;
    private List<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
    private List<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
    private List<MoveInfo> mPendingMoves = new ArrayList<>();
    private List<ChangeInfo> mPendingChanges = new ArrayList<>();
    private List<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<>();
    private List<ArrayList<MoveInfo>> mMovesList = new ArrayList<>();
    private List<ArrayList<ChangeInfo>> mChangesList = new ArrayList<>();
    private List<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
    private List<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
    private List<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
    private List<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();

    private int mPosition = -1;
    @AnimationType
    private int mAnimationType;
    private MoveAnimationListener mListener;

    public void setListener(MoveAnimationListener listener) {
        mListener = listener;
    }

    private void animateAddImpl(final BatAdapter.ViewHolder holder) {
        final View view = holder.itemView;
        mAddAnimations.add(holder);
        view.setAlpha(0);

        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        animation.setDuration(ANIM_DURATION).
                setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        ViewCompat.setAlpha(view, 1);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        view.setAlpha(1);
                        animation.setListener(null);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                        mListener.onAnimationFinished();
                    }
                }).start();
    }

    private void animateMoveImpl(final BatAdapter.ViewHolder holder) {
        final View view = holder.itemView;
        final boolean isMainView = isMainListItem(holder.getItemPosition());

        if (isMainView) {
            holder.rootView.setBackgroundResource(R.drawable.header_background_rounded);
            mListener.onAnimationStarted();
        }

        ViewCompat.animate(view).scaleX(isMainView ? 1.05f : 1).scaleY(isMainView ? 1.05f : 1)
                .setDuration(mAnimationType == AnimationType.MOVE ? Constant.ANIM_DURATION_MILLIS : 0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ViewCompat.animate(view).translationX(0).translationY(0).setDuration(Constant.ANIM_DURATION_MILLIS)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewCompat.animate(view).scaleX(1).scaleY(1).start();
                                        if (isMainListItem(holder.getItemPosition())) {
                                            holder.rootView.setBackgroundResource(R.drawable.list_item_background);
                                            mPosition = -1;
                                        }
                                        dispatchMoveFinished(holder);
                                        mMoveAnimations.remove(holder);
                                        dispatchFinishedWhenDone();
                                        mListener.onAnimationFinished();
                                    }
                                });
                    }
                });
    }

    private boolean isMainListItem(int itemPosition) {
        return mPosition == itemPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setAnimationType(@AnimationType int animationType) {
        mAnimationType = animationType;
    }

    @Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean movesPending = !mPendingMoves.isEmpty();
        boolean changesPending = !mPendingChanges.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        if (!removalsPending && !movesPending && !additionsPending && !changesPending) {
            // nothing to animate
            return;
        }
        mPendingRemovals.clear();
        // Next, move stuff
        if (movesPending) {
            final ArrayList<MoveInfo> moves = new ArrayList<>();
            moves.addAll(mPendingMoves);
            mMovesList.add(moves);
            mPendingMoves.clear();
            Runnable mover = new Runnable() {
                @Override
                public void run() {
                    for (MoveInfo moveInfo : moves) {
                        animateMoveImpl((BatAdapter.ViewHolder) moveInfo.holder);
                    }
                    moves.clear();
                    mMovesList.remove(moves);
                }
            };
            if (removalsPending) {
                View view = moves.get(0).holder.itemView;
                ViewCompat.postOnAnimationDelayed(view, mover, getRemoveDuration());
            } else {
                mover.run();
            }
        }
        // Next, change stuff, to run in parallel with move animations
        if (changesPending) {
            final ArrayList<ChangeInfo> changes = new ArrayList<>();
            changes.addAll(mPendingChanges);
            mChangesList.add(changes);
            mPendingChanges.clear();
            Runnable changer = new Runnable() {
                @Override
                public void run() {
                    for (ChangeInfo change : changes) {
                        animateChangeImpl(change);
                    }
                    changes.clear();
                    mChangesList.remove(changes);
                }
            };
            if (removalsPending) {
                RecyclerView.ViewHolder holder = changes.get(0).oldHolder;
                ViewCompat.postOnAnimationDelayed(holder.itemView, changer, getRemoveDuration());
            } else {
                changer.run();
            }
        }
        // Next, add stuff
        if (additionsPending) {
            final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList<>();
            additions.addAll(mPendingAdditions);
            mAdditionsList.add(additions);
            mPendingAdditions.clear();
            Runnable adder = new Runnable() {
                public void run() {
                    for (RecyclerView.ViewHolder holder : additions) {
                        animateAddImpl((BatAdapter.ViewHolder) holder);
                    }
                    additions.clear();
                    mAdditionsList.remove(additions);
                }
            };
            if (removalsPending || movesPending || changesPending) {
                long removeDuration = removalsPending ? getRemoveDuration() : 0;
                long moveDuration = movesPending ? getMoveDuration() : 0;
                long changeDuration = changesPending ? getChangeDuration() : 0;
                long totalDelay = removeDuration + Math.max(moveDuration, changeDuration);
                View view = additions.get(0).itemView;
                ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
            } else {
                adder.run();
            }
        }
    }

    @Override
    public boolean animateRemove(final RecyclerView.ViewHolder holder) {
        endAnimation(holder);
        mPendingRemovals.add(holder);
        return true;
    }

    @Override
    public boolean animateAdd(final RecyclerView.ViewHolder holder) {
        endAnimation(holder);
        ViewCompat.setAlpha(holder.itemView, 0);

        mPendingAdditions.add(holder);
        return true;
    }

    @Override
    public boolean animateMove(final RecyclerView.ViewHolder holder, int fromX, int fromY,
                               int toX, int toY) {
        final View view = holder.itemView;
        fromX += ViewCompat.getTranslationX(holder.itemView);
        fromY += ViewCompat.getTranslationY(holder.itemView);
        endAnimation(holder);
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder);
            return false;
        }
        if (deltaX != 0) {
            ViewCompat.setTranslationX(view, -deltaX);
        }
        if (deltaY != 0) {
            ViewCompat.setTranslationY(view, -deltaY);
        }
        mPendingMoves.add(new MoveInfo(holder, fromX, fromY, toX, toY));
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                                 int fromX, int fromY, int toX, int toY) {
        final float prevTranslationX = ViewCompat.getTranslationX(oldHolder.itemView);
        final float prevTranslationY = ViewCompat.getTranslationY(oldHolder.itemView);
        final float prevAlpha = ViewCompat.getAlpha(oldHolder.itemView);
        endAnimation(oldHolder);
        int deltaX = (int) (toX - fromX - prevTranslationX);
        int deltaY = (int) (toY - fromY - prevTranslationY);
        // recover prev translation state after ending animation
        ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
        ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);
        ViewCompat.setAlpha(oldHolder.itemView, prevAlpha);
        if (newHolder != null && newHolder.itemView != null) {
            // carry over translation values
            endAnimation(newHolder);
            ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
            ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
            ViewCompat.setAlpha(newHolder.itemView, 0);
        }
        mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
        return true;
    }

    private void animateChangeImpl(final ChangeInfo changeInfo) {

    }

    private void endChangeAnimation(List<ChangeInfo> infoList, RecyclerView.ViewHolder item) {
        for (int i = infoList.size() - 1; i >= 0; i--) {
            ChangeInfo changeInfo = infoList.get(i);
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    infoList.remove(changeInfo);
                }
            }
        }
    }

    private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }

    private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder item) {
        boolean oldItem = false;
        if (changeInfo.newHolder == item) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder == item) {
            changeInfo.oldHolder = null;
            oldItem = true;
        } else {
            return false;
        }
        ViewCompat.setAlpha(item.itemView, 1);
        ViewCompat.setTranslationX(item.itemView, 0);
        ViewCompat.setTranslationY(item.itemView, 0);
        dispatchChangeFinished(item, oldItem);
        return true;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        final View view = item.itemView;
        // this will trigger end callback which should set properties to their target values.
        ViewCompat.animate(view).cancel();
        // TODO if some other animations are chained to end, how do we cancel them as well?
        for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
            MoveInfo moveInfo = mPendingMoves.get(i);
            if (moveInfo.holder == item) {
                ViewCompat.setTranslationY(view, 0);
                ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(item);
                mPendingMoves.remove(i);
            }
        }
        endChangeAnimation(mPendingChanges, item);
        if (mPendingRemovals.remove(item)) {
            ViewCompat.setAlpha(view, 1);
            dispatchRemoveFinished(item);
        }
        if (mPendingAdditions.remove(item)) {
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
        }
        for (int i = mChangesList.size() - 1; i >= 0; i--) {
            ArrayList<ChangeInfo> changes = mChangesList.get(i);
            endChangeAnimation(changes, item);
            if (changes.isEmpty()) {
                mChangesList.remove(changes);
            }
        }
        for (int i = mMovesList.size() - 1; i >= 0; i--) {
            ArrayList<MoveInfo> moves = mMovesList.get(i);
            for (int j = moves.size() - 1; j >= 0; j--) {
                MoveInfo moveInfo = moves.get(j);
                if (moveInfo.holder == item) {
                    ViewCompat.setTranslationY(view, 0);
                    ViewCompat.setTranslationX(view, 0);
                    dispatchMoveFinished(item);
                    moves.remove(j);
                    if (moves.isEmpty()) {
                        mMovesList.remove(moves);
                    }
                    break;
                }
            }
        }
        for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
            ArrayList<RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
            if (additions.remove(item)) {
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions);
                }
            }
        }
        dispatchFinishedWhenDone();
    }

    @Override
    public boolean isRunning() {
        return (!mPendingAdditions.isEmpty() ||
                !mPendingChanges.isEmpty() ||
                !mPendingMoves.isEmpty() ||
                !mPendingRemovals.isEmpty() ||
                !mMoveAnimations.isEmpty() ||
                !mRemoveAnimations.isEmpty() ||
                !mAddAnimations.isEmpty() ||
                !mChangeAnimations.isEmpty() ||
                !mMovesList.isEmpty() ||
                !mAdditionsList.isEmpty() ||
                !mChangesList.isEmpty());
    }

    private void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    @Override
    public void endAnimations() {
        int count = mPendingMoves.size();
        for (int i = count - 1; i >= 0; i--) {
            MoveInfo item = mPendingMoves.get(i);
            View view = item.holder.itemView;
            ViewCompat.setTranslationY(view, 0);
            ViewCompat.setTranslationX(view, 0);
            dispatchMoveFinished(item.holder);
            mPendingMoves.remove(i);
        }
        count = mPendingRemovals.size();
        for (int i = count - 1; i >= 0; i--) {
            RecyclerView.ViewHolder item = mPendingRemovals.get(i);
            dispatchRemoveFinished(item);
            mPendingRemovals.remove(i);
        }
        count = mPendingAdditions.size();
        for (int i = count - 1; i >= 0; i--) {
            RecyclerView.ViewHolder item = mPendingAdditions.get(i);
            View view = item.itemView;
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
            mPendingAdditions.remove(i);
        }
        count = mPendingChanges.size();
        for (int i = count - 1; i >= 0; i--) {
            endChangeAnimationIfNecessary(mPendingChanges.get(i));
        }
        mPendingChanges.clear();
        if (!isRunning()) {
            return;
        }
        int listCount = mMovesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<MoveInfo> moves = mMovesList.get(i);
            count = moves.size();
            for (int j = count - 1; j >= 0; j--) {
                MoveInfo moveInfo = moves.get(j);
                RecyclerView.ViewHolder item = moveInfo.holder;
                View view = item.itemView;
                ViewCompat.setTranslationY(view, 0);
                ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(moveInfo.holder);
                moves.remove(j);
                if (moves.isEmpty()) {
                    mMovesList.remove(moves);
                }
            }
        }
        listCount = mAdditionsList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
            count = additions.size();
            for (int j = count - 1; j >= 0; j--) {
                RecyclerView.ViewHolder item = additions.get(j);
                View view = item.itemView;
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                additions.remove(j);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions);
                }
            }
        }
        listCount = mChangesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<ChangeInfo> changes = mChangesList.get(i);
            count = changes.size();
            for (int j = count - 1; j >= 0; j--) {
                endChangeAnimationIfNecessary(changes.get(j));
                if (changes.isEmpty()) {
                    mChangesList.remove(changes);
                }
            }
        }
        cancelAll(mRemoveAnimations);
        cancelAll(mMoveAnimations);
        cancelAll(mAddAnimations);
        cancelAll(mChangeAnimations);
        dispatchAnimationsFinished();
    }

    void cancelAll(List<RecyclerView.ViewHolder> viewHolders) {
        for (int i = viewHolders.size() - 1; i >= 0; i--) {
            ViewCompat.animate(viewHolders.get(i).itemView).cancel();
        }
    }

    @Override
    public long getAddDuration() {
        return Constant.ANIM_DURATION_MILLIS;
    }

    @Override
    public long getRemoveDuration() {
        return Constant.ANIM_DURATION_MILLIS;
    }

    @Override
    public long getChangeDuration() {
        return Constant.ANIM_DURATION_MILLIS;
    }

    @Override
    public long getMoveDuration() {
        return Constant.ANIM_DURATION_MILLIS;
    }

    private static class MoveInfo {
        public RecyclerView.ViewHolder holder;
        public int fromX, fromY, toX, toY;

        private MoveInfo(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    private static class ChangeInfo {
        public RecyclerView.ViewHolder oldHolder, newHolder;
        public int fromX, fromY, toX, toY;

        private ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }

        private ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                           int fromX, int fromY, int toX, int toY) {
            this(oldHolder, newHolder);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

}
