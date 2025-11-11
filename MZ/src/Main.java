import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class Task {
    private String nazwa;
    private boolean ukonczone;
    private String priorytet;

    public Task(String nazwa, boolean ukonczone, String priorytet) {
        this.nazwa = nazwa;
        this.ukonczone = ukonczone;
        this.priorytet = priorytet;
    }

    public String getNazwa() { return nazwa; }
    public boolean isUkonczone() { return ukonczone; }
    public String getPriorytet() { return priorytet; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
    public void setUkonczone(boolean ukonczone) { this.ukonczone = ukonczone; }
    public void setPriorytet(String priorytet) { this.priorytet = priorytet; }
}

class TaskTableModel extends AbstractTableModel {
    private List<Task> zadania;
    private String[] kolumny = {"Nazwa", "Status", "Priorytet"};

    public TaskTableModel(List<Task> zadania) {
        this.zadania = zadania;
    }

    @Override
    public int getRowCount() {
        return zadania.size();
    }

    @Override
    public int getColumnCount() {
        return kolumny.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolumny[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task t = zadania.get(rowIndex);
        switch (columnIndex) {
            case 0: return t.getNazwa();
            case 1: return t.isUkonczone();
            case 2: return t.getPriorytet();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0 || col == 1 || col == 2;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Task t = zadania.get(row);
        switch (col) {
            case 0: t.setNazwa((String) value); break;
            case 1: t.setUkonczone((Boolean) value); break;
            case 2: t.setPriorytet((String) value); break;
        }
        fireTableCellUpdated(row, col);
    }

    public void addTask(Task t) {
        zadania.add(t);
        fireTableRowsInserted(zadania.size()-1, zadania.size()-1);
    }

    public void removeTask(int row) {
        if (row >= 0 && row < zadania.size()) {
            zadania.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}

class PriorityCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String priorytet = (String) table.getValueAt(row, 2);
        if (!isSelected) {
            if ("Wysoki".equalsIgnoreCase(priorytet)) {
                c.setBackground(new Color(255, 120, 120));
            } else if ("Średni".equalsIgnoreCase(priorytet)) {
                c.setBackground(new Color(255, 240, 180));
            } else {
                c.setBackground(Color.WHITE);
            }
        }
        return c;
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Menedżer Zadań");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        List<Task> zadania = new ArrayList<>();
        zadania.add(new Task("Zrobić zakupy", false, "Wysoki"));
        zadania.add(new Task("Napisać raport", true, "Średni"));
        zadania.add(new Task("Umyć samochód", false, "Niski"));

        TaskTableModel model = new TaskTableModel(zadania);
        JTable tabela = new JTable(model);

        TableColumn statusColumn = tabela.getColumnModel().getColumn(1);
        statusColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        statusColumn.setCellRenderer(tabela.getDefaultRenderer(Boolean.class));

        tabela.getColumnModel().getColumn(2).setCellRenderer(new PriorityCellRenderer());

        JPanel panelLewy = new JPanel();
        panelLewy.setLayout(new GridLayout(2, 1, 10, 10));
        JButton btnDodaj = new JButton("Dodaj Zadanie");
        JButton btnUsun = new JButton("Usuń Wybrane");
        panelLewy.add(btnDodaj);
        panelLewy.add(btnUsun);

        btnDodaj.addActionListener(e -> {
            String nazwa = JOptionPane.showInputDialog(frame, "Podaj nazwę zadania:");
            if (nazwa != null && !nazwa.trim().isEmpty()) {
                String[] opcje = {"Wysoki", "Średni", "Niski"};
                String priorytet = (String) JOptionPane.showInputDialog(
                        frame, "Wybierz priorytet:", "Nowe Zadanie",
                        JOptionPane.PLAIN_MESSAGE, null, opcje, "Średni");
                if (priorytet != null) {
                    model.addTask(new Task(nazwa, false, priorytet));
                }
            }
        });

        btnUsun.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                model.removeTask(row);
            } else {
                JOptionPane.showMessageDialog(frame, "Wybierz zadanie do usunięcia!");
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLewy, scrollPane);
        splitPane.setDividerLocation(150);
        frame.add(splitPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
