# Google Mobile Ads (AdMob) — keep SDK entry points reachable after R8.
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.internal.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Play Services common base classes used by the ads SDK.
-keep class com.google.android.gms.common.** { *; }
-dontwarn com.google.android.gms.common.**

# AndroidX / Material — already handled by the optimize defaults, but
# keep ViewBinding-generated classes since they're referenced reflectively.
-keep class com.justmakeapps.gravitydots.databinding.** { *; }

# Kotlin metadata used by reflection in some libs.
-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod
