package de.sourcestream.movieDB.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.sourcestream.movieDB.R;


public class MovieDetailsSlidingTabLayout extends HorizontalScrollView {


    public interface TabColorizer {


        int getDividerColor(int position);

        int getIndicatorColor(int mSelectedPosition);
    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 14;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final MovieDetailsSlidingTabStrip mTabStrip;

    public MovieDetailsSlidingTabLayout(Context context) {
        this(context, null);
    }

    public MovieDetailsSlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieDetailsSlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new MovieDetailsSlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, (int) (49 * getResources().getDisplayMetrics().density));
    }


    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }


    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }


    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }


    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }


    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }


    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        boolean phone = getResources().getBoolean(R.bool.portrait_only);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        int height = size.y;
        // special case for 7' inch tablets
        // if we leave padding to 16 the text title on the viewPager will be cut
        if (!phone && ((width == 1024 && height == 600) || (height == 1024 && width == 600)))
            TAB_VIEW_PADDING_DIPS = 14;

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            tabView.setLayoutParams(lp);

            mTabStrip.addView(tabView);

            tabTitleView.setTextColor(getResources().getColorStateList(R.color.tabselector));

            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                mTabStrip.getChildAt(i).setSelected(position == i);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public void disableTabClickListener(int i) {
        mTabStrip.getChildAt(i).setClickable(false);
    }

}
