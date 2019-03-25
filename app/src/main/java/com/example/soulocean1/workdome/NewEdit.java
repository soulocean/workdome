package com.example.soulocean1.workdome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.TextView;

public class NewEdit extends AppCompatActivity {
    public String Positionc;
    public String Titlec;
    public String Datac;
    public boolean isBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);


        final Intent i = getIntent();//因为Activity是通过intend来启动的，所以通过getIntend来获取与这个Activity相关的数据
        Positionc = i.getStringExtra("position");
        Titlec = i.getStringExtra("title");


        TextView title_Text = findViewById(R.id.editTitleText);
        title_Text.setText(Titlec);

        TextView mian_Textc = findViewById(R.id.edit_MainText);
        CheckBox IsPrivate = findViewById(R.id.edit_checkbox);

        Datac = mian_Textc.getText().toString();
        isBox = IsPrivate.getFreezesText();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Finish();
        return super.onKeyDown(keyCode, event);
    }


    private void Finish() {
        Intent intent = new Intent(NewEdit.this, MainActivity.class);
        intent.putExtra("NewEdit_title", Titlec);
        intent.putExtra("NewEdit_Data", Datac);
        intent.putExtra("NewEdit_isBox", isBox);
        startActivity(intent);


        finish();

    }


}
