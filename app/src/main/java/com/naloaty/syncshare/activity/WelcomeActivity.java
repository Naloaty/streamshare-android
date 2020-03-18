package com.naloaty.syncshare.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naloaty.syncshare.R;
import com.naloaty.syncshare.app.Activity;
import com.naloaty.syncshare.util.AppUtils;
import com.naloaty.syncshare.widget.DynamicViewPagerAdapter;

public class WelcomeActivity extends Activity {

    private ViewGroup mSplashView;
    private ViewGroup mPermissionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setSkipPermissionRequest(true);
        setWelcomePageDisallowed(true);

        final FloatingActionButton nextButton = findViewById(R.id.activity_welcome_view_next);
        final AppCompatImageView previousButton = findViewById(R.id.activity_welcome_view_previous);
        final ProgressBar progressBar = findViewById(R.id.activity_welcome_progress_bar);
        final ViewPager viewPager = findViewById(R.id.activity_welcome_view_pager);
        final DynamicViewPagerAdapter pagerAdapter = new DynamicViewPagerAdapter();

        /*--------- layout_welcome_page_1 ------------ */
        mSplashView = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_welcome_page_1, null, false);
        pagerAdapter.addView(mSplashView);

        /*--------- layout_welcome_page_2 ------------ */
        if (Build.VERSION.SDK_INT >= 23) {
            mPermissionsView = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_welcome_page_2, null, false);
            pagerAdapter.addView(mPermissionsView);
            checkPermissionsState();

            mPermissionsView.findViewById(R.id.layout_welcome_page_2_request_btn)
                    .setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            requestRequiredPermissions(false);
                        }
                    });
        }

        /*--------- layout_welcome_page_3 ------------ */
        View view = getLayoutInflater().inflate(R.layout.layout_welcome_page_3, null, false);
        pagerAdapter.addView(view);

        /*--------- Widgets setup ------------ */
        progressBar.setMax((pagerAdapter.getCount() - 1) * 100);

        previousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewPager.getCurrentItem() - 1 >= 0)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewPager.getCurrentItem() + 1 < pagerAdapter.getCount())
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                else {
                    // end presentation
                    /*getDefaultPreferences().edit()
                            .putBoolean("introduction_shown", true)
                            .apply();*/

                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                progressBar.setProgress((position * 100) + (int) (positionOffset * 100));

                if (position == 0) {
                    progressBar.setAlpha(positionOffset);
                    previousButton.setAlpha(positionOffset);
                } else {
                    progressBar.setAlpha(1.0f);
                    previousButton.setAlpha(1.0f);
                }
            }

            @Override
            public void onPageSelected(int position)
            {
                nextButton.setImageResource(position + 1 >= pagerAdapter.getCount()
                        ? R.drawable.ic_check_white_24dp
                        : R.drawable.ic_navigate_next_white_24dp);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });


        /*--------- All set ------------ */
        viewPager.setAdapter(pagerAdapter);
    }

    protected void checkPermissionsState()
    {
        if (Build.VERSION.SDK_INT < 23)
            return;

        boolean permissionsOk = AppUtils.checkRunningConditions(this);

        mPermissionsView.findViewById(R.id.layout_welcome_page_2_perm_ok_img)
                .setVisibility(permissionsOk ? View.VISIBLE : View.GONE);

        mPermissionsView.findViewById(R.id.layout_welcome_page_2_request_btn)
                .setVisibility(permissionsOk ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissionsState();
    }
}