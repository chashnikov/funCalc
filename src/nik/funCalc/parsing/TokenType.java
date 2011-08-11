package nik.funCalc.parsing;

/**
 * @author nik
 */
public enum TokenType {
  IDENTIFIER("Identifier"), INT("Integer"), SYMBOL("Symbol"), EOF("End of file");
  private String myName;

  TokenType(String name) {
    myName = name;
  }

  public String getName() {
    return myName;
  }
}
