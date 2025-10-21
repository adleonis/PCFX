package org.pcfx.adapter.android.db;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\t\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u001e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u0019\u00c0\u0006\u0003"}, d2 = {"Lorg/pcfx/adapter/android/db/EventDao;", "", "insertEvent", "", "event", "Lorg/pcfx/adapter/android/db/EventEntity;", "(Lorg/pcfx/adapter/android/db/EventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEvent", "", "deleteEvent", "getUnpostedEvents", "", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUnpostedCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePostedEventsOlderThan", "olderThanMs", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentEvents", "Android-A1_debug"})
@androidx.room.Dao()
public abstract interface EventDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.db.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEvent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.db.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEvent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.db.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exposure_events WHERE isPosted = 0 ORDER BY createdAt ASC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnpostedEvents(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<org.pcfx.adapter.android.db.EventEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exposure_events WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEventById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.pcfx.adapter.android.db.EventEntity> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM exposure_events WHERE isPosted = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnpostedCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "DELETE FROM exposure_events WHERE isPosted = 1 AND createdAt < :olderThanMs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePostedEventsOlderThan(long olderThanMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exposure_events ORDER BY createdAt DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentEvents(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<org.pcfx.adapter.android.db.EventEntity>> $completion);
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}