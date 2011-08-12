package nik.funCalc.tree;

/**
 * @author nik
 */
public class AssignmentStatement implements Statement {
  private String myVarName;
  private Expression myExpression;

  public AssignmentStatement(String varName, Expression expression) {
    myVarName = varName;
    myExpression = expression;
  }

  public String getVarName() {
    return myVarName;
  }

  public Expression getExpression() {
    return myExpression;
  }

  public String getText() {
    return myVarName + "=" + myExpression.getText() + ";";
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitAssignmentStatement(this);
  }
}
