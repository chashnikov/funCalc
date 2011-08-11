package nik.funCalc.codegen;

import nik.funCalc.tree.*;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.PrintStream;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author nik
 */
public class FunctionCodeGenerator implements NodeVisitor {
  private MethodVisitor myMethodVisitor;

  public FunctionCodeGenerator(MethodVisitor methodVisitor) {
    myMethodVisitor = methodVisitor;
  }

  public void visitStatements(StatementsNode node) {
    for (Statement statement : node.getStatements()) {
      statement.accept(this);
    }
  }

  public void visitPrintStatement(PrintStatement node) {
    node.getExpression().accept(this);
    myMethodVisitor.visitFieldInsn(GETSTATIC, Type.getInternalName(System.class), "out", Type.getDescriptor(PrintStream.class));
    myMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(PrintStream.class), "println",
                                    Type.getMethodDescriptor(Type.VOID_TYPE, new Type[]{Type.INT_TYPE}));
  }

  public void visitExpressionStatement(ExpressionStatement node) {
    node.accept(this);
    myMethodVisitor.visitInsn(POP);
  }

  public void visitFunctionCallExpression(FunctionCallExpression node) {
  }

  public void visitBinaryExpression(BinaryExpression node) {
    node.getLeftOperand().accept(this);
    node.getRightOperand().accept(this);
    node.getOperation().generate(myMethodVisitor);
  }

  public void visitIntegerLiteral(IntegerLiteralExpression node) {
    myMethodVisitor.visitLdcInsn(node.getValue());
  }
}
