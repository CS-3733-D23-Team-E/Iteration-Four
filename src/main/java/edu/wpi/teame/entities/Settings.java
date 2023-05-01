
package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public enum Settings {
  INSTANCE;

  public String nyay = "\u00F1"; // ñ

  public String aA = "\u00E1";
  public String capitalaA = "\u00C1";
  public String aE = "\u00E9"; // é
  public String aI = "\u00ED"; // í
  public String aO = "\u00F3"; // ó
  public String aU = "\u00FA"; // ù
  public String aQuestion = "\u00BF"; // Upside down question mark

  // Hawaiian Letters

  public String oH = "\u014D";
  public String okina = "\u02BB"; // Okina ʻ

  public String aH = "\u0101";
  public String eH = "\u0113";

  // French
  public String ceH = "\u00E7"; //ç
  public String aEH = "\u00E2"; //â
  public String oEH = "\u00F4"; //ô

  public String eEH = "\u00EA"; //ê


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
  @Getter @Setter String defaultLocation;

  Settings() {}
}

