package com.example.soulocean1.workdome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.github.tonnyl.light.Light;

public class EnrollActivity extends AppCompatActivity {

    private boolean PASS = false;
    private RelativeLayout relLayout;
    private EditText Enraccountedit, Enrpasswordedit, StuNumber;
    String account, password, stunumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        Bmob.initialize(this, "fd29bf904043d253d94871e047192af8");


        relLayout = findViewById(R.id.rellayoutview);
        Button enrollbuttom = findViewById(R.id.EnrollButtom);

        Enraccountedit = findViewById(R.id.Enraccount);
        Enrpasswordedit = findViewById(R.id.Enrpassword);


        //注册
        enrollbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = Enraccountedit.getText().toString();
                password = Enrpasswordedit.getText().toString();

                int aeesize = Enraccountedit.getText().length();
                int peesize = Enrpasswordedit.getText().length();


                if (aeesize <= 5) {
                    Light.error(relLayout, "用户名需大于5位", Light.LENGTH_SHORT).show();
                } else if (peesize <= 6) {
                    Light.error(relLayout, "密码需大于6位", Light.LENGTH_SHORT).show();
                } else {


                    MyUser bu = new MyUser();
                    bu.setUsername(account);
                    bu.setPassword(password);


                    bu.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser s, BmobException e) {
                            if (e == null) {
                                Light.success(relLayout, "注册成功\n" + s.toString(), Light.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if (e.getErrorCode() == 9016) {
                                    Light.error(relLayout, "网络链接失败", Light.LENGTH_SHORT).show();
                                } else if (e.getErrorCode() == 202) {
                                    Light.error(relLayout, "用户名已被注册", Light.LENGTH_SHORT).show();
                                } else {
                                    Light.error(relLayout, "注册失败\n" + e.toString(), Light.LENGTH_SHORT).show();
                                }


                            }
                        }
                    });


                }

            }
        });


    }


    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // Intent intent = new Intent(Logo.this,LoginActivity.class);
            Intent intent = new Intent(EnrollActivity.this, MainActivity.class);
            intent.putExtra("data_account", account);//用putExtra把内容传送到另一个Activity,名字是data，值是account
            startActivity(intent);
            finish();
        }
    };


}
