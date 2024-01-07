package Utils;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeNode {
    private String symbol;
    private ParseTreeNode parent;
    private List<ParseTreeNode> children;

    public ParseTreeNode(String symbol) {
        this.symbol = symbol;
        this.children = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public ParseTreeNode getParent() {
        return parent;
    }

    public List<ParseTreeNode> getChildren() {
        return children;
    }

    public void addChild(ParseTreeNode child) {
        child.parent = this;
        children.add(child);
    }

    @Override
    public String toString() {
        return symbol;
    }

    public String toStringTree() {
        StringBuilder treeString = new StringBuilder();
        toStringTreeHelper(treeString, "", true);
        return treeString.toString();
    }

    private void toStringTreeHelper(StringBuilder treeString, String prefix, boolean isTail) {
        treeString.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(symbol)
                .append("\n");

        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).toStringTreeHelper(treeString, prefix + (isTail ? "    " : "│   "), false);
        }

        if (!children.isEmpty()) {
            children.get(children.size() - 1).toStringTreeHelper(treeString, prefix + (isTail ?"    " : "│   "), true);
        }
    }
}
