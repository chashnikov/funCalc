package nik.funCalc.tree;

/**
 * @author nik
 */
public interface NodeVisitor {
  void visitStatements(StatementsNode node);

  void visitPrintStatement(PrintStatement node);

  void visitExpressionStatement(ExpressionStatement node);

  void visitFunctionCallExpression(FunctionCallExpression node);

  void visitBinaryExpression(BinaryExpression node);

  void visitIntegerLiteral(IntegerLiteralExpression node);

  void visitAssignmentStatement(AssignmentStatement node);

  void visitVariableExpression(VariableExpression node);
}
