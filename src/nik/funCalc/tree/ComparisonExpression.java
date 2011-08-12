package nik.funCalc.tree;

/**
 * @author nik
 */
public class ComparisonExpression implements BooleanExpression {
  private ComparisonOperation myOperation;
  private Expression myLeftOperand;
  private Expression myRightOperand;

  public ComparisonExpression(ComparisonOperation operation, Expression leftOperand, Expression rightOperand) {
    myOperation = operation;
    myLeftOperand = leftOperand;
    myRightOperand = rightOperand;
  }

  public ComparisonOperation getOperation() {
    return myOperation;
  }

  public Expression getLeftOperand() {
    return myLeftOperand;
  }

  public Expression getRightOperand() {
    return myRightOperand;
  }

  public String getText() {
    return myLeftOperand.getText() + myOperation.getText() + myRightOperand.getText();
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitComparisonExpression(this);
  }

}
