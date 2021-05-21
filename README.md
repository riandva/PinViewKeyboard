# PinViewKeyboard
[![Release](https://jitpack.io/v/Mathvediz/PinViewKeyboard.svg)](https://jitpack.io/#Mathvediz/PinViewKeyboard)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)

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
	implementation 'com.github.Mathvediz:PinViewKeyboard:v1.0.1'
}
```
Sync the gradle and that's it! :+1:

## How to use

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
