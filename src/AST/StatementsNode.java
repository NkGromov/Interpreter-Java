package AST;

import java.util.List;

public class StatementsNode extends ExpressionNode {
  private List<ExpressionNode> codeStrings;

  public void addNode(ExpressionNode node) {
    this.codeStrings.add(node);
  }

}
