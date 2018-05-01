UETool
======

[![License](https://img.shields.io/badge/license-MIT-000000.svg)](https://github.com/BigKeeper/big-keeper/blob/master/LICENSE)

### EFFECT

<div>
 <img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/edit_attr.png"/>

<img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/relative_position.png"/>

<img width="216" height="384" src="https://github.elenet.me/waimai/UETool/blob/master/art/show_gridding.png"/>
</div>

### HOW TO USE 

#### Installation

```gradle
dependencies {
  debugCompile 'me.ele:uetool:1.0.3-SNAPSHOT'
  releaseCompile 'me.ele:uetool-no-op:1.0.3-SNAPSHOT'
  
  // if you want to show attrs about Fresco's DraweeView
  debugCompile 'me.ele:uetool-fresco:1.0.3-SNAPSHOT'
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

