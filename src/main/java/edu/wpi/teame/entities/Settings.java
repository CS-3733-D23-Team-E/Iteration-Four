package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public enum Settings {
  INSTANCE;

  String nyay = "\u00F1"; // ñ

  String aA = "\u00E1";
  String capitalaA = "\u00C1";
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  // Hawaiian Letters

  String oH = "\u014D";
  String okina = "\u02BB"; // Okina ʻ

  String aH = "\u0101";
  String eH = "\u0113";

  // French
  String ceH = "\u00E7";

  public enum Language {
    ENGLISH,
    SPANISH,
    FRENCH,
    HAWAIIAN;
  }

  public enum ScreenMode {

    DARK_MODE,
    LIGHT_MODE;
  }
  @Getter @Setter Language language;
  @Getter @Setter ScreenMode screenMode;

  Settings() {}
}
