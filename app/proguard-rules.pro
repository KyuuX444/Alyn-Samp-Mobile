-keep class com.nvidia.devtech.** { *; }
-keep class com.rockstargames.gtasa.** { *; }
-keep class com.wardrumstudios.utils.** { *; }

-keep class ro.alynsampmobile.game.** { *; }
-keep interface ro.alynsampmobile.game.** { *; }
-keep class ro.alynsampmobile.launcher.** { *; }
-keep interface ro.alynsampmobile.launcher.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-dontwarn com.bumptech.glide.**

# AppLovin
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

# PRDownloader
-keep class com.downloader.** { *; }
-dontwarn com.downloader.**

# EasyPermissions
-keep class pub.devrel.easypermissions.** { *; }

# for minify
-dontwarn javax.servlet.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okhttp3.**
-dontwarn okio.**
