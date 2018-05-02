UETool
======

[![License](https://img.shields.io/badge/license-MIT-000000.svg)](https://github.com/BigKeeper/big-keeper/blob/master/LICENSE)

### EFFECT

<div>
 <img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/edit_attr.png"/>

<img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/relative_position.png"/>

<img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/show_gridding.png"/>
</div>


### ATTRIBUTE LIST

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


### HOW TO USE 

#### Installation

```gradle
dependencies {
  debugCompile 'me.ele:uetool:1.0.4-SNAPSHOT'
  releaseCompile 'me.ele:uetool-no-op:1.0.4-SNAPSHOT'
  
  // if you want to show attrs about Fresco's DraweeView
  debugCompile 'me.ele:uetool-fresco:1.0.4-SNAPSHOT'
}
```

#### Usage

```java
// open 
UETool.showUETMenu();

// close
UETool.dismissUETMenu();
```

### License

![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

The gem is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).

