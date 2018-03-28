# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#---------------------------------基本指令区----------------------------------

# shrink
# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# Uncomment this to preserve the line number information for
# debugging stack traces.
# 抛出异常时保留代码行号，在异常分析中可以方便定位
-keepattributes SourceFile,LineNumberTable
# 保护内部类不被混淆，比如RecyclerView.Adapter中的inner class ViewHolder
-keepattributes InnerClasses
# 保护代码中的Annotation不被混淆，这在JSON实体映射时非常重要，比如FastJson、Gson等
-keepattributes *Annotation*
# 避免混淆泛型，这在JSON实体映射时非常重要，比如FastJson、Gson等
-keepattributes Signature


# optimize
# 代码混淆压缩比，在0和7之间，默认为5，一般不需要改
-optimizationpasses 5
# 指定混淆时采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不改变
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable
#-dontoptimize 如果不想用采用混淆算法，则用-dontoptimize
# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames


# obfuscate
# 有了verbose这句话，混淆后就会生成映射文件
# 包含有类名->混淆后类名的映射关系
# 然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt
# 不打印警告
-ignorewarnings


# preverify
# 不做预校验，preverify是proguard的4个步骤之一，Android不需要preverify，去掉这一步可加快混淆速度
-dontpreverify

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# -----------------------------------END---------------------------------


#---------------------------------默认保留区---------------------------------

-keep public class com.android.vending.licensing.ILicensingService
# 保留了继承自Application以及四大组件的子类
# 因为这些子类，都有可能被外部调用 ExternalApplication call
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
# 保留继承自View的子类，以及类中的构造函数像View构造函数的类
-keep public class * extends android.view.View
-keepclassmembers class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留android.support包中的类
-keep class android.support.*** {*;}
#-keep class android.support.annotation.** {*;}
#-keep interface android.support.annotation.** { *; }
-keep interface android.support.*** {*;}
# 有native方法的类保留
-keepclasseswithmembernames class * {
    native <methods>;
}
# 枚举类中原生方法values和valueOf不能被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 保留Serializable序列化的类中某些成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 对于R（资源）下的所有类及其方法，都不能被混淆
-keep class **.R$* {
 *;
}
# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}

# -----------------------------------END---------------------------------

#---------------------------------实体类---------------------------------
-keep class com.jht.doctor.ui.bean_jht.** {*;}
-keep class com.jht.doctor.ui.bean.** {*;}
# -----------------------------------END---------------------------------


#---------------------------------三方包---------------------------------

#-ignorewarnings
# QuickAdapter
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * { @butterknife.* <fields>;}
-keepclasseswithmembernames class * { @butterknife.* <methods>;}

# Retrofit2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Retain generic type information for use by reflection by converters and adapters.
#-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.**
-keep class retrofit2.** {
 *;
}

# OkHttp3
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**

# Gson
# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent


# RxLifeCycle
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

# sharesdk
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class com.mob.**{*;}
-dontwarn com.mob.**


# 微信支付
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.stat.**
-keep class com.tencent.mm.** {*;}
-keep class com.tencent.wxop.stat.**{*;}

# 支付宝钱包
-dontwarn com.alipay.**
-dontwarn HttpUtils.HttpFetcher
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*


