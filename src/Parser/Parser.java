package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
      node.applyNode(this.scope, this.fnDefinitions);
    }
  }

  public StatementsNode parseCode() {
    StatementsNode root = new StatementsNode();
    while (isPositionLessTokenList()) {
      ExpressionNode codeStringNode = this.parseExpression();
      this.checkRequire(TokenTypeList.SEMICOLON);
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
    if (this.positionRelativeMatch(TokenTypeList.IF))
      return this.parseCondition();
    if (this.positionRelativeMatch(TokenTypeList.VARIABLE, TokenTypeList.LPAR)) {
      return this.parseFunctionCall(this.match(TokenTypeList.VARIABLE).getText());
    }
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

  private ExpressionNode parseCondition() {
    List<CompareNode> compares = new ArrayList<>();
    while (this.match(TokenTypeSets.LOGICKEYWORDS.getSets()) != null) {
      CompareType type;
      ExpressionNode leftNode = new ExpressionNode();
      ExpressionNode rightNode = new ExpressionNode();
      Token operator = new Token(this.position, "operator", TokenTypeList.LPAR.getType());
      if (this.match(TokenTypeList.IF) != null)
        type = CompareType.IFELSE;
      else if (this.tokenList.get(this.position - 1).getToken() == TokenTypeList.ELSE.getType())
        type = CompareType.ELSE;
      else
        type = CompareType.IF;
      if (type != CompareType.ELSE) {
        this.checkRequire(TokenTypeList.LPAR);
        leftNode = parseVariableOrNumber();
        operator = this.match(TokenTypeSets.LOGICOPERATIONS.getSets());
        rightNode = parseVariableOrNumber();
        this.checkRequire(TokenTypeList.RPAR);
      }
      List<ExpressionNode> body = getBody();
      compares.add(new CompareNode(leftNode, rightNode, operator, type, body));
    }
    return new ConditionNode(compares);
  }

  private ExpressionNode parseRightExpression(String variableName) {
    if (this.positionRelativeMatch(TokenTypeList.LPAR, TokenTypeList.RPAR)) {
      return this.parseFunctionExpression(variableName);
    }
    if (this.positionRelativeMatch(TokenTypeList.LPAR, TokenTypeList.VARIABLE)
        && this.match(TokenTypeSets.OPERATIONS.getSets()) == null) {
      return this.parseFunctionExpression(variableName);
    }
    if (this.positionRelativeMatch(TokenTypeList.VARIABLE, TokenTypeList.LPAR)) {
      Token variableNode = this.match(TokenTypeList.VARIABLE);
      return this.parseFunctionCall(variableNode.getText());
    }
    return this.parseFormula();
  }

  private ExpressionNode parseFunctionCall(String name) {
    checkRequire(TokenTypeList.LPAR);
    List<Integer> args = this.getArgs(TokenTypeList.NUMBER).stream().map(arg -> Integer.parseInt(arg.getText()))
        .collect(Collectors.toList());
    checkRequire(TokenTypeList.RPAR);
    return new FnCallNode(name, args);
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

  private ExpressionNode parseFunctionExpression(String variableName) {
    this.checkRequire(TokenTypeList.LPAR);
    List<String> arguments = this.getArgs(TokenTypeList.VARIABLE).stream().map(arg -> arg.getText())
        .collect(Collectors.toList());
    this.checkRequire(TokenTypeList.RPAR);
    this.checkRequire(TokenTypeList.GT);
    List<ExpressionNode> body = getBody();
    PrototypeNode proto = new PrototypeNode(variableName, arguments);
    return new FunctionNode(proto, body);
  }

  private List<ExpressionNode> getBody() {
    this.checkRequire(TokenTypeList.LBRACE);
    List<ExpressionNode> body = new ArrayList<>();
    while (this.tokenList.get(this.position).getToken().getName() != TokenTypeList.RBRACE.getType().getName()) {
      body.add(this.parseExpression());
      this.checkRequire(TokenTypeList.SEMICOLON);
    }
    this.checkRequire(TokenTypeList.RBRACE);
    return body;
  }

  private List<Token> getArgs(TokenTypeList type) {
    List<Token> args = new ArrayList<>();
    Token currentToken = this.match(type);
    while (currentToken != null) {
      args.add(currentToken);
      currentToken = this.match(type);
    }
    return args;
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
    this.checkRequire(TokenTypeList.RPAR);
    return node;
  }

  private void checkRequire(TokenTypeList... types) {
    try {
      this.require(types);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void require(TokenTypeList... types) throws Exception {
    if (this.match(types) == null)
      throw new Exception("На позиции " + this.position + " ожидается " + types[0]);
  }

  private Token match(TokenTypeList... types) {
    if (isPositionLessTokenList()) {
      Token currentToken = this.tokenList.get(this.position);
      Boolean isHasTokenType = Stream.of(types)
          .anyMatch(type -> type.getType().getName() == currentToken.getToken().getName());
      if (isHasTokenType) {
        this.position += 1;
        return currentToken;
      }
    }
    return null;
  }

  private boolean positionRelativeMatch(TokenTypeList... types) {
    boolean result = true;
    for (int i = 0; i < types.length; i++) {
      int currentPosition = this.position + i;
      if (this.tokenList.get(currentPosition).getToken().getName() != types[i].getType().getName())
        result = false;
    }
    return result;
  }

  private boolean isPositionLessTokenList() {
    return this.position < this.tokenList.size();
  }
}
