package org.t_robop.masatsuna.taskmanagement;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LayoutInflater li;                      //additionのレイアウトを使うための変数
    View view;                              //additionのレイアウトを使うための変数
    AlertDialog.Builder alertdb;            //ダイアログ生成のための変数
    Spinner selectSpinner1, selectSpinner2; //ダイアログのSpinner
    ArrayAdapter adapter1, adapter2;        //Spinnerの値を格納する変数
    static SQLiteDatabase mydb;             //SQLiteを使うための変数
    MySQLiteOpenHelper myhl;                //SQLiteを使うための変数
    ContentValues values;                   //入力データを格納
    EditText et1, et2;                      //EditTextの変数
    Cursor cursor;                          //SQLiteのデータを取り出すための変数
    ListView listView;                      //ListView使用のための変数
    ArrayAdapter<String> adapter;           //ListViewに表示するデータを格納
    SimpleAdapter sAdapter;
    ArrayList<Map<String,Object>> arrayList = new ArrayList<Map<String, Object>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteを使用可能にする
        myhl = new MySQLiteOpenHelper(getApplicationContext());
        mydb = myhl.getWritableDatabase();

        listView = (ListView)findViewById(R.id.listview);

        //SQLite上のデータを表示
        dataShow();

        //ListViewをクリックした時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {

                ListView listView2 = (ListView)parent;
                TextView tv = (TextView)view;
                tv.getText();

                //int dataId = item.getInt(item.getColumnIndex("_id"));

            }
        });

    }

    //追加ボタンの処理
    public void onClick(View v) {

        //additionのレイアウトを取得
        li = LayoutInflater.from(MainActivity.this);
        view = li.inflate(R.layout.addition, null, false);

        et1 = (EditText) view.findViewById(R.id.taskname);
        et2 = (EditText) view.findViewById(R.id.detail);

        //spinnerの設定
        adapter1 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item);
        adapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item);

        //spinnerに数値を登録
        for (int i = 0; i < 12; i++) {
            adapter1.add(i + 1);
        }

        for (int i = 0; i < 31; i++) {
            adapter2.add(i + 1);
        }

        //spinnerのレイアウトを取得
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectSpinner1 = (Spinner) view.findViewById(R.id.spinner1);
        selectSpinner2 = (Spinner) view.findViewById(R.id.spinner2);
        selectSpinner1.setAdapter(adapter1);
        selectSpinner2.setAdapter(adapter2);

        //AlertBuilderのインスタンス生成
        alertdb = new AlertDialog.Builder(this);

        //alertdbのタイトル
        alertdb.setTitle("タスク登録");

        //alertdbにadditionのレイアウトをセット
        alertdb.setView(view);

        //登録ボタン
        alertdb.setPositiveButton(
                "登録",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int iMonth, iDate;  //int型の日付データ
                        String taskName, month, date, detail;   //sqliteのデータを格納

                        //入力フォームからデータを取得
                        month = String.valueOf(selectSpinner1.getSelectedItem());
                        date = String.valueOf(selectSpinner2.getSelectedItem());
                        taskName = et1.getText().toString();
                        detail = et2.getText().toString();

                        //日付データをint型に変換
                        iDate = Integer.parseInt(date);
                        iMonth = Integer.parseInt(month);

                        //SQLiteにデータ登録
                        register(iMonth, iDate, taskName, detail);

                        //SQLiteのデータをListViewに表示
                        dataShow();
                    }
                });

        //キャンセルボタン
        alertdb.setNegativeButton("キャンセル", null);

        //ダイアログ生成
        alertdb.show();

    }

    //SQLiteに入力データを登録
    public void register(int month, int date, String taskName, String detail) {
        values = new ContentValues();
        values.put("month", month);
        values.put("date", date);
        values.put("taskname", taskName);
        values.put("detail", detail);
        mydb.insert("tasktable", null, values);


    }

    //データを取得してListViewに追加
    public void dataShow() {

        Map data = new HashMap();

        //SQLiteデータを取得
        cursor = mydb.query("tasktable", new String[]{"month", "date", "taskname", "detail"}, null, null, null, null, null);

        //adapterにデータを格納
        while (cursor.moveToNext()) {
            data.put("date",cursor.getColumnIndex("month")  + "/" + cursor.getString(cursor.getColumnIndex("date")));
            data.put("taskname",cursor.getColumnIndex("taskname"));
            arrayList.add(data);
        }

        sAdapter = new SimpleAdapter(this, arrayList, R.layout.listviewlayout, new String[] {});

        //ListViewに表示
        listView.setAdapter(adapter);
    }
}
