package app.betterplate.betterplate.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class RoomPreloadUtil {

    private static String INITIALIZED_PREFERENCE_NAME = "_initialized";

    public static RoomDatabase.Builder getPreloadedDatabaseBuilder(Context context, Class clazz, String databaseName, boolean reinitializeOnStart) {

        String actualInitializedPreferenceName = databaseName.concat(INITIALIZED_PREFERENCE_NAME);

        SharedPreferences initializedPreference = PreferenceManager.getDefaultSharedPreferences(context);

        if (reinitializeOnStart && context.getDatabasePath(databaseName) != null) {
            context.getDatabasePath(databaseName).delete();
            initializedPreference.edit().putBoolean(actualInitializedPreferenceName, false).commit();
        }

        if (!initializedPreference.getBoolean(actualInitializedPreferenceName, false)) {
            SQLiteAssetHelper assetHelper = new SQLiteAssetHelper(context, databaseName, null, null, 1);
            assetHelper.getWritableDatabase().close();
            initializedPreference.edit().putBoolean(actualInitializedPreferenceName, true).apply();
        }

        return Room.databaseBuilder(context, clazz, databaseName).addMigrations(new Migration(1,2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {

            }
        });
    }

}

