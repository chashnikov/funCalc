package nik.funCalc.tree;

/**
 * @author nik
 */
public class PrintStatement implements Statement {
  private Expression myExpression;

  public PrintStatement(Expression expression) {
    myExpression = expression;
  }

  public Expression getExpression() {
    return myExpression;
  }

  public String getText() {
    return "print " + myExpression.getText() + ";";
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitPrintStatement(this);
  }
}
