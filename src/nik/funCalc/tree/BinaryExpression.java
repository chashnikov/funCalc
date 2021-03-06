package nik.funCalc.tree;

/**
 * @author nik
 */
public class BinaryExpression implements Expression, BooleanExpression {
  private ArithmeticOperation myOperation;
  private Expression myLeftOperand;
  private Expression myRightOperand;

  public BinaryExpression(ArithmeticOperation operation, Expression leftOperand, Expression rightOperand) {
    myOperation = operation;
    myLeftOperand = leftOperand;
    myRightOperand = rightOperand;
  }

  public ArithmeticOperation getOperation() {
    return myOperation;
  }

  public Expression getLeftOperand() {
    return myLeftOperand;
  }

  public Expression getRightOperand() {
    return myRightOperand;
  }

  public String getText() {
    return "(" + myLeftOperand.getText() + ")" + myOperation.getText() + "(" + myRightOperand.getText() + ")";
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitBinaryExpression(this);
  }
}
