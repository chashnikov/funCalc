package nik.funCalc.parsing;

import nik.funCalc.tree.*;

import java.util.*;

/*
 expression := addExpression
 addExpression := multExpression (('+'|'-') multExpression)*
 multExpression := primaryExpression (('*'|'/') primaryExpression)*
 primaryExpression := '(' expression ')'
 primaryExpression := id '(' expression (',' expression)* ')'
 primaryExpression := number
 primaryExpression := id
 booleanExpression := expression ('<' | '>' | '==') expression
 statement := expression ';'
 statement := 'print' expression ';'
 statement := id '=' expression ';'
 statement := 'fun' id '(' id (',' id)* ')' '{' program '}'
 statement := 'return' expression ';'
 statement := 'if' '(' booleanExpression ')' statement ['else' statement]

 program := (statement)+
*/

/**
 * @author nik
 */
public class ParserImpl implements Parser {
  private static Map<TokenType, ComparisonOperation> BOOLEAN_OPERATIONS = new HashMap<TokenType, ComparisonOperation>();
  static {
    BOOLEAN_OPERATIONS.put(TokenType.EQUAL, Operations.EQUAL);
    BOOLEAN_OPERATIONS.put(TokenType.LESS, Operations.LESS);
    BOOLEAN_OPERATIONS.put(TokenType.GREATER, Operations.GREATER);
  }
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
      String name = myLexer.getToken();
      if (myLexer.nextToken() == TokenType.LPAREN) {
        List<Expression> arguments = parseExpressionList();
        tokenExpected(TokenType.RPAREN);
        return new FunctionCallExpression(name, arguments);
      }
      myLexer.pushBack();
      return new VariableExpression(name);
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

  private BooleanExpression parseBooleanExpression() {
    Expression left = parseExpression();
    TokenType type = myLexer.nextToken();
    ComparisonOperation operation = BOOLEAN_OPERATIONS.get(type);
    if (operation == null) {
      throw new ParsingException("'<', '>' or '==' expected but " + myLexer.getToken() + " found");
    }
    Expression right = parseExpression();
    return new ComparisonExpression(operation, left, right);
  }

  private Statement parseStatement() {
    TokenType type = myLexer.nextToken();
    if (type == TokenType.IDENTIFIER) {
      String token = myLexer.getToken();
      if (token.equals("print")) {
        Expression expression = parseExpression();
        tokenExpected(TokenType.SEMICOLON);
        return new PrintStatement(expression);
      }
      else if (token.equals("return")) {
        Expression expression = parseExpression();
        tokenExpected(TokenType.SEMICOLON);
        return new ReturnStatement(expression);
      }
      else if (token.equals("fun")) {
        return parseFunctionDeclaration();
      }
      else if (token.equals("if")) {
        tokenExpected(TokenType.LPAREN);
        BooleanExpression condition = parseBooleanExpression();
        tokenExpected(TokenType.RPAREN);
        Statement thenClause = parseStatement();
        Statement elseClause = null;
        if (myLexer.nextToken() == TokenType.IDENTIFIER && myLexer.getToken().equals("else")) {
          elseClause = parseStatement();
        }
        else {
          myLexer.pushBack();
        }
        return new IfStatement(condition, thenClause, elseClause);
      }
      if (myLexer.nextToken() == TokenType.ASSIGN) {
        Expression expression = parseExpression();
        tokenExpected(TokenType.SEMICOLON);
        return new AssignmentStatement(token, expression);
      }
      myLexer.pushBack();
      myLexer.pushBack(token, TokenType.IDENTIFIER);
    }
    else {
      myLexer.pushBack();
    }

    Expression expression = parseExpression();
    tokenExpected(TokenType.SEMICOLON);
    return new ExpressionStatement(expression);
  }

  private Statement parseFunctionDeclaration() {
    tokenExpected(TokenType.IDENTIFIER);
    String name = myLexer.getToken();
    tokenExpected(TokenType.LPAREN);
    List<String> parameters = new ArrayList<String>();
    do {
      tokenExpected(TokenType.IDENTIFIER);
      parameters.add(myLexer.getToken());
    }
    while (myLexer.nextToken() == TokenType.COMMA);
    myLexer.pushBack();
    tokenExpected(TokenType.RPAREN);
    tokenExpected(TokenType.LBRACE);
    StatementsNode body = parseStatements(true);
    tokenExpected(TokenType.RBRACE);
    return new FunctionDeclaration(name, parameters, body);
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
    return parseStatements(false);
  }

  private StatementsNode parseStatements(boolean insideFun) {
    List<Statement> statements = new ArrayList<Statement>();
    do {
      Statement statement = parseStatement();
      statements.add(statement);
      TokenType type = myLexer.nextToken();
      myLexer.pushBack();
      if (type == TokenType.EOF || insideFun && type == TokenType.RBRACE) {
        break;
      }
    } while (true);
    return new StatementsNode(statements);
  }
}
