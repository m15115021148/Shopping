package com.geek.shopping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.util.ImageUtil;
import com.geek.shopping.view.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${chenM} on 2019/5/21.
 */
public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.Holder>{
    private List<ProductModel> mList = new ArrayList<>();

    public void setData(List<ProductModel> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public List<ProductModel> getData(){
        return this.mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.issue_banner_layout,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.initData(i);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private View mItemView;
        @BindView(R.id.img)
        public MyImageView mImg;
        @BindView(R.id.detail)
        public TextView mDetail;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mItemView = itemView;
        }

        public void initData(int position){
            ProductModel model = mList.get(position);

            String[] split = model.getImg().split(";");
            if (split != null && split.length>0){
                ImageUtil.loadImage(mItemView.getContext(),split[0],R.drawable.head_defaut,mImg);
            }else {
                mImg.setBackgroundResource(R.drawable.head_defaut);
            }
            mDetail.setText(model.getDetail());
        }

    }
}
