package nik.funCalc.codegen;

import nik.funCalc.tree.*;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author nik
 */
public class FunctionCodeGenerator implements NodeVisitor {
  private MethodVisitor myMethodVisitor;
  private Map<String, Integer> myVariables = new HashMap<String, Integer>();
  private int myMaxSlot;

  public FunctionCodeGenerator(MethodVisitor methodVisitor) {
    myMethodVisitor = methodVisitor;
  }

  public void visitStatements(StatementsNode node) {
    for (Statement statement : node.getStatements()) {
      statement.accept(this);
    }
  }

  public void visitPrintStatement(PrintStatement node) {
    myMethodVisitor.visitFieldInsn(GETSTATIC, Type.getInternalName(System.class), "out", Type.getDescriptor(PrintStream.class));
    node.getExpression().accept(this);
    myMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(PrintStream.class), "println",
                                    Type.getMethodDescriptor(Type.VOID_TYPE, new Type[]{Type.INT_TYPE}));
  }

  public void visitExpressionStatement(ExpressionStatement node) {
    node.getExpression().accept(this);
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

  public void visitAssignmentStatement(AssignmentStatement node) {
    String varName = node.getVarName();
    Integer slot = myVariables.get(varName);
    if (slot == null) {
      slot = myMaxSlot++;
      myVariables.put(varName, slot);
    }
    node.getExpression().accept(this);
    myMethodVisitor.visitVarInsn(ISTORE, slot);
  }

  public void visitVariableExpression(VariableExpression node) {
    String varName = node.getVarName();
    if (!myVariables.containsKey(varName)) {
      throw new GenerationException("Cannot resolve variable '" + varName + "'");
    }
    myMethodVisitor.visitVarInsn(ILOAD, myVariables.get(varName));
  }
}
