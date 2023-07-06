package com.example.notes.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.EditActivity;
import com.example.notes.NoteDbOpenHelper;
import com.example.notes.R;
import com.example.notes.bean.Note;
import com.example.notes.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private NoteDbOpenHelper mNoteDbOpenHelper;


    public MyAdapter(Context context, List<Note> mBeanList){
        this.mBeanList = mBeanList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.list_item_layout,parent,false);

        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.mTvWriter.setText(note.getWriter());

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到编辑
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                //长按弹出弹窗 删除或者编辑
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                View view=mLayoutInflater.inflate(R.layout.list_item_dialog_layout,null);

                TextView tvDelete=view.findViewById(R.id.tv_delete);
                TextView tvEdit=view.findViewById(R.id.tv_edit);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int row=mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if(row>0){
                          deleteData(holder.getAdapterPosition());
                          ToastUtil.toastShort(mContext,"删除成功！");
                        }else {
                          ToastUtil.toastShort(mContext,"删除失败！");
                        }
                        dialog.dismiss();
                    }
                });

                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EditActivity.class);
                        intent.putExtra("note", note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {

        return mBeanList.size();
    }

    public void refreshData(List<Note> notes){
        this.mBeanList = notes; //替换新数据
        notifyDataSetChanged(); //通知数据已经改变
    }

    public void deleteData(int pos){
        mBeanList.remove(pos);
        notifyItemRemoved(pos);

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle; //标题
        TextView mTvContent;//内容
        TextView mTvTime;//时间
        TextView mTvWriter;//作者
        ViewGroup rlContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.mTvWriter=itemView.findViewById(R.id.tv_writer);

            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

}
