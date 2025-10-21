package org.pcfx.adapter.android.db;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<EventEntity> __insertAdapterOfEventEntity;

  private final EntityDeleteOrUpdateAdapter<EventEntity> __deleteAdapterOfEventEntity;

  private final EntityDeleteOrUpdateAdapter<EventEntity> __updateAdapterOfEventEntity;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfEventEntity = new EntityInsertAdapter<EventEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exposure_events` (`id`,`schema`,`ts`,`device`,`adapterId`,`capabilitiesUsed`,`sourceJson`,`contentJson`,`privacyJson`,`signature`,`eventJson`,`isPosted`,`createdAt`,`lastAttemptAt`,`attemptCount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final EventEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
        if (entity.getSchema() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getSchema());
        }
        if (entity.getTs() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getTs());
        }
        if (entity.getDevice() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDevice());
        }
        if (entity.getAdapterId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getAdapterId());
        }
        if (entity.getCapabilitiesUsed() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getCapabilitiesUsed());
        }
        if (entity.getSourceJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getSourceJson());
        }
        if (entity.getContentJson() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getContentJson());
        }
        if (entity.getPrivacyJson() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getPrivacyJson());
        }
        if (entity.getSignature() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getSignature());
        }
        if (entity.getEventJson() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getEventJson());
        }
        final int _tmp = entity.isPosted() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getCreatedAt());
        if (entity.getLastAttemptAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastAttemptAt());
        }
        statement.bindLong(15, entity.getAttemptCount());
      }
    };
    this.__deleteAdapterOfEventEntity = new EntityDeleteOrUpdateAdapter<EventEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exposure_events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final EventEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfEventEntity = new EntityDeleteOrUpdateAdapter<EventEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exposure_events` SET `id` = ?,`schema` = ?,`ts` = ?,`device` = ?,`adapterId` = ?,`capabilitiesUsed` = ?,`sourceJson` = ?,`contentJson` = ?,`privacyJson` = ?,`signature` = ?,`eventJson` = ?,`isPosted` = ?,`createdAt` = ?,`lastAttemptAt` = ?,`attemptCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final EventEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
        if (entity.getSchema() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getSchema());
        }
        if (entity.getTs() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getTs());
        }
        if (entity.getDevice() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDevice());
        }
        if (entity.getAdapterId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getAdapterId());
        }
        if (entity.getCapabilitiesUsed() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getCapabilitiesUsed());
        }
        if (entity.getSourceJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getSourceJson());
        }
        if (entity.getContentJson() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getContentJson());
        }
        if (entity.getPrivacyJson() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getPrivacyJson());
        }
        if (entity.getSignature() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getSignature());
        }
        if (entity.getEventJson() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getEventJson());
        }
        final int _tmp = entity.isPosted() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getCreatedAt());
        if (entity.getLastAttemptAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastAttemptAt());
        }
        statement.bindLong(15, entity.getAttemptCount());
        if (entity.getId() == null) {
          statement.bindNull(16);
        } else {
          statement.bindText(16, entity.getId());
        }
      }
    };
  }

  @Override
  public Object insertEvent(final EventEntity event, final Continuation<? super Long> $completion) {
    if (event == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfEventEntity.insertAndReturnId(_connection, event);
    }, $completion);
  }

  @Override
  public Object deleteEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    if (event == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfEventEntity.handle(_connection, event);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object updateEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    if (event == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __updateAdapterOfEventEntity.handle(_connection, event);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getUnpostedEvents(final int limit,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM exposure_events WHERE isPosted = 0 ORDER BY createdAt ASC LIMIT ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSchema = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "schema");
        final int _columnIndexOfTs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ts");
        final int _columnIndexOfDevice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "device");
        final int _columnIndexOfAdapterId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "adapterId");
        final int _columnIndexOfCapabilitiesUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "capabilitiesUsed");
        final int _columnIndexOfSourceJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceJson");
        final int _columnIndexOfContentJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "contentJson");
        final int _columnIndexOfPrivacyJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "privacyJson");
        final int _columnIndexOfSignature = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "signature");
        final int _columnIndexOfEventJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "eventJson");
        final int _columnIndexOfIsPosted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isPosted");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfLastAttemptAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastAttemptAt");
        final int _columnIndexOfAttemptCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "attemptCount");
        final List<EventEntity> _result = new ArrayList<EventEntity>();
        while (_stmt.step()) {
          final EventEntity _item;
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final String _tmpSchema;
          if (_stmt.isNull(_columnIndexOfSchema)) {
            _tmpSchema = null;
          } else {
            _tmpSchema = _stmt.getText(_columnIndexOfSchema);
          }
          final String _tmpTs;
          if (_stmt.isNull(_columnIndexOfTs)) {
            _tmpTs = null;
          } else {
            _tmpTs = _stmt.getText(_columnIndexOfTs);
          }
          final String _tmpDevice;
          if (_stmt.isNull(_columnIndexOfDevice)) {
            _tmpDevice = null;
          } else {
            _tmpDevice = _stmt.getText(_columnIndexOfDevice);
          }
          final String _tmpAdapterId;
          if (_stmt.isNull(_columnIndexOfAdapterId)) {
            _tmpAdapterId = null;
          } else {
            _tmpAdapterId = _stmt.getText(_columnIndexOfAdapterId);
          }
          final String _tmpCapabilitiesUsed;
          if (_stmt.isNull(_columnIndexOfCapabilitiesUsed)) {
            _tmpCapabilitiesUsed = null;
          } else {
            _tmpCapabilitiesUsed = _stmt.getText(_columnIndexOfCapabilitiesUsed);
          }
          final String _tmpSourceJson;
          if (_stmt.isNull(_columnIndexOfSourceJson)) {
            _tmpSourceJson = null;
          } else {
            _tmpSourceJson = _stmt.getText(_columnIndexOfSourceJson);
          }
          final String _tmpContentJson;
          if (_stmt.isNull(_columnIndexOfContentJson)) {
            _tmpContentJson = null;
          } else {
            _tmpContentJson = _stmt.getText(_columnIndexOfContentJson);
          }
          final String _tmpPrivacyJson;
          if (_stmt.isNull(_columnIndexOfPrivacyJson)) {
            _tmpPrivacyJson = null;
          } else {
            _tmpPrivacyJson = _stmt.getText(_columnIndexOfPrivacyJson);
          }
          final String _tmpSignature;
          if (_stmt.isNull(_columnIndexOfSignature)) {
            _tmpSignature = null;
          } else {
            _tmpSignature = _stmt.getText(_columnIndexOfSignature);
          }
          final String _tmpEventJson;
          if (_stmt.isNull(_columnIndexOfEventJson)) {
            _tmpEventJson = null;
          } else {
            _tmpEventJson = _stmt.getText(_columnIndexOfEventJson);
          }
          final boolean _tmpIsPosted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsPosted));
          _tmpIsPosted = _tmp != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final Long _tmpLastAttemptAt;
          if (_stmt.isNull(_columnIndexOfLastAttemptAt)) {
            _tmpLastAttemptAt = null;
          } else {
            _tmpLastAttemptAt = _stmt.getLong(_columnIndexOfLastAttemptAt);
          }
          final int _tmpAttemptCount;
          _tmpAttemptCount = (int) (_stmt.getLong(_columnIndexOfAttemptCount));
          _item = new EventEntity(_tmpId,_tmpSchema,_tmpTs,_tmpDevice,_tmpAdapterId,_tmpCapabilitiesUsed,_tmpSourceJson,_tmpContentJson,_tmpPrivacyJson,_tmpSignature,_tmpEventJson,_tmpIsPosted,_tmpCreatedAt,_tmpLastAttemptAt,_tmpAttemptCount);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getEventById(final String id, final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM exposure_events WHERE id = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, id);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSchema = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "schema");
        final int _columnIndexOfTs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ts");
        final int _columnIndexOfDevice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "device");
        final int _columnIndexOfAdapterId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "adapterId");
        final int _columnIndexOfCapabilitiesUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "capabilitiesUsed");
        final int _columnIndexOfSourceJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceJson");
        final int _columnIndexOfContentJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "contentJson");
        final int _columnIndexOfPrivacyJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "privacyJson");
        final int _columnIndexOfSignature = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "signature");
        final int _columnIndexOfEventJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "eventJson");
        final int _columnIndexOfIsPosted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isPosted");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfLastAttemptAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastAttemptAt");
        final int _columnIndexOfAttemptCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "attemptCount");
        final EventEntity _result;
        if (_stmt.step()) {
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final String _tmpSchema;
          if (_stmt.isNull(_columnIndexOfSchema)) {
            _tmpSchema = null;
          } else {
            _tmpSchema = _stmt.getText(_columnIndexOfSchema);
          }
          final String _tmpTs;
          if (_stmt.isNull(_columnIndexOfTs)) {
            _tmpTs = null;
          } else {
            _tmpTs = _stmt.getText(_columnIndexOfTs);
          }
          final String _tmpDevice;
          if (_stmt.isNull(_columnIndexOfDevice)) {
            _tmpDevice = null;
          } else {
            _tmpDevice = _stmt.getText(_columnIndexOfDevice);
          }
          final String _tmpAdapterId;
          if (_stmt.isNull(_columnIndexOfAdapterId)) {
            _tmpAdapterId = null;
          } else {
            _tmpAdapterId = _stmt.getText(_columnIndexOfAdapterId);
          }
          final String _tmpCapabilitiesUsed;
          if (_stmt.isNull(_columnIndexOfCapabilitiesUsed)) {
            _tmpCapabilitiesUsed = null;
          } else {
            _tmpCapabilitiesUsed = _stmt.getText(_columnIndexOfCapabilitiesUsed);
          }
          final String _tmpSourceJson;
          if (_stmt.isNull(_columnIndexOfSourceJson)) {
            _tmpSourceJson = null;
          } else {
            _tmpSourceJson = _stmt.getText(_columnIndexOfSourceJson);
          }
          final String _tmpContentJson;
          if (_stmt.isNull(_columnIndexOfContentJson)) {
            _tmpContentJson = null;
          } else {
            _tmpContentJson = _stmt.getText(_columnIndexOfContentJson);
          }
          final String _tmpPrivacyJson;
          if (_stmt.isNull(_columnIndexOfPrivacyJson)) {
            _tmpPrivacyJson = null;
          } else {
            _tmpPrivacyJson = _stmt.getText(_columnIndexOfPrivacyJson);
          }
          final String _tmpSignature;
          if (_stmt.isNull(_columnIndexOfSignature)) {
            _tmpSignature = null;
          } else {
            _tmpSignature = _stmt.getText(_columnIndexOfSignature);
          }
          final String _tmpEventJson;
          if (_stmt.isNull(_columnIndexOfEventJson)) {
            _tmpEventJson = null;
          } else {
            _tmpEventJson = _stmt.getText(_columnIndexOfEventJson);
          }
          final boolean _tmpIsPosted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsPosted));
          _tmpIsPosted = _tmp != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final Long _tmpLastAttemptAt;
          if (_stmt.isNull(_columnIndexOfLastAttemptAt)) {
            _tmpLastAttemptAt = null;
          } else {
            _tmpLastAttemptAt = _stmt.getLong(_columnIndexOfLastAttemptAt);
          }
          final int _tmpAttemptCount;
          _tmpAttemptCount = (int) (_stmt.getLong(_columnIndexOfAttemptCount));
          _result = new EventEntity(_tmpId,_tmpSchema,_tmpTs,_tmpDevice,_tmpAdapterId,_tmpCapabilitiesUsed,_tmpSourceJson,_tmpContentJson,_tmpPrivacyJson,_tmpSignature,_tmpEventJson,_tmpIsPosted,_tmpCreatedAt,_tmpLastAttemptAt,_tmpAttemptCount);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getUnpostedCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM exposure_events WHERE isPosted = 0";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getRecentEvents(final int limit,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM exposure_events ORDER BY createdAt DESC LIMIT ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSchema = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "schema");
        final int _columnIndexOfTs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ts");
        final int _columnIndexOfDevice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "device");
        final int _columnIndexOfAdapterId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "adapterId");
        final int _columnIndexOfCapabilitiesUsed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "capabilitiesUsed");
        final int _columnIndexOfSourceJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceJson");
        final int _columnIndexOfContentJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "contentJson");
        final int _columnIndexOfPrivacyJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "privacyJson");
        final int _columnIndexOfSignature = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "signature");
        final int _columnIndexOfEventJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "eventJson");
        final int _columnIndexOfIsPosted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isPosted");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfLastAttemptAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastAttemptAt");
        final int _columnIndexOfAttemptCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "attemptCount");
        final List<EventEntity> _result = new ArrayList<EventEntity>();
        while (_stmt.step()) {
          final EventEntity _item;
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final String _tmpSchema;
          if (_stmt.isNull(_columnIndexOfSchema)) {
            _tmpSchema = null;
          } else {
            _tmpSchema = _stmt.getText(_columnIndexOfSchema);
          }
          final String _tmpTs;
          if (_stmt.isNull(_columnIndexOfTs)) {
            _tmpTs = null;
          } else {
            _tmpTs = _stmt.getText(_columnIndexOfTs);
          }
          final String _tmpDevice;
          if (_stmt.isNull(_columnIndexOfDevice)) {
            _tmpDevice = null;
          } else {
            _tmpDevice = _stmt.getText(_columnIndexOfDevice);
          }
          final String _tmpAdapterId;
          if (_stmt.isNull(_columnIndexOfAdapterId)) {
            _tmpAdapterId = null;
          } else {
            _tmpAdapterId = _stmt.getText(_columnIndexOfAdapterId);
          }
          final String _tmpCapabilitiesUsed;
          if (_stmt.isNull(_columnIndexOfCapabilitiesUsed)) {
            _tmpCapabilitiesUsed = null;
          } else {
            _tmpCapabilitiesUsed = _stmt.getText(_columnIndexOfCapabilitiesUsed);
          }
          final String _tmpSourceJson;
          if (_stmt.isNull(_columnIndexOfSourceJson)) {
            _tmpSourceJson = null;
          } else {
            _tmpSourceJson = _stmt.getText(_columnIndexOfSourceJson);
          }
          final String _tmpContentJson;
          if (_stmt.isNull(_columnIndexOfContentJson)) {
            _tmpContentJson = null;
          } else {
            _tmpContentJson = _stmt.getText(_columnIndexOfContentJson);
          }
          final String _tmpPrivacyJson;
          if (_stmt.isNull(_columnIndexOfPrivacyJson)) {
            _tmpPrivacyJson = null;
          } else {
            _tmpPrivacyJson = _stmt.getText(_columnIndexOfPrivacyJson);
          }
          final String _tmpSignature;
          if (_stmt.isNull(_columnIndexOfSignature)) {
            _tmpSignature = null;
          } else {
            _tmpSignature = _stmt.getText(_columnIndexOfSignature);
          }
          final String _tmpEventJson;
          if (_stmt.isNull(_columnIndexOfEventJson)) {
            _tmpEventJson = null;
          } else {
            _tmpEventJson = _stmt.getText(_columnIndexOfEventJson);
          }
          final boolean _tmpIsPosted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsPosted));
          _tmpIsPosted = _tmp != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final Long _tmpLastAttemptAt;
          if (_stmt.isNull(_columnIndexOfLastAttemptAt)) {
            _tmpLastAttemptAt = null;
          } else {
            _tmpLastAttemptAt = _stmt.getLong(_columnIndexOfLastAttemptAt);
          }
          final int _tmpAttemptCount;
          _tmpAttemptCount = (int) (_stmt.getLong(_columnIndexOfAttemptCount));
          _item = new EventEntity(_tmpId,_tmpSchema,_tmpTs,_tmpDevice,_tmpAdapterId,_tmpCapabilitiesUsed,_tmpSourceJson,_tmpContentJson,_tmpPrivacyJson,_tmpSignature,_tmpEventJson,_tmpIsPosted,_tmpCreatedAt,_tmpLastAttemptAt,_tmpAttemptCount);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deletePostedEventsOlderThan(final long olderThanMs,
      final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM exposure_events WHERE isPosted = 1 AND createdAt < ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, olderThanMs);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
