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

# Application classes that will be serialized/deserialized over Gson
-keep class gr.cpaleop.core.data.model** { *; }
-keep class gr.cpaleop.profile.data.model** { *; }
-keep class gr.cpaleop.profile.domain.entities.Personal { *; }
-keep class gr.cpaleop.profile.domain.entities.Social { *; }
-keep class gr.cpaleop.dashboard.data.model** { *; }
-keep class gr.cpaleop.announcement.data.model** { *; }
-keep class gr.cpaleop.announcements.data.model** { *; }
-keep class gr.cpaleop.categoryfilter.data.model** { *; }
-keep class gr.cpaleop.teithe_apps.UserPreferences  { *; }
-keep class gr.cpaleop.teithe_apps.TokenPreferences  { *; }
-keep class gr.cpaleop.teithe_apps.DocumentPreviewPreferences  { *; }
-keep class gr.cpaleop.teithe_apps.DocumentSortPreferences  { *; }
-keep class gr.cpaleop.teithe_apps.SystemPreferences  { *; }
-keep class com.google.android.material.tabs.** {*;}

-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable