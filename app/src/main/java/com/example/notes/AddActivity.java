package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.notes.bean.Note;
import com.example.notes.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private EditText etTitle,etContent,etWriter;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        etWriter = findViewById(R.id.et_writer);

        mNoteDbOpenHelper=new NoteDbOpenHelper(this);

    }

    public void add(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String writer = etWriter.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空！");
            return;
        }
        if(TextUtils.isEmpty(writer)) {
            writer="匿名";
            ToastUtil.toastShort(this, "使用默认作者！");
        }
        Note note = new Note();

        note.setTitle(title);
        note.setContent(content);
        note.setWriter(writer);
        note.setCreatedTime(getCurrentTimeFormat());
        long row=mNoteDbOpenHelper.insertData(note);
        if(row!=-1){
            ToastUtil.toastShort(this,"添加成功！");
            this.finish();
        }else {
            ToastUtil.toastShort(this,"添加失败");
        }

    }
    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY年MM月dd日  HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);

    }
}