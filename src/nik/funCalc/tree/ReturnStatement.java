package nik.funCalc.tree;

/**
 * @author nik
 */
public class ReturnStatement implements Statement {
  private Expression myExpression;

  public ReturnStatement(Expression expression) {
    myExpression = expression;
  }

  public String getText() {
    return "return " + myExpression.getText() + ";";
  }

  public Expression getExpression() {
    return myExpression;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitReturnStatement(this);
  }
}
