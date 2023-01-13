package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import AST.*;
import Token.Token;
import Token.TokenTypeList;

public class Parser {
  private Integer position;
  private List<Token> tokenList = new ArrayList<>();
  private Map<String, Integer> scope;

  public Parser(List<Token> tokenList) {
    this.tokenList = tokenList;
  }

  public void parseCode() {
    try {
      StatementsNode root = new StatementsNode();
      while (this.position < this.tokenList.size()) {
        ExpressionNode codeStringNode = this.parseExpression();
        this.require(TokenTypeList.SEMICOLON);
        root.addNode(codeStringNode);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private ExpressionNode parseExpression() {
    if (this.match(TokenTypeList.LOG) != null)
      return this.parsePrint();
    this.position -= 1;
    ExpressionNode variable = this.parseVariableOrNumber();
    Token assign = this.match(TokenTypeList.ASSIGN);
    if (assign != null) {
      ExpressionNode rightNode = this.parseFormula();
      ExpressionNode binaryNode = new BinOperationNode(assign, variable, rightNode);
      return binaryNode;
    }
    throw new Error("После переменной ожидается оператор присвоения на позиции " + this.position);
  }

  private ExpressionNode parsePrint() {
    return new UnarOperationNode(this.match(TokenTypeList.ASSIGN), this.parseFormula());
  }

  private ExpressionNode parseVariableOrNumber() {
    Token numberNode = this.match(TokenTypeList.NUMBER);
    if (numberNode != null)
      return new NumberNode(numberNode);
    Token variableNode = this.match(TokenTypeList.VARIABLE);
    if (variableNode != null)
      return new NumberNode(variableNode);
    throw new Error("Ожидается число или переменная на позиции " + this.position);
  }

  private ExpressionNode parseFormula() {
    ExpressionNode leftNode = this.parseParentheses();
    Token operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS);
    while (operator != null) {
      ExpressionNode rithNode = this.parseParentheses();
      leftNode = new BinOperationNode(operator, leftNode, rithNode);
      operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS);
    }
    return leftNode;
  }

  private ExpressionNode parseParentheses() {
    if (this.match(TokenTypeList.LPAR) == null)
    return this.parseVariableOrNumber();
    ExpressionNode node = this.parseFormula();
    try {
      this.require(TokenTypeList.RPAR);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return node;
  }

  private void require(TokenTypeList... types) throws Exception {
    if (this.match(types) == null)
      throw new Exception("На позиции " + this.position + " ожидается " + types[0]);
  }

  private Token match(TokenTypeList... types) {
    if (this.position < tokenList.size()) {
      Token currentToken = tokenList.get(this.position);
      Boolean isHasTokenType = Stream.of(types)
          .anyMatch(type -> type.getType().getName() == currentToken.getToken().getName());
      if (isHasTokenType) {
        this.position += 1;
        return currentToken;
      }
    }
    return null;
  }
}
