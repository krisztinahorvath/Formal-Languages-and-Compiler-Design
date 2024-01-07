package Models;

import Utils.ParseTreeNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserOutput {
    private List<ParseTreeTableRecord> table;
    private int nodeIdCounter;
    private ParseTreeNode root;

    public ParserOutput(ParseTreeNode root) {
        this.table = new ArrayList<>();
        this.nodeIdCounter = 1;
        this.root = root;

        convertToTable();
    }

    public List<ParseTreeTableRecord> convertToTable() {
        nodeIdCounter = 1;  // Reset the counter
        table.clear();
        traverseTree(root, 0);
        return table;
    }

    private int traverseTree(ParseTreeNode node, int fatherId) {
        int currentNodeId = nodeIdCounter++;
        ParseTreeTableRecord record = new ParseTreeTableRecord(node.getSymbol(), currentNodeId, fatherId);
        table.add(record);

        int previousSiblingId = 0;
        for (int i = 0; i < node.getChildren().size(); i++) {
            ParseTreeNode child = node.getChildren().get(i);
            int siblingId = traverseTree(child, currentNodeId);
            record.addSiblingId(siblingId);

            if (previousSiblingId != 0) {
                // Update leftSiblingId only if there is a left sibling
                table.get(previousSiblingId - 1).setLeftSiblingId(siblingId);
            }

            previousSiblingId = siblingId;
        }

        return currentNodeId; // Return the current node ID
    }

    public void displayTableRecords() {
        System.out.printf("%s | %s | %s | %s%n",
                "Node", "Symbol", "Father", "Left Sibling");
        System.out.println("------------------------------------");
        for (ParserOutput.ParseTreeTableRecord record : table) {
            System.out.printf("%-2d | %-2s | %-2s | %s%n",
                    record.getNodeId(), record.getSymbol(), toStringOrEmpty(record.getFatherId()),
                    toStringOrEmpty(record.getLeftSiblingId()));
        }
    }

    public void writeToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ParserOutput.ParseTreeTableRecord record : table) {
                writer.write(String.format("%-2d | %-2s | %-2s | %s%n",
                        record.getNodeId(), record.getSymbol(), toStringOrEmpty(record.getFatherId()),
                        toStringOrEmpty(record.getLeftSiblingId())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toStringOrEmpty(Object value) {
        return (value != null) ? value.toString() : "null";
    }

    public static class ParseTreeTableRecord {
        private String symbol;
        private int nodeId;
        private int fatherId;
        private int leftSiblingId;
        private List<Integer> siblingIds;

        public ParseTreeTableRecord(String symbol, int nodeId, int fatherId) {
            this.symbol = symbol;
            this.nodeId = nodeId;
            this.fatherId = fatherId;
            this.leftSiblingId = 0;
            this.siblingIds = new ArrayList<>();
        }

        public String getSymbol() {
            return symbol;
        }

        public int getNodeId() {
            return nodeId;
        }

        public int getFatherId() {
            return fatherId;
        }

        public int getLeftSiblingId() {
            return leftSiblingId;
        }

        public List<Integer> getSiblingIds() {
            return siblingIds;
        }

        public void addSiblingId(int siblingId) {
            siblingIds.add(siblingId);
        }

        public void setLeftSiblingId(int leftSiblingId) {
            this.leftSiblingId = leftSiblingId;
        }
    }
}
