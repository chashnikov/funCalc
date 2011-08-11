package nik.funCalc.tree;

/**
 * @author nik
 */
public interface Node {
  String getText();

  void accept(NodeVisitor visitor);
}
