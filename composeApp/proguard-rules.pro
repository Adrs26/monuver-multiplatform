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

# Keep Compose compiler generated classes
-keep class androidx.activity.ComponentActivity { *; }

# Optional: Keep ViewModels if you're using them
-keep class * extends androidx.lifecycle.ViewModel

# Keep Kotlin metadata
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature, Exceptions, SourceFile, LineNumberTable, LocalVariableTable, LocalVariableTypeTable, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, AnnotationDefault

# If you're using navigation-compose
-keep class androidx.navigation.NavHostController { *; }

# Keep all @Serializable classes
-keep @kotlinx.serialization.Serializable class ** { *; }

# Keep the generated serializers
-keepnames class kotlinx.serialization.** { *; }

# Prevent obfuscation of enum values
-keepclassmembers enum * { *; }

# Retain classes with reflective annotations
-keepclassmembers class ** {
    @kotlinx.serialization.* <fields>;
    @kotlinx.serialization.* <methods>;
}

-dontwarn javax.annotation.Nullable