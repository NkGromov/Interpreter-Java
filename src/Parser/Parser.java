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
  private Map<String, FunctionDefiniton> fnDefinitions = new HashMap<>();

  public Parser(List<Token> tokenList) {
    this.tokenList = tokenList;
  }

  public void run(StatementsNode rooteNode) {
    for (ExpressionNode node : rooteNode.getCodeStrings()) {
      if (node instanceof FnCallNode || node instanceof FunctionNode) {
        node.applyNode(this.scope, fnDefinitions);
      } else if (node instanceof BinOperationNode) {
        BinOperationNode binNode = (BinOperationNode) node;
        if (binNode.getRightNode() instanceof FunctionNode) {
          binNode.applyNode(scope, fnDefinitions);
        }
      } else {
        node.applyNode(this.scope);
      }

    }

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
    Token logToken = this.match(TokenTypeList.LOG);
    if (logToken != null)
      return this.parsePrint(logToken);
    Token returnToken = this.match(TokenTypeList.RETURN);
    if (returnToken != null)
      return this.parseReturn(returnToken);
    if (this.match(TokenTypeList.VARIABLE) != null && this.match(TokenTypeList.LPAR) != null) {
      this.position -= 2;
      return this.parseFunctionCall(this.match(TokenTypeList.VARIABLE).getText());
    }
    this.position -= 1;
    VariableNode variable = (VariableNode) this.parseVariableOrNumber();
    Token assign = this.match(TokenTypeList.ASSIGN);
    if (assign != null) {
      ExpressionNode rightNode = this.parseRightExpression(variable.getVariable().getText());
      ExpressionNode binaryNode = new BinOperationNode(assign, variable, rightNode);
      return binaryNode;
    }
    throw new Error("После переменной ожидается оператор присвоения на позиции " + this.position);
  }

  private ExpressionNode parsePrint(Token token) {
    return new UnarOperationNode(token, this.parseFormula());
  }

  private ExpressionNode parseReturn(Token token) {
    return new UnarOperationNode(token, this.parseFormula());
  }

  private ExpressionNode parseFunctionCall(String name) {
    try {
      this.require(TokenTypeList.LPAR);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    List<Integer> args = this.getArgs();
    try {
      this.require(TokenTypeList.RPAR);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return new FnCallNode(name, args);
  }

  private List<Integer> getArgs() {
    List<Integer> args = new ArrayList<>();
    Token currentToken = this.match(TokenTypeList.NUMBER);
    while (currentToken != null) {
      args.add(Integer.parseInt(currentToken.getText()));
      currentToken = this.match(TokenTypeList.NUMBER);
    }
    return args;
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

  private ExpressionNode parseRightExpression(String variableName) {
    if (match(TokenTypeList.LPAR) != null) {
      return parseFunctionExpression(variableName);
    }
    return parseFormula();
  }

  private ExpressionNode parseFunctionExpression(String variableName) {
    try {
      this.require(TokenTypeList.RPAR);
      this.require(TokenTypeList.GT);
      this.require(TokenTypeList.LBRACE);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    List<ExpressionNode> body = new ArrayList<>();
    while (tokenList.get(this.position).getToken().getName() != TokenTypeList.RBRACE.getType().getName()) {
      body.add(parseExpression());
      try {
        this.require(TokenTypeList.SEMICOLON);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    PrototypeNode proto = new PrototypeNode(variableName, new ArrayList<>());
    try {
      this.require(TokenTypeList.RBRACE);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return new FunctionNode(proto, body);
  }

  private ExpressionNode parseFormula() {
    ExpressionNode leftNode = this.parseParentheses();
    Token operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS, TokenTypeList.MULTIPLICATION,
        TokenTypeList.DIVISION);
    while (operator != null) {
      ExpressionNode rithNode = this.parseParentheses();
      leftNode = new BinOperationNode(operator, leftNode, rithNode);
      operator = this.match(TokenTypeList.PLUS, TokenTypeList.MINUS, TokenTypeList.MULTIPLICATION,
          TokenTypeList.DIVISION);
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
