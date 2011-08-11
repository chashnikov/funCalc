package nik.funCalc.parsing;

import nik.funCalc.tree.StatementsNode;

/**
 * @author nik
 */
public interface Parser {
  StatementsNode parseProgram();
}
