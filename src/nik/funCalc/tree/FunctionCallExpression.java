package nik.funCalc.tree;

/**
 * @author nik
 */
public class FunctionCallExpression implements Expression {
  public void accept(NodeVisitor visitor) {
    visitor.visitFunctionCallExpression(this);
  }
}
