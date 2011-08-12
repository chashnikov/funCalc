package nik.funCalc.tree;

/**
 * @author nik
 */
public class WhileStatement implements Statement {
  private BooleanExpression myCondition;
  private Statement myBody;

  public WhileStatement(BooleanExpression condition, Statement body) {
    myCondition = condition;
    myBody = body;
  }

  public String getText() {
    return "while (" + myCondition.getText() + ")\n" +
           myBody.getText();
  }

  public BooleanExpression getCondition() {
    return myCondition;
  }

  public Statement getBody() {
    return myBody;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitWhileStatement(this);
  }
}
