package nik.funCalc.tree;

/**
 * @author nik
 */
public class IntegerLiteralExpression implements Expression {
  private int myValue;

  public IntegerLiteralExpression(int value) {
    myValue = value;
  }

  public int getValue() {
    return myValue;
  }

  public String getText() {
    return String.valueOf(myValue);
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitIntegerLiteral(this);
  }
}
