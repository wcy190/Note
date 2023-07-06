package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.notes.bean.Note;
import com.example.notes.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private EditText etTitle,etContent,etWriter;
    private Note note;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        etWriter = findViewById(R.id.et_writer);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note"); //Note序列化了
        if (note != null) {
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
            etWriter.setText(note.getWriter());
        }
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    public void save(View view) {
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
        note.setWriter(writer);
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());
        long rowId = mNoteDbOpenHelper.updateData(note);
        if (rowId != -1) {
            ToastUtil.toastShort(this, "修改成功！");
            this.finish();//退出当前界面
        }else {
            ToastUtil.toastShort(this, "修改失败！");
        }
    }
    //格式化显示时间
    private String getCurrentTimeFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
}