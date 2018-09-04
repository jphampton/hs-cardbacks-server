package com.models;

public class Cardback {
  private Cardback(int id, String gamepediaName, String mashapeName, String imgURL, String imgAnimatedURL, Month month, int year) {
    this.id = id;
    this.gamepediaName = gamepediaName;
    this.mashapeName = mashapeName;
    this.imgURL = imgURL;
    this.imgAnimatedURL = imgAnimatedURL;
    this.month = month;
    this.year = year;
  }

  public static class Builder {
    int id;
    String gamepediaName;
    String mashapeName;
    String imgURL;
    String imgAnimatedURL;
    Month month;
    int year;

    public Builder setId(int id) {
      this.id = id;
      return this;
    }

    public Builder setMashapeName(String mashapeName) {
      this.mashapeName = mashapeName;
      return this;
    }

    public Builder setGamepediaName(String gamepediaName) {
      this.gamepediaName = gamepediaName;
      return this;
    }

    public Builder setImgURL(String imgURL) {
      this.imgURL = imgURL;
      return this;
    }

    public Builder setImgAnimatedURL(String imgAnimatedURL) {
      this.imgAnimatedURL = imgAnimatedURL;
      return this;
    }

    public Builder setMonth(Month month) {
      this.month = month;
      return this;
    }

    public Builder setYear(int year) {
      this.year = year;
      return this;
    }

    public Cardback build() {
      return new Cardback(id, gamepediaName, mashapeName, imgURL, imgAnimatedURL, month, year);
    }
  }

  public Builder toBuilder() {
    return new Builder()
        .setId(id)
        .setGamepediaName(gamepediaName)
        .setMashapeName(mashapeName)
        .setImgURL(imgURL)
        .setImgAnimatedURL(imgAnimatedURL)
        .setMonth(month)
        .setYear(year);
  }

  public final int id;
  public final String gamepediaName;
  public final String mashapeName;
  public final String imgURL;
  public final String imgAnimatedURL;
  public final Month month;
  public final int year;
}