package com.exam;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class HistoryWindow {
    private final JFrame frame;

    public HistoryWindow(String email) {
        frame = new JFrame("Your Exam History - " + email);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        String[] columnNames = {"Score", "Cheat Attempts", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        MongoCollection<Document> collection = MongoDBUtil.getDatabase().getCollection("exam_results");
        try (MongoCursor<Document> cursor = collection.find(new Document("email", email)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(doc.getInteger("score")));
                row.add(String.valueOf(doc.getInteger("cheatAttempts")));
                row.add(doc.getString("timestamp"));
                model.addRow(row);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
