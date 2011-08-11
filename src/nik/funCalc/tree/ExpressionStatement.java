package nik.funCalc.tree;

/**
 * @author nik
 */
public class ExpressionStatement implements Statement {
  private Expression myExpression;

  public ExpressionStatement(Expression expression) {
    myExpression = expression;
  }

  public Expression getExpression() {
    return myExpression;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitExpressionStatement(this);
  }
}
