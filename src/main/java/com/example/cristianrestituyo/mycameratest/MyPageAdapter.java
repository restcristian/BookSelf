package com.example.cristianrestituyo.mycameratest;


import android.support.v4.app.*;

import wreckingball.bookself.booklist.ListViewActivity;


/**
 * Created by CristianRestituyo on 3/25/2015.
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    public int getCount() {
        return 2;
    }

    public MyPageAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new MainActivity();
            case 1:
                return new ListViewActivity();
        }
        return null;
    }

    /*
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Fragment fr = new MainActivity();
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.layout.activity_main;
                fr = new MainActivity();
                break;
            case 1:
                resId = R.layout.activity_list_view;
                fr = new MainActivity();
                break;
        }


        ((ViewPager) collection).addView(fr.getView(), 0);

        return fr.getView();
    }
*/
/*
    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }

    @Override
    public Parcelable saveState() {
        return null;
    }
    */
}