# AndroidGhost
Since JellyBean (API Level 16), `android.widget.Toast` will not show when check off "Show Notification" on app info settings. Ghost shows message like the Toast without depending on settings.

Gradle
--------

```groovy

repositories {
    jcenter()
}

 (...)

dependencies {
    compile 'com.garlicg:ghost:0.9.0'
}

```

Usage
--------

##### 1. Add `ghostFrameBackground` item into your app theme.
```xml

    <style name="AppTheme" parent="Theme.AppCompat">
        <item name="ghostFrameBackground">@android:drawable/toast_frame</item>
    </style>

```

##### 2. Call `Ghost` in the same way to call `android.widget.Toast`. 

```java

Ghost.makeText(context, "Ghost!", Ghost.LENGTH_SHORT).show();

```

This is shortend way to call. (I prefer it)

```java

Ghost.show(context, "Ghost!")
Ghost.showLong(context, "Ghost!")

```

API Level
-------
Ghost works on API level 9+.

Credits
-------
The code is based on android.widget.Toast from Android 5.0.1 source code.

License
-------

    Copyright 2015 Takahiro GOTO

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

