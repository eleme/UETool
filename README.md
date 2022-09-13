UETool [![GitHub release](https://img.shields.io/github/release/eleme/UETool.svg?style=social)](https://github.com/eleme/UETool/releases) [![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html) [![license](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/eleme/UETool/blob/master/LICENSE)
======

![](https://github.com/eleme/UETool/blob/master/art/uet_banner.jpeg)

## INTRODUCTION

[中文版](https://github.com/eleme/UETool/blob/master/README_zh.md)

UETool is a debug tool for anyone who needs to show and edit the attributes of user interface views on mobile devices. It works on Activity/Fragment/Dialog/PopupWindow or any other view.

Presently, UETool provides the following functionalities:

- Move any view on the screen (selecting view repeatedly will select its parent's view)
- Show/edit normal view's attributes such as TextView's text, textSize, textColor etc.
- Show two view's relative positions
- Show grid for checking view alignment
- Support Android P
- Show view's current Fragment
- Show activity's Fragment tree
- Show view's view holder name if it exist
- Easily customize any view's attributes you want simply, such as business parameters

- If you are using Fresco's DraweeView, UETool shows more properties like ImageURI, PlaceHolderImage, CornerRadius etc.
- If the view selected by UETool isn’t what you want, you can check ValidViews to choose which target view you want

## UETool's Effects:

<div>
<img width="270" height="480" src="https://github.com/eleme/UETool/blob/master/art/move_view.gif"/>

<img width="270" height="480" src="https://github.com/eleme/UETool/blob/master/art/show_image_uri.gif"/>

<br/>

<img width="270" height="480" src="https://github.com/eleme/UETool/blob/master/art/relative_position.gif"/>

<img width="270" height="480" src="https://github.com/eleme/UETool/blob/master/art/show_gridding.png"/>
</div>


## ATTRIBUTE LIST

| Attribute | Value Sample | Editable |
| --- | --- | --- |
| Move | if you checked it, you can move view easily |  |
| ValidViews | sometimes target view which UETool offered isn’t you want, you can check it and choose which you want |  |
| Class | android.widget.LinearLayout |  |
| Id | 0x7f0d009c |  |
| ResName | btn |  |
| Clickble | TRUE |  |
| Focoused | FALSE |  |
| Width(dp) | 107 | YES |
| Height(dp) | 19 | YES |
| Alpha | 1.0 |  |
| PaddingLeft(dp) | 10 | YES |
| PaddingRight(dp) | 10 | YES |
| PaddingTop(dp) | 10 | YES |
| PaddingBottom(dp) | 10 | YES |
| Background | #90000000 <br/> #FF8F8F8F -> #FF688FDB <br/> [PICTURE] 300px*300px |  |
| **TextView** |  |  |
| Text | Hello World | YES |
| TextSize(sp) | 14 | YES |
| TextColor | #DE000000 | YES |
| IsBold | TRUE | YES |
| SpanBitmap | [PICTURE] 72px*39px |  |
| DrawableLeft | [PICTURE] 51px*51px |  |
| DrawableRight | [PICTURE] 36px*36px |  |
| DrawableTop | [PICTURE] 36px*36px |  |
| DrawableBottom | [PICTURE] 36px*36px |  |
| **ImageView** |  |  |
| Bitmap | [PICTURE] 144px*144px |  |
| ScaleType | CENTER_CROP |  |
| **DraweeView** |  |  |
| CornerRadius | 2dp |  |
| ImageURI | https://avatars2.githubusercontent.com/u/1201438?s=200&v=4 |  |
| ActualScaleType | CENTER_CROP |  |
| IsSupportAnimation | TRUE |  |
| PlaceHolderImage | [PICTURE] 300px*300px |  |
|  |  |  |


## HOW TO USE 

### Installation

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
  debugImplementation 'com.github.eleme.UETool:uetool:1.3.4'
  debugImplementation 'com.github.eleme.UETool:uetool-base:1.3.4'
  releaseImplementation 'com.github.eleme.UETool:uetool-no-op:1.3.4'
  
  // if you want to show more attrs about Fresco's DraweeView
  debugImplementation 'com.github.eleme.UETool:uetool-fresco:1.3.4'
}
```

### Usage

#### Show floating window

```java
UETool.showUETMenu();

UETool.showUETMenu(int y);
```

#### Dismiss floating window

```java
UETool.dismissUETMenu();
```

#### Filter out unwanted views from selection

```java
UETool.putFilterClass(Class viewClazz);

UETool.putFilterClass(String viewClassName);
```

#### Customize your view

```java

// step 1, implement IAttrs

public class UETFresco implements IAttrs {
  @Override public List<Item> getAttrs(Element element) {
  
  }  
}

// step 2, put in UETool

UETool.putAttrsProviderClass(Class customizeClazz);

UETool.putAttrsProviderClass(String customizeClassName);

```

## License

[MIT](http://opensource.org/licenses/MIT)

