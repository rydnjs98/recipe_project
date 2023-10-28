package com.example.recipe_project;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter {
    protected static final String TAG = "DataAdapter";

    // TODO : TABLE 이름을 명시해야함
    protected static final String TABLE_NAME = "student";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public List getTableData() {
        try {
            // Table 이름 -> antpool_bitcoin 불러오기
            String sql = "SELECT * FROM " + TABLE_NAME;

            // 모델 넣을 리스트 생성
            List userList = new ArrayList();

            // TODO : 모델 선언
            User user = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                // 칼럼의 마지막까지
                while (mCur.moveToNext()) {

                    // TODO : 커스텀 모델 생성
                    user = new User();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    user.Username(mCur.getString(0));
                    user.Student_id(mCur.getString(1));
                    user.ID(mCur.getString(2));
                    user.Password(mCur.getString(3));
                    user.Email(mCur.getString(4));
                    user.is_professor(mCur.getString(5));
                    user.Time(mCur.getString(6));
                    user.CsCheck(mCur.getString(7));


                    // 리스트에 넣기
                    userList.add(user);
                }

            }
            return userList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"ID"},
                "ID = ? AND Password = ?", new String[]{username, password},
                null, null, null);

        boolean authenticated = cursor.moveToFirst();
        cursor.close();
        db.close();

        return authenticated;
    }

public void setAccount(String username, String password) {
    SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<User> LoginUser = new ArrayList<>();
        User user = null;

        // SQL 쿼리 실행
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE ID = ? AND Password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        // 결과 처리
        while (cursor.moveToNext()) {
            // User 객체 생성 및 정보 저장
            user = new User();
            user.Username(cursor.getString(0));
            user.Student_id(cursor.getString(1));
            user.ID(cursor.getString(2));
            user.Password(cursor.getString(3));
            user.Email(cursor.getString(4));
            user.is_professor(cursor.getString(5));
            user.Time(cursor.getString(6));
            user.CsCheck(cursor.getString(7));
            LoginUser.add(user);
        }

    // 리소스 해제
    cursor.close();
    db.close();

    User.userList = LoginUser;
    }
}

