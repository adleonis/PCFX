package org.pcfx.adapter.android.validation;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001\u0014B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0015\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u000fH\u0002\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0015"}, d2 = {"Lorg/pcfx/adapter/android/validation/EventValidator;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "schema", "error/NonExistentClass", "getSchema", "()Lerror/NonExistentClass;", "schema$delegate", "Lkotlin/Lazy;", "validateEvent", "Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult;", "eventJson", "", "validateEventStructure", "loadSchema", "assetFileName", "(Ljava/lang/String;)Lerror/NonExistentClass;", "ValidationResult", "Android-A1_debug"})
public final class EventValidator {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy schema$delegate = null;
    
    public EventValidator(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final error.NonExistentClass getSchema() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.validation.EventValidator.ValidationResult validateEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String eventJson) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.validation.EventValidator.ValidationResult validateEventStructure(@org.jetbrains.annotations.NotNull()
    java.lang.String eventJson) {
        return null;
    }
    
    private final error.NonExistentClass loadSchema(java.lang.String assetFileName) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007\u00a8\u0006\b"}, d2 = {"Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult;", "", "<init>", "()V", "Valid", "Invalid", "Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult$Invalid;", "Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult$Valid;", "Android-A1_debug"})
    public static abstract class ValidationResult {
        
        private ValidationResult() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult$Invalid;", "Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult;", "message", "", "<init>", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Android-A1_debug"})
        public static final class Invalid extends org.pcfx.adapter.android.validation.EventValidator.ValidationResult {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            
            public Invalid(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final org.pcfx.adapter.android.validation.EventValidator.ValidationResult.Invalid copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
                return null;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult$Valid;", "Lorg/pcfx/adapter/android/validation/EventValidator$ValidationResult;", "<init>", "()V", "Android-A1_debug"})
        public static final class Valid extends org.pcfx.adapter.android.validation.EventValidator.ValidationResult {
            @org.jetbrains.annotations.NotNull()
            public static final org.pcfx.adapter.android.validation.EventValidator.ValidationResult.Valid INSTANCE = null;
            
            private Valid() {
            }
        }
    }
}