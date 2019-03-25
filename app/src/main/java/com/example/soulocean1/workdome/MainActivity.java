package com.example.soulocean1.workdome;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.tonnyl.light.Light;

public class MainActivity extends AppCompatActivity {

    public static final int MSG_ONE = 1;
    private static final String TAG = "filec";
    private static String CALENDAR_EVENT_URL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";
    public List<ImageInfor> list1;
    public String PublicName = "Public_Note";
    RecyclerView main_rv;
    private int Psum = 0;
    private int Psumc = 0;
    private DrawerLayout mDrawerLayout;
    private String newTitle;
    private String newTypeing;
    private int newPriority;
    private TextView tv_time;
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_ONE:
                    //获取网络时间
                    //请求网络资源是耗时操作。放到子线程中进行
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            getNetTime();
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    };
    private Calendar mCalendar = Calendar.getInstance();
    private int Ryear;
    private int Rmonth;
    private int Rday;
    private int Rhour;
    private int Rminute;
    private Switch mSwitch;
    private MyAdapter myAdapter;
    private String username;

    private boolean selectTime = false;
    private int Modec = 0;

    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("filee", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
            //Log.e("文件",i+" "+files[i].getAbsolutePath());
            Log.d(TAG, i + " " + files[i].getAbsolutePath());
        }
        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new TimeThread().start();


        main_rv = findViewById(R.id.main_rv);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        username = (String) BmobUser.getObjectByKey("username");


        Light.success(main_rv, username + "欢迎使用", Light.LENGTH_SHORT).show();

        tv_time = findViewById(R.id.main_Time);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("备忘卡");


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu); //设置menu键得图标
        }


        //添加数据
        list1 = new ArrayList<>();


        createFile(PublicName);
        createFile("" + username);

        getFilesAllName(getApplicationContext().getFilesDir().getAbsolutePath() + "Soul_note");
//
        String dataname = "Psum" + username;
        File file = new File("/data/data/com.example.soulocean1.workdome/files/" + dataname);

        String datanamec = "Psum" + "publicc";
        File filec = new File("/data/data/com.example.soulocean1.workdome/files/" + datanamec);


        if (filec.exists()) {

            PsumLoad("publicc");
            Psumc = Psum;
            Psum = 0;

            for (int k = 0; k < Psumc; k++) {
                list1.add(new ImageInfor(k, "c", "c", "", 0, ""));
                Itemload(list1, k, 0, PublicName);
            }

        }


        if (file.exists()) {

            PsumLoad(username);
            int c = 0;
            if (Psumc > 0) c = Psumc;

            for (int k = 0; k < Psum; k++) {
                list1.add(new ImageInfor(k + Psumc, "#", "#", "", 0, ""));


                Itemload(list1, k, c, username);
            }

        }


        //------------------------------------------------------
        //设置列表显示方式
        main_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //设置列表默认动画效果
        main_rv.setItemAnimator(new DefaultItemAnimator());

        myAdapter = new MyAdapter(list1);

        main_rv.setAdapter(myAdapter);


        //列表点击事件
        myAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                Light.success(main_rv, "这个备忘是: " + list1.get(position).getName() + "\n建立时间为: " + list1.get(position).getTime(), Light.LENGTH_SHORT).show();

            }
        });

        myAdapter.setOnItemlongClickListener(new MyAdapter.OnItemlongLitener() {
            @Override
            public void onItemlongClick(View view, int position) {
                //Light.info(main_rv, "长按 " + list1.get(position).getName(), Light.LENGTH_SHORT).show();
                LongSetEvent(position, list1, myAdapter);
            }

        });


//悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*

                Toast.makeText(getApplicationContext(),"你点击了悬浮按钮",Toast.LENGTH_SHORT).show();
                newEditView(myAdapter.getItemCount() + 1, "无标题",list1);
                */
                item_edit(myAdapter.getItemCount(), list1, myAdapter);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.nav_changeMade:
                        Switch aSwitch = findViewById(R.id.switchButton);
                        if (!aSwitch.isChecked()) {
                            aSwitch.setChecked(true);
                            Modec = 1;
                            Light.success(mDrawerLayout, "响铃模式 ", Light.LENGTH_SHORT).show();
                            //PsumSave(1,"mode");

                        } else {
                            aSwitch.setChecked(false);
                            Modec = 0;
                            Light.success(mDrawerLayout, "震动模式 ", Light.LENGTH_SHORT).show();
                            //PsumSave(0,"mode");

                        }


                        break;

                    case R.id.nav_logout:
                        BmobUser.logOut();
                        /* Light.success(mDrawerLayout, "exit", Light.LENGTH_SHORT).show();*/
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }


                mDrawerLayout.closeDrawers();
                item.setChecked(false);
                return true;
            }


        });


        View HeadView = navigationView.getHeaderView(0);
        CircleImageView Micon = HeadView.findViewById(R.id.icon_image);
        TextView Musername = HeadView.findViewById(R.id.username);
        TextView Musermail = HeadView.findViewById(R.id.mail);


        Musername.setText(username);
        Musername.setTextSize(24);

        Musermail.setText(username + "@gmail.com");


        Micon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Light.info(mDrawerLayout, "正在开发", Light.LENGTH_SHORT).show();
            }
        });

    }

    public void LongSetEvent(final int position, final List<ImageInfor> lis, final MyAdapter myAdapter) {


        TextView ShowSetTime = new TextView(this);
        final EditText EditTypedata = new EditText(this);

        ShowSetTime.setText(lis.get(position).getTime());
        EditTypedata.setText(lis.get(position).getItemtype());


        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(ShowSetTime);
        linearLayout.addView(EditTypedata);


        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("🌿 " + lis.get(position).getName());

        builder.setMessage("这张备忘卡片将会提醒于");
        builder.setView(linearLayout);

        builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (position < Psumc) {


                    ItemDel(position + "c" + PublicName, PublicName);
                    Light.info(mDrawerLayout, lis.get(position).getName() + " 已删除(共享)", Light.LENGTH_SHORT).show();
                    lis.remove(position);
                    Psumc--;
                    PsumSave(Psumc, "publicc");


                    for (int i = 0; i < Psumc; i++) {
                        ItemSave(i, lis.get(i).getName(), lis.get(i).getItemtype(), lis.get(i).getTime(), lis.get(i).get_INT_ImageId(), PublicName, lis.get(i).getisP());
                        Itemload(list1, i, 0, PublicName);
                    }


                } else if (position >= Psumc) {
                    ItemDel(position - Psumc + "c" + username, username);
                    Light.info(mDrawerLayout, lis.get(position).getName() + " 已删除(私人)", Light.LENGTH_SHORT).show();
                    lis.remove(position);
                    Psum--;
                    PsumSave(Psum, username);


                    for (int i = 0; i < Psum; i++) {
                        ItemSave(i, lis.get(i + Psumc).getName(), lis.get(i + Psumc).getItemtype(), lis.get(i + Psumc).getTime(), lis.get(i + Psumc).get_INT_ImageId(), username, lis.get(i + Psumc).getisP());
                        Itemload(list1, i, Psumc, username);
                    }
                }


                main_rv.setAdapter(myAdapter);


            }
        });
        builder.setNegativeButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, lis.get(position).getItemtype());
                startActivity(Intent.createChooser(textIntent, lis.get(position).getName()));
            }
        });

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ChangeType;
                ChangeType = EditTypedata.getText().toString();
                lis.get(position).setItemtype(ChangeType);

                if (position < Psumc) {
                    ItemSave(position, lis.get(position).getName(), ChangeType, lis.get(position).getTime(), lis.get(position).get_INT_ImageId(), PublicName, lis.get(position).getisP());//保存数据————————————————————test————————
                } else
                    ItemSave(position - Psumc, lis.get(position).getName(), ChangeType, lis.get(position).getTime(), lis.get(position).get_INT_ImageId(), username, lis.get(position).getisP());//保存数据————————————————————test————————


                main_rv.setAdapter(myAdapter);

                Light.info(main_rv, list1.get(position).getName() + "修改保存完成", Light.LENGTH_SHORT).show();


            }
        });

        builder.show();

    }

    public void item_edit(final int position, final List<ImageInfor> lis, final MyAdapter myAdapter) {

        final EditText et = new EditText(this);
        et.setHint("标题");

        final EditText ed = new EditText(this);
        ed.setHint("内容");

        final CheckBox cb = new CheckBox(this);
        cb.setText("标为共享");

        final CheckBox cbremind = new CheckBox(this);
        cbremind.setText("添加提醒");


        final TextView dataView = new TextView(this);
        final TextView timeView = new TextView(this);


        final Button date = new Button(this);
        date.setText("选择提醒日期");
        final Button time = new Button(this);
        time.setText("选择提醒时间");

        final NumberPicker valuepicker = new NumberPicker(this);

        valuepicker.setMinValue(1);
        valuepicker.setMaxValue(7);
        valuepicker.setValue(1);

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("添加备忘提醒🌿 ");
        final String timetemp = tv_time.getText().toString();

        Light.info(mDrawerLayout, timetemp + " 创建", Light.LENGTH_SHORT).show();


        final LinearLayout linearLayout = new LinearLayout(this);
        final LinearLayout linearLayout2h = new LinearLayout(this);


        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout2h.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout2h.addView(cb);
        linearLayout2h.addView(cbremind);


        linearLayout.addView(linearLayout2h);

        linearLayout.addView(dataView);
        linearLayout.addView(timeView);

        linearLayout.addView(et);
        linearLayout.addView(ed);

        linearLayout.addView(valuepicker);

        builder.setView(linearLayout);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(MainActivity.this, 2, dataView, mCalendar);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(MainActivity.this, 2, timeView, mCalendar);
                selectTime = true;
            }
        });


        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {


                newTitle = et.getText().toString();
                newTypeing = ed.getText().toString();
                newPriority = valuepicker.getValue();

                String AddUpTime;
                AddUpTime = dataView.getText().toString() + timeView.getText().toString();

                //Toast.makeText(getApplicationContext(), "" + newTitle, Toast.LENGTH_LONG).show();

                if (cbremind.isChecked()) remind(newTitle, newTypeing);
                String isPu = "";


                if (cb.isChecked()) {
                    isPu = "⭐";
                    Toast.makeText(getApplicationContext(), "共享卡片", Toast.LENGTH_LONG).show();

                    PsumSave(Psumc, "publicc");
                    ItemSave(Psumc, newTitle, newTypeing, AddUpTime, newPriority, PublicName, isPu);
                    Psumc++;


                } else {
                    Toast.makeText(getApplicationContext(), "私人卡片", Toast.LENGTH_LONG).show();

                    Psum = myAdapter.getItemCount() - Psumc;
                    PsumSave(Psum, username);

                    ItemSave(Psum, newTitle, newTypeing, AddUpTime, newPriority, username, isPu);//保存数据————————————————————test————————
                    Psum++;

                }

                lis.add(new ImageInfor(position, newTitle, AddUpTime, newTypeing, newPriority, isPu));


                PsumSave(Psum, username);
                PsumSave(Psumc, PublicName);

                main_rv.setAdapter(myAdapter);


            }
        });
        builder.setNegativeButton("取消", null);


        builder.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                break;

            case 0:
                SortByTime();
                Toast.makeText(getApplicationContext(), "sort by newTime", Toast.LENGTH_SHORT).show();
                break;

            case 1:
                SortBySetDown();
                Toast.makeText(getApplicationContext(), "sort by set down", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                SortByPriority();
                Toast.makeText(getApplicationContext(), "sort by priority", Toast.LENGTH_SHORT).show();
                break;

            case R.id.calendar:
                Toast.makeText(getApplicationContext(), "打开日历", Toast.LENGTH_SHORT).show();

                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.android.calendar");
                startActivity(LaunchIntent);


                break;


        }
        return true;
    }

    public void PsumSave(int psum, String username) {
        FileOutputStream out;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("Psum" + username, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(psum + "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //---------------------

    public void PsumLoad(String username) {

        FileInputStream in;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            in = openFileInput("Psum" + username);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String s = content.toString();
        int c = Integer.parseInt(s);
        Psum = c;

    }


    //---------------------------

    public void Itemload(List<ImageInfor> lis, int position, int pAdd, String username) {
        FileInputStream in = null;
        BufferedReader reader = null;


        String str = "/data/data/com.example.soulocean1.workdome/files/" + username + "/";
        String Path = str + position + "c" + username;
        File file = new File(Path);


        StringBuffer content = new StringBuffer();
        try {
            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (i == 1) lis.get(position + pAdd).setName(line);
                else if (i == 2) lis.get(position + pAdd).setItemtype(line);
                else if (i == 3) lis.get(position + pAdd).setTime(line);

                else if (i == 4) {

                    int s = Integer.parseInt(line + "");
                    lis.get(position + pAdd).setImageId(s);
                } else if (i == 5) {

                    lis.get(position + pAdd).setisP(line);


                }

                i++;
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String c = content.toString();

    }

    public void ItemSave(int position, String inputTitle, String inputText, String time, int imageID, String username, String isp) {
        FileOutputStream out;
        BufferedWriter writer = null;


        String str = "/data/data/com.example.soulocean1.workdome/files/" + username + "/";
        String Path = str + position + "c" + username;
        File file = new File(Path);


        //File file = new File( getApplicationContext().getFilesDir().getAbsolutePath()+"/"+director);

        try {

            out = new FileOutputStream(file, false);

            writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write(inputTitle + "\n");
            writer.write(inputText + "\n");
            writer.write(time + "\n");

            String StrID = Integer.toString(imageID);
            writer.write(StrID + "\n");

            writer.write(isp + "");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ItemDel(String dataname, String username) {
        //删除文件存储时对应目录下的文件
        File file = new File("/data/data/com.example.soulocean1.workdome/files/" + username + "/" + dataname);
        file.delete();
    }

    /**
     * @param director 文件夹名称
     * @return
     */
    public boolean isFileExist(String director) {
        File file = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + director);
        return file.exists();
    }

    /**
     * create multiple director
     *
     * @param path
     * @return
     */
    public boolean createFile(String director) {
        if (isFileExist(director)) {
            return true;
        } else {
           /* File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + director);
*/
            File file = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + director);
            if (!file.mkdirs()) {
                return false;
            }
            return true;
        }
    }


    public void SortByTime() {

        //排序规则
        Comparator<ImageInfor> comparator = new Comparator<ImageInfor>() {
            public int compare(ImageInfor s1, ImageInfor s2) {
                return s2.getTime().compareTo(s1.getTime());
            }
        };

        //这里就会自动根据规则进行排序
        Collections.sort(list1, comparator);
        main_rv.setAdapter(myAdapter);

    }

    public void SortBySetDown() {

        //排序规则
        Comparator<ImageInfor> comparator = new Comparator<ImageInfor>() {
            public int compare(ImageInfor s1, ImageInfor s2) {
                return s1.getcID() - s2.getcID();
            }
        };

        //这里就会自动根据规则进行排序
        Collections.sort(list1, comparator);
        main_rv.setAdapter(myAdapter);

    }

    public void SortByPriority() {

        //排序规则
        Comparator<ImageInfor> comparator = new Comparator<ImageInfor>() {
            public int compare(ImageInfor s1, ImageInfor s2) {
                return s1.getImageId() - s2.getImageId();
            }
        };

        //这里就会自动根据规则进行排序
        Collections.sort(list1, comparator);
        main_rv.setAdapter(myAdapter);

    }


    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, final Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                Ryear = year;
                Rmonth = monthOfYear + 1;
                Rday = dayOfMonth;

              /*  mCalendar.set(Calendar.YEAR,year);
                mCalendar.set(Calendar.MONTH,monthOfYear+1);
                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
*/

                tv.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");



                /*final int yearc=year;
                final int monthc=monthOfYear+1;
                final int dayc=dayOfMonth;*/

            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, final Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Rday = hourOfDay;
                        Rminute = minute;

                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        mCalendar.set(Calendar.SECOND, 0);


                        tv.setText(hourOfDay + "时" + minute + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    private void remind(final String newTitle, final String data) {

        //case R.id.action_reminder /*2131296280*/:
        final GetNowDate getNowDate = new GetNowDate();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, final int i, final int i2, final int i3) {
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i4, int i5) {


                        ContentValues event = new ContentValues();
                        event.put("title", "" + newTitle);
                        event.put("description", "" + data);
                        // 插入账户
                        event.put("calendar_id", 1);
                        System.out.println("calId: " + 1);


                        android.icu.util.Calendar mCalendar = android.icu.util.Calendar.getInstance();

//                        mCalendar.set(Calendar.HOUR_OF_DAY, 14);
//                        mCalendar.set(Calendar.MINUTE, 53);
                        mCalendar.set(i, i2, i3, i4, i5, 0);

                        long start = mCalendar.getTime().getTime();

//                        mCalendar.set(Calendar.HOUR_OF_DAY, 15);
                        long end = mCalendar.getTime().getTime() + 600000;

                        event.put(CalendarContract.Events.DTSTART, start);
                        event.put(CalendarContract.Events.DTEND, end);

                        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
                        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                        Uri newEvent = getContentResolver().insert(Uri.parse(CALENDAR_EVENT_URL), event);
                        long id = Long.parseLong(newEvent.getLastPathSegment());
                        ContentValues values = new ContentValues();
                        values.put(CalendarContract.Reminders.EVENT_ID, id);
                        values.put(CalendarContract.Reminders.MINUTES, 0);
                        values.put(CalendarContract.Reminders.METHOD, 1);
                        Uri uri = getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
                        if (uri == null) {
                            Toast.makeText(MainActivity.this, "添加提醒失败", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, "已添加提醒⭐", Toast.LENGTH_LONG).show();


                    }

                }, getNowDate.getHour(), getNowDate.getMinute(), true).show();

            }

        }, getNowDate.getYear(), getNowDate.getMonth(), getNowDate.getDay()).show();


    }

    //+_+_+_+_+_+设置提醒+_+_+_+_+
    private void setReminder() {


        AlarmManager alarmService = (AlarmManager) getApplication().getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class).setAction("intent_alarm_log");
        alarmIntent.putExtra("modec", Modec);

        //sendBroadcast(alarmIntent);      //发送广播

        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);//通过广播接收
        alarmService.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), broadcast);


    }


    @Override
    protected void onDestroy() {
        PsumSave(Psum, username);
        PsumSave(Psumc, "publicc");
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "最新顺序");
        menu.add(0, 1, 1, "记录顺序");
        menu.add(0, 2, 2, "优先级顺序");
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void openOptionsMenu() {
        // TODO Auto-generated method stub
        super.openOptionsMenu();
    }

    private void getNetTime() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.qq.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat(
                    "yyyy年MM月dd日 HH时mm分ss秒");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);

            final String format = formatter.format(calendar.getTime());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_time.setText(format);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开一个线程继承Thread
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);

                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = MSG_ONE;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

}


