# ScreenCaptor
Simple Android library to capture screenshots deterministically

[![Build Status](https://travis-ci.org/wealthfront/screencaptor.svg?branch=master)](https://travis-ci.org/wealthfront/screencaptor)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/screencaptor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/screencaptor)

## Features
* Capture a screenshot of any view in your integration tests
* Dynamically hide views from your screenshots (Eg. Dates, Account Value, etc)
* Use `ViewMutators` to mutate various types of views as needed
* Use `ViewModifiers` to allow supplying sample data for screenshots (which will be reset after the screeenshot)

## Download

Add this dependency in your build.gradle:

```groovy
implementation 'com.wealthfront:screencaptor:1.1.0'
```

## How do I capture a screenshot?
The simplest way to capture a screenshot is to just pass in the view to be screenshotted with a name for the screenshot when you want to take one.
```kotlin
ScreenCaptor.takeScreenshot(view = rootView, screenshotName = "my_favorite_screenshot")
```

There are other configurations that you can pass in as well,

* `screenshotNameSuffix` - for any suffix that you want to attach to your screenshot
* `viewIdsToExclude` - takes in a set of ids to exclude from the screenshot
* `viewModifiers` - takes in a set of data modifiers which will be processed by the [viewDataProcessor]
* `viewDataProcessor` - allows you to pass in a custom view modification processor.
* `viewMutators` - allows you to mutate the subclasses of views in any particular way (Hides scrollbars and cursors by default)
* `screenshotDirectory` - specify which directory you want to save the screenshot in (sdcard/screenshots/ by default)
* `screenshotFormat` - whether the format needs to be JPEG, PNG or WEBP (PNG by default)
* `screenshotQuality` - specify the quality of the screenshot (Best quality by default)

**Note**: You can disable the default mutators by passing in an empty set for the `viewMutators` argument in the `takeScreenshot` method.

Here's an example of a complex screencaptor setup:

```kotlin
ScreenCaptor.takeScreenshot(
  view = rootView,
  screenshotName = "my_favorite_screenshot",
  screenshotNameSuffix = "latest",
  viewModifiers = = setOf(TextViewDataModifier(R.id.date, "01/01/2000"))
  viewIdsToExclude = setOf(R.id.date, R.id.account_value),
  screenshotFormat = JPEG,
  screenshotQuality = BEST
)
```

## Why is screencaptor more deterministic?
When researching a way to properly take screenshots of a select view deterministically, we couldn't really find a simple framework that did just that and did it well. So, we went ahead and developed this framework for taking screenshots as deterministically as much as possible.

### Manually drawing on a Canvas

Instead of using AndroidX's implementation of screenshot which utilizes the UiAutomator to take a screenshot, we manually draw the screen's content onto a bitmap canvas. This is a much more direct approach and ultimately leads to the screenshot process being the most native and close to the metal.

### Handling Dynamic data within views

This is a pretty common case that we run into where we have a dynamic piece of data displayed on the view that might change with every run of the test. The classic example is any date that gets displayed on the screen. And for this, we have a way of disabling/hiding these views when we take the screenshot using the  **ViewVisibilityModifier**. This turns the views to be  **INVISIBLE** right before taking the screenshot and turns the views to their initial state after the screenshot is taken.

### Disabling cursor and scrollbars

Cursors and scrollbars show up in EditTexts and ScrollViews in an inconsistent manner and having that enabled means that it might make our screenshots more flaky. So to address that issue, we have the  **ViewTreeMutator** which walks through the view hierarchy and disables scrollbars and cursors whenever applicable.

These techniques allow our screenshot framework to be free of flakiness as much as possible and makes it pretty extensible.

## How we use screencaptor
We use screencaptor to capture screenshots in various places in our app when running our integration tests and compare the screenshot on master with any new PR that gets created. This helps us narrow down on any visual changes that happens with our app. Like so.

<img src="/.github/mismatch.png" width="450"/>

This helps us address a class of bugs before our app rolls out to production :)

## License
```
Copyright 2020 Wealthfront, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
