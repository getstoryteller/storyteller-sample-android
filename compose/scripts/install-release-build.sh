TODAYS_DATE=$(date +%Y%m%d)
APK_RELEASE="Showcase-apk-nightly-release-$TODAYS_DATE.apk"
APK_DEBUG="Showcase-apk-nightly-debug-$TODAYS_DATE.apk"
ZIP_NAME="Showcase-apk-nightly-$TODAYS_DATE.zip"

if [ "$1" == "zip" ]; then
    ./gradlew :app:assembleRelease
    ./gradlew :app:assembleDebug
    cp "./app/build/outputs/apk/release/app-release.apk" "./$APK_RELEASE"
    cp "./app/build/outputs/apk/debug/app-debug.apk" "./$APK_DEBUG"
    zip "./$ZIP_NAME" "./$APK_RELEASE" "./$APK_DEBUG"
    rm "./$APK_RELEASE"
    rm "./$APK_DEBUG"
    open .
else
    ./gradlew :app:assembleRelease installRelease
    adb shell am start -n "com.getstoryteller.storytellershowcaseapp/com.getstoryteller.storytellershowcaseapp.ui.features.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
fi
