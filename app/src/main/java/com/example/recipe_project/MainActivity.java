package com.example.recipe_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




    /* void getVal(){
        View root = binding.getRoot();

        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        DataBaseHelper helper = new DataBaseHelper(cscontext.getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();



        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM student" ,null);

        helper.insertData(getTime(),csCheck,SName);
//        helper.insertTimeData(getTime(),"김경호");
//        helper.insertCSData(csCheck,"김경호");
//        if(cursor != null && cursor.moveToFirst())
        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번

            //  val = cursor.getString(7);

            //val = slideshowViewModel.getprla();
//            if(isInserted==true)
//

        }



        cursor.close();
        helper.close();



    } */

   /* public List<User> userList ;

    public void initLoadDB() {

        DataAdapter mDbHelper = new DataAdapter(cscontext.getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        userList = mDbHelper.getTableData();

        // db 닫기
        mDbHelper.close();
    } */

}