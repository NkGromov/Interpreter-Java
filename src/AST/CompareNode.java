package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;
import Token.Token;
import Token.TokenTypeList;

public class CompareNode extends ExpressionNode {
  private ExpressionNode leftNode;
  private ExpressionNode rightNode;
  private Token operator;
  private CompareType type;
  private List<ExpressionNode> body = new ArrayList<>();

  public CompareNode(ExpressionNode leftNode, ExpressionNode rightNode, Token operator, CompareType type,
      List<ExpressionNode> body) {
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.operator = operator;
    this.type = type;
    this.body = body;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    if (this.type == CompareType.ELSE) return 1;

    if (this.operator.getToken().getName() == TokenTypeList.DOABLEASSIGN.getType().getName()) 
      return (int) this.leftNode.applyNode(scope, fnDefinitions) == (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;
    if (this.operator.getToken().getName() == TokenTypeList.NOTASSIGN.getType().getName())
      return (int) this.leftNode.applyNode(scope, fnDefinitions) != (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;
    if (this.operator.getToken().getName() == TokenTypeList.LT.getType().getName())
      return (int) this.leftNode.applyNode(scope, fnDefinitions) < (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;
    if (this.operator.getToken().getName() == TokenTypeList.LTEQ.getType().getName())
      return (int) this.leftNode.applyNode(scope, fnDefinitions) <= (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;
    if (this.operator.getToken().getName() == TokenTypeList.GT.getType().getName())
      return (int) this.leftNode.applyNode(scope, fnDefinitions) > (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;
      if (this.operator.getToken().getName() == TokenTypeList.GTEQ.getType().getName())
      return (int) this.leftNode.applyNode(scope, fnDefinitions) >= (int) this.rightNode.applyNode(scope, fnDefinitions) ? 1 : 0;

    return 0;
  }

  public ExpressionNode getLeftOperand() {
    return leftNode;
  }

  public ExpressionNode getRightOperand() {
    return rightNode;
  }

  public List<ExpressionNode> getBody() {
    return body;
  }
}
