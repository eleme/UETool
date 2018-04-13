package me.ele.uetool.items;

public class TextItem implements Item {

  private String name;
  private String detail;

  public TextItem(String name, String detail) {
    this.name = name;
    this.detail = detail;
  }

  public String getName() {
    return name;
  }

  public String getDetail() {
    return detail;
  }
}