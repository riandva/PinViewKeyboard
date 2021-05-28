# PinViewKeyboard
[![Release](https://jitpack.io/v/Mathvediz/PinViewKeyboard.svg)](https://jitpack.io/#Mathvediz/PinViewKeyboard)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/8e7ada69c2a34014a64e6d91757649bb)](https://www.codacy.com/gh/Mathvediz/PinViewKeyboard/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mathvediz/PinViewKeyboard&amp;utm_campaign=Badge_Grade)

PinViewKeyboard library for android


## How to install

Add this in your root build.gradle file at the end of repositories:
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency :
```java
dependencies {
	implementation 'com.github.Mathvediz:PinViewKeyboard:v1.0.2'
}
```
Sync the gradle and that's it! :+1:

## Screenshot
![Screenshot 1](/images/1.png)
![Screenshot 2](/images/2.png)


## How to use

### xml
```xml
        <com.riandivayana.pinviewkeyboard.PinViewKeyboard
            android:id="@+id/pin_view"
            android:layout_marginTop="@dimen/base_margin"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:pinCount="5"/>

```

### Kotlin

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.pinView.addOnPinViewChangeListener(object : OnPinViewChangeListener {
        override fun onPinChange(data: String) {
            //your code here
        }

        override fun onPinReady(data: String) {
            //when pin fully inputted
        }

        override fun onPinNotReady() {
            //when pin not fully inputted
        }

    })
    //available function
    binding.pinView.getPin()
    binding.pinView.resetPin()
```
