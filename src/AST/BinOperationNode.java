package AST;

import Token.Token;

public class BinOperationNode extends ExpressionNode {
  private Token operator;
  private ExpressionNode leftNode;
  private ExpressionNode rightNode;

  public BinOperationNode(Token operator,ExpressionNode leftNode, ExpressionNode rightNode){
    super();
    this.operator = operator;
    this.leftNode = leftNode;
    this.rightNode = rightNode;
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
