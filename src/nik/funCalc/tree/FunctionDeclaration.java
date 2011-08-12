package nik.funCalc.tree;

import java.util.List;

/**
 * @author nik
 */
public class FunctionDeclaration implements Statement {
  private String myFunName;
  private List<String> myParameters;
  private Statement myBody;

  public FunctionDeclaration(String funName, List<String> parameters, Statement body) {
    myFunName = funName;
    myParameters = parameters;
    myBody = body;
  }

  public String getFunName() {
    return myFunName;
  }

  public List<String> getParameters() {
    return myParameters;
  }

  public Statement getBody() {
    return myBody;
  }

  public String getText() {
    StringBuilder builder = new StringBuilder();
    builder.append("fun ").append(myFunName).append("(");
    boolean first = true;
    for (String parameter : myParameters) {
      if (!first) builder.append(",");
      first = false;
      builder.append(parameter);
    }
    builder.append(") {\n");
    builder.append(myBody.getText());
    builder.append("\n}");
    return builder.toString();
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitFunctionDeclaration(this);
  }
}
