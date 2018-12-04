package app.betterplate.betterplate.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.betterplate.betterplate.dao.preferences.PrefFavoriteRestaurantsDao;
import app.betterplate.betterplate.data.preferences.PrefFavoriteRestaurant;

@Database(entities = {PrefFavoriteRestaurant.class}, version = 2,
        exportSchema = false)
public abstract class PreferencesDatabase extends RoomDatabase {

    protected static volatile PreferencesDatabase instance;

    public static synchronized PreferencesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public abstract PrefFavoriteRestaurantsDao prefFavoriteRestaurantsDao();

    private static PreferencesDatabase create(final Context context) {
        return (PreferencesDatabase) RoomPreloadUtil.getPreloadedDatabaseBuilder(context, PreferencesDatabase.class, "user_preferences.db", false).allowMainThreadQueries().build();
    }

    @Override
    public void close() {
        super.close();
        instance = null;
    }

}
