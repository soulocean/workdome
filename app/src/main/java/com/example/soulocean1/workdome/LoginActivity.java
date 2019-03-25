package com.example.soulocean1.workdome;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.github.tonnyl.light.Light;


public class LoginActivity extends AppCompatActivity {


    private boolean PASS = false;
    private RelativeLayout relativeLayout;
    private EditText accountedit,passwordedit;
    String account,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "fd29bf904043d253d94871e047192af8");



        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

        relativeLayout=findViewById(R.id.relativelayoutview);
        final TextView textView= findViewById(R.id.loginErrtext);

        Button newaccount = findViewById(R.id.newaccountc);
        Button loginbuttom = findViewById(R.id.loginbuttom);
        TextView forgetpassward = findViewById(R.id.forgetpassward);
        ImageView loginLogo = findViewById(R.id.login_image);

        accountedit = findViewById(R.id.account);
        passwordedit = findViewById(R.id.password);


        account = accountedit.getText().toString();
        password = passwordedit.getText().toString();


        //注册
        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,EnrollActivity.class);
                startActivity(intent);
            }
        });


        //登陆
        loginbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = accountedit.getText().toString();
                password = passwordedit.getText().toString();
                textView.setText("");
                int aesize= accountedit.getText().length();
                int pesize= passwordedit.getText().length();

                if (aesize==0||pesize==0){
                    Light.error(relativeLayout, "请输入用户名或密码", Light.LENGTH_SHORT).show();

                }

                else if (aesize<5){
                    Light.error(relativeLayout, "用户名需大于5位", Light.LENGTH_SHORT).show();
                }

                else if (pesize<6){
                    Light.error(relativeLayout, "密码需大于6位", Light.LENGTH_SHORT).show();
                }


                else{


                    BmobUser bu2 = new BmobUser();
                    bu2.setUsername(account);
                    bu2.setPassword(password);
                    bu2.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {


                            if (e == null) {
                                Light.success(relativeLayout, account + "  欢迎登陆", Light.LENGTH_SHORT).show();
                                //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                if (e.getErrorCode() == 101) {
                                    Light.error(relativeLayout, "用户名或密码不正确", Light.LENGTH_SHORT).show();
                                }
                                else if (e.getErrorCode()==9015)
                                {
                                    Light.error(relativeLayout, "请连接网络", Light.LENGTH_SHORT).show();
                                }
                                else {
                                    Light.error(relativeLayout, "登陆失败\n" + e.toString(), Light.LENGTH_SHORT).show();
                                    textView.setText(e.toString());
                                }
                            }
                        }
                    });



                }

            }
        });






        loginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgetpassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,forgetpassword.class);
                startActivity(intent);

            }
        });




    }



}