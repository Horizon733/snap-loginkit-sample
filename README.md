<h1 align="center">Snap Login kit Sample</h1>
<p align="center">An android app that demonstrates Snapchat's Login kit integration with Jetpack compose</p>

# 💻 Requirements
------------
- To try out this sample app, you need to use [Android Studio Arctic Fox](https://developer.android.com/studio).
- You can clone this repository.

🧬 Sample
------------

| Project | Sample |
| :--- | --- |
| Android app that demonstrates use of Snapkit's Login kit with new Jetpack Compose and latest Android Studio version.<br><br> What it does? <br><br> • Login <br>• Logout<br>• Jetpack compose UI<br>• Light theme<br>• Lifecycle and Stateful<br>• No XML <br><br>  | <img src="video/login kit demo.gif" width="280" alt="Login kit sample demo">|
|  |  |

# Intructions
- Add your `client id` from Snapkit [Developer Portal](https://kit.snapchat.com/manage/) inside [strings.xml](https://github.com/Horizon733/snap-loginkit-sample/blob/master/app/src/main/res/values/strings.xml)
``` xml
<string name="snapchat_client_id">Your client id</string>
```
- Make sure to change project name in `AndroidManifest.xml`
```xml
<application
...
 >
...
<meta-data
    android:name="com.snapchat.kit.sdk.redirectUrl"
    android:value="loginkitsample://snap-kit/oauth2" />
<activity
    android:name="com.snapchat.kit.sdk.SnapKitActivity"
    android:launchMode="singleTask"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="snap-kit"
            android:path="/oauth2"
            android:scheme="your app name />
    </intent-filter>
</activity>
...
</application>
```

