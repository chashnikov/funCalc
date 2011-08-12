package nik.funCalc.codegen;

import nik.funCalc.tree.*;
import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author nik
 */
public class FunctionCodeGenerator implements NodeVisitor {
  private MethodVisitor myMethodVisitor;
  private String myClassName;
  private ClassVisitor myClassVisitor;
  private Map<String, Integer> myVariables = new HashMap<String, Integer>();
  private Map<String, FunctionDeclaration> myFunctions = new HashMap<String, FunctionDeclaration>();
  private FunctionCodeGenerator myParentFunction;
  private int myMaxSlot;
  private int myReturnsCount;

  public FunctionCodeGenerator(MethodVisitor methodVisitor, String className, ClassVisitor classVisitor) {
    myMethodVisitor = methodVisitor;
    myClassName = className;
    myClassVisitor = classVisitor;
  }

  public FunctionCodeGenerator(MethodVisitor methodVisitor, FunctionCodeGenerator parent, List<String> parameters) {
    myMethodVisitor = methodVisitor;
    myClassName = parent.myClassName;
    myClassVisitor = parent.myClassVisitor;
    myParentFunction = parent;
    for (String parameter : parameters) {
      myVariables.put(parameter, myMaxSlot++);
    }
    myFunctions.putAll(parent.myFunctions);
  }

  public int getReturnsCount() {
    return myReturnsCount;
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
    String funName = node.getFunctionName();
    FunctionDeclaration declaration = myFunctions.get(funName);
    if (declaration == null) {
      throw new GenerationException("Cannot resolve function '"+funName+"'");
    }
    List<Expression> arguments = node.getArguments();
    if (declaration.getParameters().size() != arguments.size()) {
      throw new GenerationException(declaration.getParameters().size()+" parameters for '"+funName+"' expected but '" +
                                    arguments.size()+" found");
    }
    for (Expression argument : arguments) {
      argument.accept(this);
    }
    myMethodVisitor.visitMethodInsn(INVOKESTATIC, myClassName, funName, getMethodSignature(declaration));
  }

  private String getMethodSignature(FunctionDeclaration declaration) {
    StringBuilder sig = new StringBuilder();
    sig.append("(");
    for (String parameter : declaration.getParameters()) {
      sig.append("I");
    }
    sig.append(")I");
    return sig.toString();
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

  public void visitFunctionDeclaration(FunctionDeclaration node) {
    if (myParentFunction != null) {
      throw new GenerationException("Inner functions aren't supported");
    }
    String funName = node.getFunName();
    if (myFunctions.containsKey(funName)) {
      throw new GenerationException("function '"+funName+"' already declared");
    }
    myFunctions.put(funName, node);
    MethodVisitor methodVisitor = myClassVisitor.visitMethod(ACC_PRIVATE | ACC_STATIC, funName, getMethodSignature(node), null, null);
    methodVisitor.visitCode();
    FunctionCodeGenerator generator = new FunctionCodeGenerator(methodVisitor, this, node.getParameters());
    node.getBody().accept(generator);
    if (generator.myReturnsCount == 0) {
      methodVisitor.visitInsn(ICONST_0);
      methodVisitor.visitInsn(IRETURN);
    }
    else if (generator.myReturnsCount == 1) {
      Statement body = node.getBody();
      while (body instanceof StatementsNode) {
        List<Statement> statements = ((StatementsNode) body).getStatements();
        body = statements.get(statements.size()-1);
      }
      if (!(body instanceof ReturnStatement)) {
        throw new GenerationException("'return' statement must be the last statement in a function body");
      }
    }
    else {
      throw new GenerationException("function body can contain only one return statement");
    }
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }

  public void visitReturnStatement(ReturnStatement node) {
    node.getExpression().accept(this);
    myMethodVisitor.visitInsn(IRETURN);
    myReturnsCount++;
  }

  public void visitIfStatement(IfStatement node) {
    node.getCondition().accept(this);
    Label label = new Label();
    myMethodVisitor.visitJumpInsn(IFEQ, label);
    node.getThenClause().accept(this);
    Statement elseClause = node.getElseClause();
    if (elseClause == null) {
      myMethodVisitor.visitLabel(label);
    }
    else {
      Label end = new Label();
      myMethodVisitor.visitJumpInsn(GOTO, end);
      myMethodVisitor.visitLabel(label);
      elseClause.accept(this);
      myMethodVisitor.visitLabel(end);
    }
  }

  public void visitComparisonExpression(ComparisonExpression node) {
    node.getLeftOperand().accept(this);
    node.getRightOperand().accept(this);
    Label label = new Label();
    myMethodVisitor.visitJumpInsn(node.getOperation().getOpcode(), label);
    myMethodVisitor.visitInsn(ICONST_0);
    Label end = new Label();
    myMethodVisitor.visitJumpInsn(GOTO, end);
    myMethodVisitor.visitLabel(label);
    myMethodVisitor.visitInsn(ICONST_1);
    myMethodVisitor.visitLabel(end);
  }

  public void visitWhileStatement(WhileStatement node) {
    Label start = new Label();
    myMethodVisitor.visitLabel(start);
    node.getCondition().accept(this);
    Label end = new Label();
    myMethodVisitor.visitJumpInsn(IFEQ, end);
    node.getBody().accept(this);
    myMethodVisitor.visitJumpInsn(GOTO, start);
    myMethodVisitor.visitLabel(end);
  }
}
