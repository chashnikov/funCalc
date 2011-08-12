package nik.funCalc.tree;

/**
 * @author nik
 */
public class VariableExpression implements Expression {
  private String myVarName;

  public VariableExpression(String varName) {
    myVarName = varName;
  }

  public String getVarName() {
    return myVarName;
  }

  public String getText() {
    return myVarName;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitVariableExpression(this);
  }
}
