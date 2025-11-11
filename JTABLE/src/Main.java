import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;


class Product {
    private String nazwa;
    private double cena;
    private int ilosc;

    public Product(String nazwa, double cena, int ilosc) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.ilosc = ilosc;
    }

    public String getNazwa() { return nazwa; }
    public double getCena() { return cena; }
    public int getIlosc() { return ilosc; }
}

// ----- Model tabeli -----
class ProductTableModel extends AbstractTableModel {
    private List<Product> produkty;
    private String[] nazwyKolumn = {"Nazwa", "Cena", "Ilość"};

    public ProductTableModel(List<Product> produkty) {
        this.produkty = produkty;
    }

    @Override
    public int getRowCount() {
        return produkty.size();
    }

    @Override
    public int getColumnCount() {
        return nazwyKolumn.length;
    }

    @Override
    public String getColumnName(int column) {
        return nazwyKolumn[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = produkty.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getNazwa();
            case 1: return p.getCena();
            case 2: return p.getIlosc();
            default: return null;
        }
    }
}


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            List<Product> produkty = new ArrayList<>();
            produkty.add(new Product("Chleb", 4.50, 10));
            produkty.add(new Product("Mleko", 3.20, 20));
            produkty.add(new Product("Masło", 8.99, 5));


            ProductTableModel model = new ProductTableModel(produkty);
            JTable tabela = new JTable(model);


            JFrame frame = new JFrame("Lista produktów");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(tabela), BorderLayout.CENTER);
            frame.setSize(400, 200);
            frame.setVisible(true);
        });
    }
}
