UETool ![](https://img.shields.io/badge/platform-android-lightgrey.svg) [![License](https://img.shields.io/badge/license-MIT-000000.svg)](https://github.com/eleme/UETool/blob/master/LICENSE) 
======

![](https://github.com/eleme/UETool/blob/master/art/uet_banner.jpeg)

## INTRODUCTION

[中文版](https://github.com/eleme/UETool/blob/master/README_zh.md)

UETool is a debug tool for anyone who needs show/edit one or more view's attributions. It works on Activity/Fragment/Dialog/PopupWindow or any else view shows on the screen.

At present UETool provides functionality as bellows:

- move any view on the screen, select view repeatedly will select its parent view
- show / edit normal view's attributions such as edit TextView's text、textSize、textColor etc.
- if you are using Fresco's DraweeView, UETool provides show more attributions like ImageURI、PlaceHolderImage、CornerRadius etc.
- you can customize any view's attributions you want with simple way such as some biz param
- show two view's relative position
- show gridding for checking some view alignment 

## EFFECT

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
| SpanBitmap | [PICTURE] 72px*39px | |
| DrawableLeft | [PICTURE] 51px*51px |  |
| DrawableRight | [PICTURE] 36px*36px |  |
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
dependencies {
  debugCompile 'me.ele:uetool:1.0.12'
  releaseCompile 'me.ele:uetool-no-op:1.0.12'

  // if you want to show more attrs about Fresco's DraweeView
  debugCompile 'me.ele:uetool-fresco:1.0.12'
}
```

### Usage

#### show floating window

```java
UETool.showUETMenu();

UETool.showUETMenu(int y);
```

#### dismiss floating window

```java
UETool.dismissUETMenu();
```

#### filter out view whitch you don't want to select

```java
UETool.putFilterClass(Class viewClazz);

UETool.putFilterClass(String viewClassName);
```

#### customize with your view

```java

// step 1, implements IAttrs

public class UETFresco implements IAttrs {
  @Override public List<Item> getAttrs(Element element) {
  
  }  
}

// step 2, put in UETool

UETool.putAttrsProviderClass(Class customizeClazz);

UETool.putAttrsProviderClass(String customizeClassName);

```

## License

![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

The gem is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).

