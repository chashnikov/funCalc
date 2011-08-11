package nik.funCalc.tree;

import java.util.List;

/**
 * @author nik
 */
public class FunctionCallExpression implements Expression {
  private String myFunctionName;
  private List<Expression> myArguments;

  public FunctionCallExpression(String functionName, List<Expression> arguments) {
    myFunctionName = functionName;
    myArguments = arguments;
  }

  public String getFunctionName() {
    return myFunctionName;
  }

  public List<Expression> getArguments() {
    return myArguments;
  }

  public String getText() {
    StringBuilder builder = new StringBuilder();
    builder.append(myFunctionName).append("(");
    boolean first = true;
    for (Expression argument : myArguments) {
      if (!first) builder.append(",");
      first = false;
      builder.append(argument.getText());
    }
    builder.append(")");
    return builder.toString();
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitFunctionCallExpression(this);
  }
}
