package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import AST.*;
import Token.*;

public class Parser {
  private Integer position = 0;
  private List<Token> tokenList = new ArrayList<>();
  private Map<String, Integer> scope = new HashMap<>();

  public Parser(List<Token> tokenList) {
    this.tokenList = tokenList;
  }

  public void run(StatementsNode rooteNode) {
    for (ExpressionNode node : rooteNode.getCodeStrings())
      node.applyNode(this.scope);
  }

  public StatementsNode parseCode() {
    StatementsNode root = new StatementsNode();
    while (isPositionLessTokenList()) {
      ExpressionNode codeStringNode = this.parseExpression();
      try {
        this.require(TokenTypeList.SEMICOLON);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      root.addNode(codeStringNode);
    }

    return root;
  }

  private ExpressionNode parseExpression() {
    if (this.match(TokenTypeList.LOG) != null)
      return this.parsePrint();
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
    return new UnarOperationNode(this.match(TokenTypeList.LOG), this.parseFormula());
  }

  private ExpressionNode parseVariableOrNumber() {
    Token numberNode = this.match(TokenTypeList.NUMBER);
    if (numberNode != null)
      return new NumberNode(numberNode);
    Token variableNode = this.match(TokenTypeList.VARIABLE);
    if (variableNode != null)
      return new VariableNode(variableNode);
    throw new Error("Ожидается число или переменная на позиции " + this.position);
  }

  private ExpressionNode parseFormula() {
    ExpressionNode leftNode = this.parseParentheses();
    Token operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS, TokenTypeList.MULTIPLICATION, TokenTypeList.DIVISION);
    while (operator != null) {
      ExpressionNode rithNode = this.parseParentheses();
      leftNode = new BinOperationNode(operator, leftNode, rithNode);
      operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS, TokenTypeList.MULTIPLICATION, TokenTypeList.DIVISION);
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
    if (isPositionLessTokenList()) {
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

  private boolean isPositionLessTokenList() {
    return this.position < this.tokenList.size();
  }
}
