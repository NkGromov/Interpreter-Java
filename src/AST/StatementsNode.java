package AST;

import java.util.ArrayList;
import java.util.List;

public class StatementsNode extends ExpressionNode {
  private List<ExpressionNode> codeStrings = new ArrayList<>();

  public void addNode(ExpressionNode node) {
    this.codeStrings.add(node);
  }

  public List<ExpressionNode> getCodeStrings() {
    return codeStrings;
  }

}
