#混淆添加
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes SourceFile,LineNumberTable

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclasseswithmembernames class * {native <methods>;}

-keepclasseswithmembernames class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-dontwarn com.jkframework.**
-keep class com.jkframework.algorithm.JKAnalysis{ *; }
-keep class com.jkframework.algorithm.JKConvert{ *; }
-keep class com.jkframework.algorithm.JKFile{ *; }
-keep class com.jkframework.algorithm.JKPicture { *; }
-keep class com.jkframework.config.JKSystem{ *; }
-keep class com.jkframework.debug.JKDebug{ *; }

-dontwarn android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#pinyin4j
-dontwarn com.hp.hpl.sparta.**
-keep class com.hp.hpl.sparta.** {*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.** {*;}

#androidannotations
-dontwarn org.androidannotations.**
-dontwarn com.sun.codemodel.util.SingleByteEncoder

#SlidingMenu
-dontwarn com.jeremyfeinstein.slidingmenu.**

#trinea-android-common
-keep class cn.trinea.android.** { *; }
-keepclassmembers class cn.trinea.android.** { *; }
-dontwarn cn.trinea.android.**

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#okhttp
-dontwarn okio.**
-dontwarn okhttp3.internal.huc.**

#PersistentCookieJar
-dontwarn com.franmontiel.persistentcookiejar.**
-keep class com.franmontiel.persistentcookiejar.**
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#RxCache
-dontwarn io.rx_cache.internal.**
-keepclassmembers enum io.rx_cache.Source { *; }

#guava
-dontoptimize
-dontobfuscate
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}
-dontwarn com.google.common.**

#joda-time
-dontwarn org.joda.time.**

#retrofit2
-dontwarn retrofit2.**


#项目
-dontwarn com.yysp.ecandroid.**
-keep class com.yysp.ecandroid.data.model.** { *; }
-keep class com.yysp.ecandroid.data.bean.** { *; }
-keep class com.yysp.ecandroid.data.response.** { *; }
-keep class com.yysp.ecandroid.net.** { *; }
-keep class com.yysp.ecandroid.persent.** { *; }
-keep class com.yysp.ecandroid.test.ECReflect { *; }

#腾讯bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}