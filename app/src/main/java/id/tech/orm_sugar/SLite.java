package id.tech.orm_sugar;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */

import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class SLite {
    public static SQLiteDatabase openDatabase(Context context) {
        SQLiteDatabase db;

        db = context.openOrCreateDatabase("db_sms",
                SQLiteDatabase.CREATE_IF_NECESSARY, null);

        db.setVersion(3);
        db.setLocale(Locale.getDefault());
        db.setLockingEnabled(true);
        return db;

    }

    public static void proccessTableSms(SQLiteDatabase db) {
        // delete jika table menu ada
        final String DELETE_TABLE_SMS = "DROP TABLE IF EXISTS tbl_sms ";

        // create ulang table
        final String CREATE_TABLE_SMS = "CREATE TABLE tbl_sms ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "idFeed TEXT," + "titleFeed TEXT,"
                + "urlNewsFeed TEXT," + "urlNewsThumb TEXT,"
                + "urlNewsThumbBig TEXT," + "urlNewsCommentFeed TEXT,"
                + "publishDate TEXT," + "creatorNews TEXT,"
                + "categoryNews TEXT, " + "newsDesc TEXT, " + "content TEXT, "
                + "commentCount TEXT );";

        db.execSQL(DELETE_TABLE_SMS);
        db.execSQL(CREATE_TABLE_SMS);
    }


//    ini yg dipake
    public static void process_CreateIfExist_Table_Sms(SQLiteDatabase db) {
        final String CREATE_TABLE_SMS_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS tbl_sms ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "senderId TEXT,"
                + "bodyMessage TEXT," + "dateReceived TEXT," + "confirmCode TEXT,"
                + "masonId TEXT," + "trxId TEXT," + "qty TEXT," + "retailerId TEXT,"
                + "viewed TEXT );";;

        final String CREATE_TABLE_SMS_INBOX_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS tbl_sms_inbox ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "senderId TEXT,"
                + "bodyMessage TEXT," + "dateReceived TEXT," + "confirmCode TEXT,"
                + "masonId TEXT," + "trxId TEXT," + "qty TEXT," + "retailerId TEXT,"
                + "viewed TEXT );";;

        final String CREATE_TABLE_SMS_NEWS = "CREATE TABLE IF NOT EXISTS tbl_sms_news ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "senderId TEXT,"
                + "bodyMessage TEXT," + "dateReceived TEXT,"
                + "viewed TEXT );";;

        final String CREATE_TABLE_SMS_INSENTIVE = "CREATE TABLE IF NOT EXISTS tbl_sms_insentive ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "senderId TEXT,"
                + "bodyMessage TEXT," + "dateReceived TEXT,"
                + "viewed TEXT );";;

        db.execSQL(CREATE_TABLE_SMS_IF_NOT_EXIST);
        db.execSQL(CREATE_TABLE_SMS_INBOX_IF_NOT_EXIST);
        db.execSQL(CREATE_TABLE_SMS_NEWS);
        db.execSQL(CREATE_TABLE_SMS_INSENTIVE);
    }

    public static void process_Insert_Sms(SQLiteDatabase db, String senderId, String bodyMessage,
                                          String date){
        final String INSERT_SMS = "INSERT INTO tbl_sms ("
                + "senderId ,"
                + "bodyMessage ," + "dateReceived ,"
                + "viewed ) VALUES ("
                + senderId + ","
                + bodyMessage + ","
                + date + ","
                + "0" + ");";

        db.execSQL(INSERT_SMS);
    }

    public static void process_Drop_Table_Sms(SQLiteDatabase db){
        final String DELETE_TABLE_SMS = "DROP TABLE IF EXISTS tbl_sms ";
        db.execSQL(DELETE_TABLE_SMS);
    }
}
