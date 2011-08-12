package nik.funCalc.tree;

import java.util.List;

/**
 * @author nik
 */
public class StatementsNode implements Statement {
  private List<Statement> myStatements;

  public StatementsNode(List<Statement> statements) {
    myStatements = statements;
  }

  public List<Statement> getStatements() {
    return myStatements;
  }

  public String getText() {
    StringBuilder builder = new StringBuilder();
    for (Statement statement : myStatements) {
      if (builder.length() > 0) {
        builder.append("\n");
      }
      builder.append(statement.getText());
    }
    return builder.toString();
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitStatements(this);
  }
}
