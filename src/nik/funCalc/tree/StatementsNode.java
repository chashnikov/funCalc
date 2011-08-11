package nik.funCalc.tree;

/**
 * @author nik
 */
public class StatementsNode implements Node {
  private Statement[] myStatements;

  public StatementsNode(Statement[] statements) {
    myStatements = statements;
  }

  public Statement[] getStatements() {
    return myStatements;
  }

  public void accept(NodeVisitor visitor) {
    visitor.visitStatements(this);
  }
}
