package com.geek.shopping.adapter;


import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.model.HealthModel;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends PagerAdapter {
    private List<HealthModel> mList = new ArrayList<>();

    public void setData(List<HealthModel> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size() ;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.home_pager_item_layout, null);

        TextView title = view.findViewById(R.id.title);
        TextView detail = view.findViewById(R.id.detail);

        HealthModel model = mList.get(position);

        title.setText(model.getTitle());
        detail.setText(model.getDes());

        container.addView(view);
        return view;
    }
}
