package com.example.soulocean1.workdome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ImageInfor> list;


    public MyAdapter(List<ImageInfor> list) {
        this.list = list;
    }

    //新建点击事件接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public interface OnItemlongLitener {
        void onItemlongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;
    private OnItemlongLitener mOnlongClickListener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setOnItemlongClickListener(OnItemlongLitener mOnlongClickListener) {
        this.mOnlongClickListener = mOnlongClickListener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 初始化布局视图
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定视图组件数据
     */
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // holder.iv_cardid.setText(list.get(position).getcID());



        holder.tv_isPublic.setText(list.get(position).getisP());

        holder.tv_title.setText(list.get(position).getName());
        holder.tv_place.setText(list.get(position).getTime());
        holder.tv_Itemtype.setText(list.get(position).getItemtype());
        holder.iv_backgroud.setBackgroundColor(list.get(position).getImageId());

        //holder.iv_backgroud.setBackgroundResource(list.get(position).getImageId());

        final int postions = position;
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }

        if (mOnlongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnlongClickListener.onItemlongClick(holder.itemView, pos);
                    return true;
                }
            });
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView iv_cardid;
        public TextView tv_title;
        public TextView tv_place;
        public TextView tv_Itemtype;
        public ImageView iv_backgroud;
        public TextView tv_isPublic;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_cardid = itemView.findViewById(R.id.card_id);
            tv_title =  itemView.findViewById(R.id.name);
            tv_place =  itemView.findViewById(R.id.place);
            tv_Itemtype = itemView.findViewById(R.id.Itemtype);
            iv_backgroud =  itemView.findViewById(R.id.picture);
            tv_isPublic= itemView.findViewById(R.id.publicc);
        }
    }


}
