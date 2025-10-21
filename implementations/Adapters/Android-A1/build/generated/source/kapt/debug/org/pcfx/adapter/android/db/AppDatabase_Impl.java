package org.pcfx.adapter.android.db;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile EventDao _eventDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "c3e973cdf8f4ffb4ca0a3407a2ac6342", "81628e79950c1696b0ea249b30372e75") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exposure_events` (`id` TEXT NOT NULL, `schema` TEXT NOT NULL, `ts` TEXT NOT NULL, `device` TEXT NOT NULL, `adapterId` TEXT NOT NULL, `capabilitiesUsed` TEXT NOT NULL, `sourceJson` TEXT NOT NULL, `contentJson` TEXT NOT NULL, `privacyJson` TEXT NOT NULL, `signature` TEXT NOT NULL, `eventJson` TEXT NOT NULL, `isPosted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `lastAttemptAt` INTEGER, `attemptCount` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c3e973cdf8f4ffb4ca0a3407a2ac6342')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exposure_events`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsExposureEvents = new HashMap<String, TableInfo.Column>(15);
        _columnsExposureEvents.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("schema", new TableInfo.Column("schema", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("ts", new TableInfo.Column("ts", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("device", new TableInfo.Column("device", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("adapterId", new TableInfo.Column("adapterId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("capabilitiesUsed", new TableInfo.Column("capabilitiesUsed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("sourceJson", new TableInfo.Column("sourceJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("contentJson", new TableInfo.Column("contentJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("privacyJson", new TableInfo.Column("privacyJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("signature", new TableInfo.Column("signature", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("eventJson", new TableInfo.Column("eventJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("isPosted", new TableInfo.Column("isPosted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("lastAttemptAt", new TableInfo.Column("lastAttemptAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureEvents.put("attemptCount", new TableInfo.Column("attemptCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExposureEvents = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExposureEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExposureEvents = new TableInfo("exposure_events", _columnsExposureEvents, _foreignKeysExposureEvents, _indicesExposureEvents);
        final TableInfo _existingExposureEvents = TableInfo.read(connection, "exposure_events");
        if (!_infoExposureEvents.equals(_existingExposureEvents)) {
          return new RoomOpenDelegate.ValidationResult(false, "exposure_events(org.pcfx.adapter.android.db.EventEntity).\n"
                  + " Expected:\n" + _infoExposureEvents + "\n"
                  + " Found:\n" + _existingExposureEvents);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exposure_events");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "exposure_events");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }
}
