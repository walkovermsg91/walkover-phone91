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
# GSON
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**


-keep class com.phone91.sdk.model.ParamListObject{ *; }
-keep class com.phone91.sdk.model.ParamObject{ *; }
-keepclassmembers class com.phone91.sdk.model.ParamListObject{ <fields>; }
-keepclassmembers class com.phone91.sdk.model.ParamObject{ <fields>; }
-keepclassmembers class com.phone91.sdk.model.Assigns{<fields>;}
-keepclassmembers class com.phone91.sdk.model.Channel{<fields>; }
-keepclassmembers class com.phone91.sdk.model.ChannelObject{ *; }
-keepclassmembers class com.phone91.sdk.model.ChannelListObject{ *; }
-keep class com.phone91.sdk.model.WidgetObject { *; }
-keep class com.phone91.sdk.model.TeamObject { *; }
-keep class com.phone91.sdk.model.AgentObject { *; }
-keep class com.phone91.sdk.model.CallConnectionSignal { *; }
-keep class com.phone91.sdk.model.CallStatusSignal { *; }
-keep class com.phone91.sdk.model.ChannelListObject { *; }
-keep class com.phone91.sdk.model.Channel { *; }
-keep class com.phone91.sdk.model.ChannelObject{ *; }
-keep class com.phone91.sdk.model.Assigns{ *; }
-keep class com.phone91.sdk.model.ChatObject { *; }
-keep class com.phone91.sdk.model.ClientObject{ *; }
-keep class com.phone91.sdk.model.LoginObject{ *; }
-keep class com.phone91.sdk.model.MessageObject{ *; }
-keep class com.phone91.sdk.model.MusicObject{ *; }
-keep class com.phone91.sdk.model.NotificationObject{ *; }
-keep class com.phone91.sdk.model.PnGcmObject{ *; }
-keep class com.phone91.sdk.model.DataObject{ *; }
-keep class com.phone91.sdk.model.PubNubObject{ *; }
-keep class com.phone91.sdk.model.RoomObject{ *; }
-keep class com.phone91.sdk.model.WidgetDBObject{ *; }


# POJOs used with GSON
# The variable names are JSON key values and should not be obfuscated


-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# React Native

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.proguard.annotations.DoNotStrip class *
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.proguard.annotations.DoNotStrip *;
    @com.facebook.common.internal.DoNotStrip *;
}

-keepclassmembers @com.facebook.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}

-keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
-keep class * extends com.facebook.react.bridge.NativeModule { *; }
-keepclassmembers,includedescriptorclasses class * { native <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.UIProp <fields>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactProp <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactPropGroup <methods>; }

-dontwarn com.facebook.react.**
-keep,includedescriptorclasses class com.facebook.react.bridge.** { *; }

# okhttp

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# okio

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class okio.** { *; }
-dontwarn okio.**

# WebRTC

-keep class org.webrtc.** { *; }
-dontwarn org.chromium.build.BuildHooksAndroid

# Jisti Meet SDK

-keep class org.jitsi.meet.** { *; }
-keep class org.jitsi.meet.sdk.** { *; }

# We added the following when we switched minifyEnabled on. Probably because we
# ran the app and hit problems...

-keep class com.facebook.react.bridge.CatalystInstanceImpl { *; }
-keep class com.facebook.react.bridge.ExecutorToken { *; }
-keep class com.facebook.react.bridge.JavaScriptExecutor { *; }
-keep class com.facebook.react.bridge.ModuleRegistryHolder { *; }
-keep class com.facebook.react.bridge.ReadableType { *; }
-keep class com.facebook.react.bridge.queue.NativeRunnable { *; }
-keep class com.facebook.react.devsupport.** { *; }

-dontwarn com.facebook.react.devsupport.**
-dontwarn com.google.appengine.**
-dontwarn com.squareup.okhttp.**
-dontwarn javax.servlet.**

# ^^^ We added the above when we switched minifyEnabled on.

# Rule to avoid build errors related to SVGs.
-keep public class com.horcrux.svg.** {*;}
