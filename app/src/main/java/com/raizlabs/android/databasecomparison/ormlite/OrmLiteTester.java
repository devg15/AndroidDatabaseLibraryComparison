package com.raizlabs.android.databasecomparison.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.raizlabs.android.databasecomparison.Generator;
import com.raizlabs.android.databasecomparison.Loader;
import com.raizlabs.android.databasecomparison.MainActivity;
import com.raizlabs.android.databasecomparison.events.LogTestDataEvent;

import java.sql.SQLException;
import java.util.Collection;

import de.greenrobot.event.EventBus;

/**
 * Runs benchmarks for OrmLite
 */
public class OrmLiteTester {
    public static final String FRAMEWORK_NAME = "OrmLite";
    private static final String TAG = OrmLiteTester.class.getName();

    public static void testAddressBooks(Context context) {
        DatabaseHelper dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        try {
            dbHelper.getAddressBookDao().deleteBuilder().delete();
            dbHelper.getAddressItemDao().deleteBuilder().delete();
            dbHelper.getContactDao().deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing DB", e);
        }

        Collection<AddressBook> addressBooks = Generator.createAddressBooks(
                AddressBook.class,
                Contact.class, AddressItem.class,
                MainActivity.ADDRESS_BOOK_COUNT);
        long startTime = System.currentTimeMillis();

        try {
            Dao<AddressBook, Integer> addressBookDao = dbHelper.getAddressBookDao();
            Dao<AddressItem, Integer> addressItemDao = dbHelper.getAddressItemDao();
            Dao<Contact, Integer> contactDao = dbHelper.getContactDao();

            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                // see http://stackoverflow.com/questions/17456321/how-to-insert-bulk-data-in-android-sqlite-database-using-ormlite-efficiently
                for (AddressBook addressBook : addressBooks) {
                    // OrmLite isn't that smart, so we have to insert the root object and then we can add the children
                    addressBookDao.create(addressBook);
                    addressBook.insertNewAddresses(addressBookDao, addressItemDao);
                    addressBook.insertNewContacts(addressBookDao, contactDao);
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            EventBus.getDefault().post(new LogTestDataEvent(startTime, FRAMEWORK_NAME, MainActivity.SAVE_TIME));

            startTime = System.currentTimeMillis();
            addressBooks = addressBookDao.queryForAll();
            Loader.loadAllInnerData(addressBooks);
            EventBus.getDefault().post(new LogTestDataEvent(startTime, FRAMEWORK_NAME, MainActivity.LOAD_TIME));

            // clean out DB for next run
            contactDao.deleteBuilder().delete();
            addressItemDao.deleteBuilder().delete();
            addressBookDao.deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing DB", e);
        }

        OpenHelperManager.releaseHelper();
    }

    public static void testAddressItems(Context context) {
        DatabaseHelper dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        try {
            dbHelper.getSimpleAddressItemDao().deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing DB", e);
        }

        Collection<SimpleAddressItem> simpleAddressItems = Generator.getAddresses(SimpleAddressItem.class, MainActivity.LOOP_COUNT);
        long startTime = System.currentTimeMillis();

        try {
            Dao<SimpleAddressItem, Integer> simpleItemDao = dbHelper.getSimpleAddressItemDao();
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                // see http://stackoverflow.com/questions/17456321/how-to-insert-bulk-data-in-android-sqlite-database-using-ormlite-efficiently
                for (SimpleAddressItem simpleAddressItem : simpleAddressItems) {
                    simpleItemDao.create(simpleAddressItem);
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            EventBus.getDefault().post(new LogTestDataEvent(startTime, FRAMEWORK_NAME, MainActivity.SAVE_TIME));

            startTime = System.currentTimeMillis();
            simpleAddressItems = simpleItemDao.queryForAll();
            EventBus.getDefault().post(new LogTestDataEvent(startTime, FRAMEWORK_NAME, MainActivity.LOAD_TIME));

            // clean out DB for next run
            simpleItemDao.deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing DB", e);
        }

        OpenHelperManager.releaseHelper();
    }
}
