package nik.funCalc.tree;

/**
 * @author nik
 */
public interface Node {
  void accept(NodeVisitor visitor);
}
