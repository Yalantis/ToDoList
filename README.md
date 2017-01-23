# ToDoList

[![License](http://img.shields.io/badge/license-MIT-green.svg?style=flat)]()
[![](https://jitpack.io/v/yalantis/todolist.svg)](https://jitpack.io/#yalantis/todolist)
[![Yalantis](https://raw.githubusercontent.com/Yalantis/PullToRefresh/develop/PullToRefreshDemo/Resources/badge_dark.png)](https://yalantis.com/?utm_source=github)

<a href='https://play.google.com/store/apps/details?id=com.yalantis.beamazingtoday&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height="70" width="180"/></a>

[Live DEMO on appetize.io](https://appetize.io/app/c72udvwr5h7rgrbm23y2qrbbfc)

Check this [project on dribbble](https://dribbble.com/shots/2589690-Be-amazing-today)

Read how we did it [on our blog](https://yalantis.com/blog/how-we-used-micro-transitions-for-smooth-android-to-do-list-animations/)

<img src="content_shot_to-do_dribbble.gif"/>

##Requirements
- Android SDK 16+

##Usage

Add to your root build.gradle:
```Groovy
allprojects {
	repositories {
	...
	maven { url "https://jitpack.io" }
	}
}
```

Add the dependency:
```Groovy
dependencies {
	compile 'com.github.yalantis:todolist:v1.0.1'
}
```

## How to use this library

Add ```BatRecyclerView``` to your xml layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/gradient"
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?android:attr/actionBarSize"
        android:background="@android:color/transparent"
        app:logo="@drawable/ic_menu">

        <TextView
            android:id="@+id/text_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="14 Feb, 2016"
            android:textColor="@android:color/white"
            android:textSize="20sp" />

    </android.support.v7.widget.Toolbar>

    <com.yalantis.beamazingtoday.ui.widget.BatRecyclerView
        android:id="@+id/bat_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center_horizontal"
        app:add_button_color="@drawable/selector_button_add"
        app:hint="@string/str_add_goal"
        app:radio_button_res="@drawable/selector_radio_button" />

</LinearLayout>
```

Create ```BatListener```

```java
private BatListener mListener = new BatListener() {
        @Override
        public void add(String string) {
            mGoals.add(0, new Goal(string));
            mAdapter.notify(AnimationType.ADD, 0);
        }

        @Override
        public void delete(int position) {
            mGoals.remove(position);
            mAdapter.notify(AnimationType.REMOVE, position);
        }

        @Override
        public void move(int from, int to) {
            if (from >= 0 && to >= 0) {
                
                //if you use 'BatItemAnimator'
                mAnimator.setPosition(to);
                
                BatModel model = mGoals.get(from);
                mGoals.remove(model);
                mGoals.add(to, model);
                mAdapter.notify(AnimationType.MOVE, from, to);

                if (from == 0 || to == 0) {
                    mRecyclerView.getView().scrollToPosition(Math.min(from, to));
                }
            }
        }
    };
```

Create ```BatAdapter```. Pass to its constructor list of models and ```BatListener```. Note that your model should implement ```BatModel``` interface

```java
mAdapter = new BatAdapter(mGoals = new ArrayList<BatModel>() {{
      add(new Goal("first"));
      add(new Goal("second"));
      add(new Goal("third"));
      add(new Goal("fourth"));
      add(new Goal("fifth"));
      add(new Goal("sixth"));
      add(new Goal("seventh"));
      add(new Goal("eighth"));
      add(new Goal("ninth"));
      add(new Goal("tenth"));
}}, mListener);
        
mAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onClick(BatModel item, int position) {
            Toast.makeText(ExampleActivity.this, item.getText(), Toast.LENGTH_SHORT).show();
      }
});
```

Initialize your recycler view

```java
mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
mRecyclerView.getView().setAdapter(mAdapter);

ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
mRecyclerView.setAddItemListener(mListener);
```

## Extra

You can use ```BatItemAnimator``` to animate list items just like in our demo:

```java
mAnimator = new BatItemAnimator();
mAdapter = new BatAdapter(mGoals = new ArrayList<BatModel>() {{
            add(new Goal("first"));
            add(new Goal("second"));
            add(new Goal("third"));
            add(new Goal("fourth"));
            add(new Goal("fifth"));
            add(new Goal("sixth"));
            add(new Goal("seventh"));
            add(new Goal("eighth"));
            add(new Goal("ninth"));
            add(new Goal("tenth"));
}}, mListener, mAnimator);
mRecyclerView.getView().setItemAnimator(mAnimator);
```
You need to pass the animator instance to ```BatRecyclerView``` and to ```BatAdapter```.
Also it's necessary to pass the position of the moved item in ```move()``` callback:

```java
@Override
public void move(int from, int to) {
     if (from >= 0 && to >= 0) {
           mAnimator.setPosition(to);
           BatModel model = mGoals.get(from);
           mGoals.remove(model);
           mGoals.add(to, model);
           mAdapter.notify(AnimationType.MOVE, from, to);

           if (from == 0 || to == 0) {
                 mRecyclerView.getView().scrollToPosition(Math.min(from, to));
           }
     }
}
```
For more usage examples please review sample app

## Changelog

### Version: 1.0.1

* Cursor fixed. 
* ```cursor_drawable``` attribute and ```setCursorDrawable(@DrawableRes int res)``` method added. 
	Should be used instead of ```cursor_color``` attribute and ```setCursorColor(@ColorInt int color)``` method 		respectively

## Let us know!

We’d be really happy if you could send us links to your projects where you use our component. Just send an email to github@yalantis.com And do let us know if you have any questions or suggestion regarding the animation. 

P.S. We’re going to publish more awesomeness wrapped in code and a tutorial on how to make UI for iOS (Android) better than better. Stay tuned!

## License

	The MIT License (MIT)

	Copyright © 2017 Yalantis, https://yalantis.com

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
