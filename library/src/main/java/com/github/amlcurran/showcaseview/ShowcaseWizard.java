package com.github.amlcurran.showcaseview;

import android.app.Activity;

import com.github.amlcurran.showcaseview.targets.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Donzello <francesco.donzello@immobiliare.it>
 */
public class ShowcaseWizard implements OnShowcaseEventListener {

    private final Activity mContext;
    private List<ShowcasePreparedView> elements;
    private ShowcaseFinishedListener mListener;

    public interface ShowcaseFinishedListener {
        void onFinish();
    }

    public interface ShowcaseElementTransitionListener {
        void onSelected();
    }

    public ShowcaseWizard(Activity context) {
        elements = new ArrayList<>();
        mContext = context;
    }

    public ShowcaseWizard add(Target target, int titleId, int contentId) {
        elements.add(new ShowcasePreparedView(target, titleId, contentId));
        return this;
    }

    public ShowcaseWizard add(Target target, int titleId, int contentId, ShowcaseElementTransitionListener listener) {
        elements.add(new ShowcasePreparedView(target, titleId, contentId, listener));
        return this;
    }

    public void play() {
        if (elements.size() > 0) {
            play(elements.get(0));
        }
    }

    private void play(ShowcasePreparedView builder) {
        new ShowcaseView.Builder(mContext)
                .setTarget(builder.getTarget())
                .setContentTitle(mContext.getResources().getString(builder.getTitleResId()))
                .setContentText(mContext.getResources().getString(builder.getContentResId()))
                .hideOnTouchOutside()
                .setStyle(R.style.ShowcaseView_Light)
                .setShowcaseEventListener(this)
                .build()
                .hideButton();

        if (builder.listener != null) {
            builder.listener.onSelected();
        }

        // removing last element
        elements.remove(builder);
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        // going further if there are more elements
        if (elements.size() > 0) {
            play(elements.get(0));
        } else {
            if (mListener != null) {
                mListener.onFinish();
            }
        }
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    public ShowcaseWizard setShowcaseFinishedListener(ShowcaseFinishedListener listener) {
        mListener = listener;
        return this;
    }

    private class ShowcasePreparedView {

        private final int titleResId;
        private final Target target;
        private final int contentResId;
        private final ShowcaseElementTransitionListener listener;

        public ShowcasePreparedView(Target target, int titleResId, int contentResId) {
            this.target = target;
            this.titleResId = titleResId;
            this.contentResId = contentResId;
            this.listener = null;
        }

        public ShowcasePreparedView(Target target, int titleResId, int contentResId, ShowcaseElementTransitionListener listener) {
            this.target = target;
            this.titleResId = titleResId;
            this.contentResId = contentResId;
            this.listener = listener;
        }

        public int getTitleResId() {
            return titleResId;
        }

        public Target getTarget() {
            return target;
        }

        public int getContentResId() {
            return contentResId;
        }
    }
}
