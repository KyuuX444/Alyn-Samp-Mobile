-keep class com.nvidia.devtech.** { *; }
-keep class com.rockstargames.gtasa.** { *; }
-keep class com.wardrumstudios.utils.** { *; }

-keep class ro.alynsampmobile.game.** { *; }
-keep class ro.alynsampmobile.launcher.utils.SignatureChecker { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# for minify
-dontwarn javax.servlet.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okhttp3.**
-dontwarn okio.**
