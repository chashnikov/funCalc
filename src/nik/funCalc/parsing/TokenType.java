package nik.funCalc.parsing;

/**
 * @author nik
 */
public enum TokenType {
  IDENTIFIER("Identifier"), INT("Integer"), EOF("End of file"),
  PLUS("+"), MINUS("-"), MULT("*"), DIV("/"), LPAREN("("), RPAREN(")"), COMMA(","), SEMICOLON(";"), ASSIGN("=");
  private String myName;

  TokenType(String name) {
    myName = name;
  }

  public String getName() {
    return myName;
  }
}
