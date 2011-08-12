package nik.funCalc.tree;

/**
 * @author nik
 */
public class IfStatement implements Statement {
  private BooleanExpression myCondition;
  private Statement myThenClause;
  private Statement myElseClause;

  public IfStatement(BooleanExpression condition, Statement thenClause, Statement elseClause) {
    myCondition = condition;
    myThenClause = thenClause;
    myElseClause = elseClause;
  }

  public String getText() {
    return "if (" + myCondition.getText() + ")\n" +
           myThenClause.getText() +
           (myElseClause == null ? "" : "\nelse\n" + myElseClause.getText());
  }

  public BooleanExpression getCondition() {
    return myCondition;
  }

  public Statement getThenClause() {
    return myThenClause;
  }

  public Statement getElseClause() {
    return myElseClause;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitIfStatement(this);
  }
}
