package io.animal.monkey.ui.guide;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import io.animal.monkey.R;
import io.animal.monkey.util.SharedPreferencesHelper;

public class AppGuideFragment extends DialogFragment {

    public final static String TAG = "AppGuideFragment";

    private ViewPager viewPager;
    private Button button;
    private SliderPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        // making activity full screen
//        if (Build.VERSION.SDK_INT >= 21) {
//            getActivity()
//                    .getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

//        getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.app_guide_fragment, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGuideFragment.super.dismiss();
            }
        });

        // bind views
        viewPager = view.findViewById(R.id.pagerIntroSlider);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        button = view.findViewById(R.id.button);

        // init slider pager adapter
        adapter = new SliderPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // set adapter
        viewPager.setAdapter(adapter);

        // set dot indicators
        tabLayout.setupWithViewPager(viewPager);

        // make status bar transparent
        changeStatusBarColor();

        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Log.d(TAG, "" + viewPager.getCurrentItem() +  adapter.getCount());
                if (viewPager.getCurrentItem() < (adapter.getCount() - 1)) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    // Checked AppGuide
                    getSharedPref().setGuided(true);

                    AppGuideFragment.super.dismiss();
                }
            }
        });

        /**
         * Add a listener that will be invoked whenever the page changes
         * or is incrementally scrolled
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override public void onPageSelected(int position) {
                if (position == adapter.getCount() - 1) {
                    button.setText(R.string.get_started);
                } else {
                    button.setText(R.string.next);
                }
            }
            @Override public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // reload
        viewPager.setCurrentItem(0);
    }

    /// --------------------------------------------------------------------------------- SharedPref

    private SharedPreferencesHelper _sp;

    private SharedPreferencesHelper getSharedPref() {
        if (_sp == null) {
            _sp = new SharedPreferencesHelper(getContext());
        }
        return _sp;
    }

    /// ----------------------------------------------------------------------------- SharedPref end

    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }
}
