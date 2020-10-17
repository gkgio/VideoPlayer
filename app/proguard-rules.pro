
# Moshi
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepattributes *Annotation*

-keepclassmembers enum * { *; }

# LoganSquare
-keep class **$$JsonObjectMapper { *; }
