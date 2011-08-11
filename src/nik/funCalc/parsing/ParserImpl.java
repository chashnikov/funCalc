package nik.funCalc.parsing;

import nik.funCalc.tree.*;

import java.util.ArrayList;
import java.util.List;

/*
 expression := addExpression
 addExpression := multExpression (('+'|'-') multExpression)*
 multExpression := primaryExpression (('*'|'/') primaryExpression)*
 primaryExpression := '(' expression ')'
 primaryExpression := id '(' expression (',' expression)* ')'
 primaryExpression := number
 statement := expression ';'
 statement := 'print' expression ';'
 statement :=

 program := (statement)+
*/

/**
 * @author nik
 */
public class ParserImpl implements Parser {
  private Lexer myLexer;

  public ParserImpl(Lexer lexer) {
    myLexer = lexer;
  }

  private List<Expression> parseExpressionList() {
    List<Expression> expressions = new ArrayList<Expression>();
    do {
      Expression expression = parseExpression();
      expressions.add(expression);
    }
    while (myLexer.nextToken() == TokenType.COMMA);
    myLexer.pushBack();
    return expressions;
  }

  private Expression parsePrimaryExpression() {
    TokenType type = myLexer.nextToken();
    if (type == TokenType.LPAREN) {
      Expression expression = parseExpression();
      tokenExpected(TokenType.RPAREN);
      return expression;
    }
    if (type == TokenType.IDENTIFIER) {
      String functionName = myLexer.getToken();
      tokenExpected(TokenType.LPAREN);
      List<Expression> arguments = parseExpressionList();
      tokenExpected(TokenType.RPAREN);
      return new FunctionCallExpression(functionName, arguments);
    }
    if (type == TokenType.INT) {
      try {
        int value = Integer.parseInt(myLexer.getToken());
        return new IntegerLiteralExpression(value);
      }
      catch (NumberFormatException e) {
        throw new ParsingException(myLexer.getToken()+" is not an integer");
      }
    }
    throw new ParsingException("'(' or number or identifier expected but " + myLexer.getToken() + " found");
  }

  private Expression parseMultExpression() {
    Expression expression = parsePrimaryExpression();
    TokenType type = myLexer.nextToken();
    while (type == TokenType.MULT || type == TokenType.DIV) {
      Expression next = parsePrimaryExpression();
      expression = new BinaryExpression(type == TokenType.MULT ? Operations.MULT : Operations.DIV, expression, next);
      type = myLexer.nextToken();
    }
    myLexer.pushBack();
    return expression;
  }

  private Expression parseAddExpression() {
    Expression expression = parseMultExpression();
    TokenType type = myLexer.nextToken();
    while (type == TokenType.PLUS || type == TokenType.MINUS) {
      Expression next = parseMultExpression();
      expression = new BinaryExpression(type == TokenType.PLUS ? Operations.ADD : Operations.SUB, expression, next);
      type = myLexer.nextToken();
    }
    myLexer.pushBack();
    return expression;
  }

  private Expression parseExpression() {
    return parseAddExpression();
  }

  private Statement parseStatement() {
    TokenType type = myLexer.nextToken();
    if (type == TokenType.IDENTIFIER && myLexer.getToken().equals("print")) {
      Expression expression = parseExpression();
      tokenExpected(TokenType.SEMICOLON);
      return new PrintStatement(expression);
    }
    myLexer.pushBack();
    Expression expression = parseExpression();
    tokenExpected(TokenType.SEMICOLON);
    return new ExpressionStatement(expression);
  }

  private void tokenExpected(TokenType type) {
    tokenExpected(type, null);
  }

  private void tokenExpected(TokenType type, String text) {
    myLexer.nextToken();
    if (type != myLexer.getTokenType()) {
      throw new ParsingException(type.getName()+" expected but "+myLexer.getToken()+" found");
    }
    if (text != null && !text.equals(myLexer.getToken())) {
      throw new ParsingException(text+" expected but "+myLexer.getToken()+" found");
    }
  }

  public StatementsNode parseProgram() {
    List<Statement> statements = new ArrayList<Statement>();
    do {
      Statement statement = parseStatement();
      statements.add(statement);
      TokenType type = myLexer.nextToken();
      if (type == TokenType.EOF) {
        break;
      }
      myLexer.pushBack();
    } while (true);
    return new StatementsNode(statements);
  }
}
