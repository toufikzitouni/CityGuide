package com.city.guide;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.city.guide.fragments.ItemFragment;
import com.city.guide.listeners.DataListener;
import com.city.guide.utils.CityGuideUtils;

import retrofit.RetrofitError;


public class MyActivity extends ActionBarActivity {
    private static final int MAX_PROGRESS = 100;
    private final int[] mTextIds = {R.id.bar, R.id.bistro, R.id.cafe};

    private MyPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private SeekBar mSeekBar;
    private boolean mIsSeekBarDragged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new CustomPagerListener());

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarPageListener(mPager));

        getSupportActionBar().setTitle(R.string.actionbar_title);
        getSupportActionBar().setIcon(R.drawable.ic_drawer);

        CityGuideUtils.getInstance().pollResults(this, new DataListener() {
            @Override
            public void onDataReceived() {
                for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                    mPagerAdapter.refreshData(i);
                }
            }

            @Override
            public void onDataFailed(RetrofitError error) {

            }
        });
    }

    private class CustomPagerListener implements ViewPager.OnPageChangeListener {
        private int mCurrentPage;
        private int mPrevPage;
        private float mCurrentPositionOffset;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (!mIsSeekBarDragged) {
                if (positionOffsetPixels == 0) {
                    mPrevPage = mCurrentPage;
                } else if (Math.abs(mCurrentPositionOffset - positionOffset) >= 0.8) {
                    mPrevPage = mCurrentPage - 1;
                }

                mCurrentPositionOffset = positionOffset;
                int progressDone = (int)((float) mPrevPage /
                        (mPagerAdapter.getCount() - 1) * MAX_PROGRESS);
                int remainingProgress = (int)((float) positionOffsetPixels/mPager.getWidth()
                        * MAX_PROGRESS/(mPagerAdapter.getCount()-1));
                mSeekBar.setProgress(progressDone + remainingProgress);
            } else if (positionOffsetPixels == 0) {
                int newPosition = (mSeekBar.getProgress()*(mPagerAdapter.getCount()-1))/
                        MAX_PROGRESS;
                if (mPager.getCurrentItem() <= newPosition) {
                    mPager.setCurrentItem(newPosition);
                } else {
                    mPager.setCurrentItem(newPosition + 1);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mTextIds.length; i++) {
                if (i == position) {
                    ((TextView) findViewById(mTextIds[i])).setTextColor(
                            getResources().getColor(R.color.highlighted_text));
                } else {
                    ((TextView) findViewById(mTextIds[i])).setTextColor(
                            getResources().getColor(R.color.white_text));
                }
            }
            mCurrentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private SparseArray<ItemFragment> mMap = new SparseArray<ItemFragment>(3);
        private final String[] TITLES = { "Bar", "Restaurant", "Cafe" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            ItemFragment fragment = ItemFragment.newInstance(position, TITLES[position]);
            mMap.put(position, fragment);
            fragment.setAdapterData();
            return fragment;
        }

        public void refreshData(int position) {
            if (mMap.get(position) != null) {
                mMap.get(position).setAdapterData();
            }
        }

    }

    private class SeekBarPageListener implements SeekBar.OnSeekBarChangeListener {
        private ViewPager mPager;
        private int mMaxOffset;
        private int mCurrentOffset;

        public SeekBarPageListener(ViewPager pager) {
            mPager = pager;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mPager.isFakeDragging()) {
                int offset = (int) ((mMaxOffset /100.0) * progress);
                int dragby = -1 * (offset - mCurrentOffset);

                mPager.fakeDragBy(dragby);

                mCurrentOffset = offset;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsSeekBarDragged = true;
            mMaxOffset = mPager.getWidth() * (mPagerAdapter.getCount() - 1);

            mPager.beginFakeDrag();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPager.endFakeDrag();
            int interval = MAX_PROGRESS / (mPagerAdapter.getCount() - 1);
            int page = seekBar.getProgress()/interval +
                    ((seekBar.getProgress()%interval < interval/2) ? 0 : 1);
            mPager.setCurrentItem(page);
            int progress = mPager.getCurrentItem() *
                    (MAX_PROGRESS / (mPagerAdapter.getCount() - 1));

            if(android.os.Build.VERSION.SDK_INT >= 11){
                ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
                animation.setDuration(200);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
                animation.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIsSeekBarDragged = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            else {
                seekBar.setProgress(progress); // no animation on Gingerbread or lower
                mIsSeekBarDragged = false;
            }
        }
    }
}
