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
        if (binNode.getRightNode() instanceof FunctionNode || binNode.getRightNode() instanceof FnCallNode) {
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
      checkRequire(TokenTypeList.SEMICOLON);
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
    if (this.positionMatch(TokenTypeList.VARIABLE, this.position) && this.positionMatch(TokenTypeList.LPAR, this.position + 1)) {
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

  private ExpressionNode parseFunctionCall(String name) {
    checkRequire(TokenTypeList.LPAR);
    List<Integer> args = this.getCallArgs();
    checkRequire(TokenTypeList.RPAR);
    return new FnCallNode(name, args);
  }

  private List<Integer> getCallArgs() {
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
    if (positionMatch(TokenTypeList.LPAR, this.position) && positionMatch(TokenTypeList.RPAR, this.position + 1)) {
      this.position += 1;
      return parseFunctionExpression(variableName);
    }
    if (positionMatch(TokenTypeList.LPAR, this.position) && positionMatch(TokenTypeList.VARIABLE, this.position + 1) && match(TokenTypeSets.OPERATIONS.getSets()) == null) {
      this.position += 1;
      return parseFunctionExpression(variableName);
    }
    if (positionMatch(TokenTypeList.VARIABLE, this.position) && positionMatch(TokenTypeList.LPAR, this.position + 1)) {
      Token variableNode = this.match(TokenTypeList.VARIABLE);
      return parseFunctionCall(variableNode.getText());
    }
    return parseFormula();
  }

  private ExpressionNode parseFunctionExpression(String variableName) {
    List<String> args = this.getDefinitionArgs();
    checkRequire(TokenTypeList.RPAR);
    checkRequire(TokenTypeList.GT);
    checkRequire(TokenTypeList.LBRACE);
    List<ExpressionNode> body = new ArrayList<>();
    while (tokenList.get(this.position).getToken().getName() != TokenTypeList.RBRACE.getType().getName()) {
      body.add(parseExpression());
      checkRequire(TokenTypeList.SEMICOLON);
    }
    PrototypeNode proto = new PrototypeNode(variableName, args);
    checkRequire(TokenTypeList.RBRACE);

    return new FunctionNode(proto, body);
  }

  private List<String> getDefinitionArgs() {
    List<String> args = new ArrayList<>();
    Token currentToken = this.match(TokenTypeList.VARIABLE);
    while (currentToken != null) {
      args.add(currentToken.getText());
      currentToken = this.match(TokenTypeList.VARIABLE);
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
    checkRequire(TokenTypeList.RPAR);
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

  private boolean positionMatch(TokenTypeList type, Integer matchPostion) {
    if(tokenList.get(matchPostion).getToken().getName() == type.getType().getName()) return true;
    else return false;
  }

  private boolean isPositionLessTokenList() {
    return this.position < this.tokenList.size();
  }
}
