package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.adapter.MyAdapter;
import com.example.notes.bean.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    private List<Note> mNotes;
    private MyAdapter mMyAdapter;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();

    }
    @Override
    protected void onResume(){
        super.onResume();
        refreshDataFromDb();// 从数据库刷新数据
    }

    private void refreshDataFromDb() {
        mNotes  =   getDataFromDB();// 从数据库获取数据
        mMyAdapter.refreshData(mNotes);// 使用mNotes刷新适配器数据
    }

    private void initEvent() {
        //初始化列表
        mMyAdapter = new MyAdapter(this,mNotes);
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        //初始化数据
        mNotes = new ArrayList<>();
        mNoteDbOpenHelper =new NoteDbOpenHelper(this);
        /*  //测试
        for(int i =0;i<5;i++){
            Note note =new Note();
            note.setTitle("这是标题"+i);
            note.setContent("这是内容"+i);
            note.setCreatedTime(getCuurentTimeFormat());
            note.setWriter("我是作者"+i);
            mNotes.add(note);
        }*/
        mNotes=getDataFromDB();// 从数据库获取数据

    }
    private List<Note> getDataFromDB() {
      return  mNoteDbOpenHelper.queryAllFromDb();
    }

    private void initView() {
        //初始化试图
        mRecyclerView =findViewById(R.id.rlv);
    }
    //点击加号跳转
    public void add(View view) {
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }
}