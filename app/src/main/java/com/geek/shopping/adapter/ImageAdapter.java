package com.geek.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.geek.shopping.R;
import com.geek.shopping.model.ImageModel;
import com.geek.shopping.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;

    private List<ImageModel> mList = new ArrayList<>();

    private OnImageCallBack mCallBack;

    public interface OnImageCallBack {
        void onClickListener(int position);
    }

    public ImageAdapter(Context context,OnImageCallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }

    public void setData(List<ImageModel> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageModel notice = mList.get(position);
        if (notice.getFile() != null) {
            Glide.with(mContext).
                    load(notice.getFile())
                    .crossFade()
                    .override(240, 240)
                    .dontTransform()
                    .into(holder.iv_img);
        } else {
            ImageUtil.loadNativeCommonImage(mContext, R.drawable.add_pic, holder.iv_img);
        }
        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) mCallBack.onClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_img)
        ImageView iv_img;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
