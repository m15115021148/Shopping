package com.geek.shopping.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.util.ImageUtil;
import com.geek.shopping.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyIssueAdapter extends RecyclerView.Adapter<MyIssueAdapter.Holder> {
    private List<ProductModel> mList = new ArrayList<>();

    public void setData(List<ProductModel> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public List<ProductModel> getData() {
        return this.mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_issue_item_layout, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.initData(i);
    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private View mItemView;
        @BindView(R.id.title)
        public TextView mTitle;
        @BindView(R.id.img)
        public ImageView mImg;
        @BindView(R.id.detail)
        public TextView mDetail;
        @BindView(R.id.time)
        public TextView mTime;
        @BindView(R.id.delete)
        public TextView mDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemView = itemView;
        }

        public void initData(final int position) {
            final ProductModel model = mList.get(position);

            mTitle.setText(model.getTitle());
            mDetail.setText(model.getDetail());
            mTime.setText(model.getCreateTime()+"");

            String img = model.getImg();
            String[] split = img.split(";");

            if (split.length>0){
                ImageUtil.loadImage(mItemView.getContext(),split[0],R.drawable.head_defaut,mImg);
            }

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mItemView.getContext());
                    builder.setMessage("确定删除吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyApplication.getInstance().mDbIssue.delOneData(model.getIssueId());
                            ToastUtil.showShort(mItemView.getContext(),"删除成功");
                            mList.remove(position);
                            MyIssueAdapter.this.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
        }
    }
}
