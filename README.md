UETool
======

### EFFECT

<img width="108" height="192" src="https://github.elenet.me/waimai/UETool/blob/master/art/edit_attr.png"/>

<img width="108" height="192" src="https://github.elenet.me/waimai/UETool/blob/master/art/relative_position.png"/>

<img width="108" height="192" src="https://github.elenet.me/waimai/UETool/blob/master/art/show_gridding.png"/>

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

#### USAGE

```java
// open 
UETool.showUETMenu();

// close
UETool.dismissUETMenu();
```


