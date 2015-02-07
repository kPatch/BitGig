package com.bitgig.bitgig.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitgig.bitgig.MainActivity;
import com.bitgig.bitgig.R;
import com.bitgig.bitgig.util.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irvin on 2/7/2015.
 */
public class WelcomeActivity extends Activity {

    private ViewPager splashPager;
    private List<View> list;
    private TextView dot0, dot1, dot2, dot3, dot4;
    private View splashView;
    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefUtils.markTosAccepted(WelcomeActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_welcome_activity);
        splashView = this.findViewById(R.id.splash_layout);
        initDot();
        initViewPager();

        next = (ImageView) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDot(){
        dot0=(TextView) this.findViewById(R.id.textView0);
        dot1=(TextView) this.findViewById(R.id.textView1);
        dot2=(TextView) this.findViewById(R.id.textView2);
        dot3=(TextView) this.findViewById(R.id.textView3);
        dot4=(TextView) this.findViewById(R.id.textView4);
    }

    private void initViewPager(){
        splashPager = (ViewPager) this.findViewById(R.id.viewPager);
        list=new ArrayList<View>();

        LayoutInflater inflater=getLayoutInflater();

        list.add(inflater.inflate(R.layout.splash_item_0, null));
        list.add(inflater.inflate(R.layout.splash_item_1, null));
        list.add(inflater.inflate(R.layout.splash_item_2, null));
        list.add(inflater.inflate(R.layout.splash_item_3, null));
        View view = inflater.inflate(R.layout.splash_item_4, null);
        list.add(view);

        splashPager.setPageTransformer(false, new FadePageTransformer());
        splashPager.setAdapter(new MyPagerAdapter(list));
        splashPager.setOnPageChangeListener(new MyPagerChangeListener());
    }

    class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }
    }

    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 0:
                    dot0.setTextColor(Color.BLACK);
                    dot1.setTextColor(Color.GRAY);
                    dot2.setTextColor(Color.GRAY);
                    dot3.setTextColor(Color.GRAY);
                    dot4.setTextColor(Color.GRAY);
                    break;
                case 1:
                    dot0.setTextColor(Color.GRAY);
                    dot1.setTextColor(Color.BLACK);
                    dot2.setTextColor(Color.GRAY);
                    dot3.setTextColor(Color.GRAY);
                    dot4.setTextColor(Color.GRAY);
                    break;

                case 2:
                    dot0.setTextColor(Color.GRAY);
                    dot1.setTextColor(Color.GRAY);
                    dot2.setTextColor(Color.BLACK);
                    dot3.setTextColor(Color.GRAY);
                    dot4.setTextColor(Color.GRAY);
                    break;

                case 3:
                    dot0.setTextColor(Color.GRAY);
                    dot1.setTextColor(Color.GRAY);
                    dot2.setTextColor(Color.GRAY);
                    dot3.setTextColor(Color.BLACK);
                    dot4.setTextColor(Color.GRAY);
                    break;

                case 4:
                    dot0.setTextColor(Color.GRAY);
                    dot1.setTextColor(Color.GRAY);
                    dot2.setTextColor(Color.GRAY);
                    dot3.setTextColor(Color.GRAY);
                    dot4.setTextColor(Color.BLACK);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }
    }

    private static class FadePageTransformer implements ViewPager.PageTransformer {
        // Jittery translation
        public void transformPage(View view, float position) {
            view.setAlpha(1 - Math.abs(position));
            if (position < 0) {
                view.setScrollX((int)((float)(view.getWidth()) * position));
            } else if (position > 0) {
                view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
            } else {
                view.setScrollX(0);
            }
        }


    }
}
