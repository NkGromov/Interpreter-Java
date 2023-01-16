package AST;

import java.util.Map;

import Token.*;

public class BinOperationNode extends ExpressionNode {
  private Token operator;
  private ExpressionNode leftNode;
  private ExpressionNode rightNode;

  public BinOperationNode(Token operator, ExpressionNode leftNode, ExpressionNode rightNode) {
    super();
    this.operator = operator;
    this.leftNode = leftNode;
    this.rightNode = rightNode;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope) {
    if (this.operator.getToken().getName() == TokenTypeList.PLUS.getType().getName())
      return this.leftNode.applyNode(scope) + this.rightNode.applyNode(scope);
    if (this.operator.getToken().getName() == TokenTypeList.MINUS.getType().getName())
      return this.leftNode.applyNode(scope) - this.rightNode.applyNode(scope);
    if (this.operator.getToken().getName() == TokenTypeList.ASSIGN.getType().getName()) {
      Integer result = this.rightNode.applyNode(scope);
      VariableNode variableNode = (VariableNode) this.leftNode;
      scope.put(variableNode.getVariable().getText(), result);
      return result;
    }
    return 0;
  }

  public Token getOperator() {
    return operator;
  }

  public ExpressionNode getLeftNode() {
    return leftNode;
  }

  public ExpressionNode getRightNode() {
    return rightNode;
  }
}
