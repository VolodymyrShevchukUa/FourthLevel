package shpp.db;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import shpp.db.manager.ConnectionManager;
import shpp.db.manager.Selector;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Parser {
    private ConnectionManager connectionManager = new ConnectionManager();

    private final Connection connection = connectionManager.getConnection();
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet spreadsheet = workbook.createSheet(" Student Data ");


    public static void main(String[] args) {
        Parser test = new Parser();
        Selector selector = new Selector(test.connection);
//         dtoManager.getCategories().forEach( t -> System.out.println(t.getName()));
        test.fillGoods();
//        test.fillCategory();
//        try {
//            test.main();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    public void main() throws IOException {
        parsListPage("https://shop.metro.ua/ua/alkoholni-napoi/19152c/?&lst=list");
        int page = 2;
        while (parsListPage("https://shop.metro.ua/ua/alkoholni-napoi/19152c/?p=" + page + "&lst=list")) {
            page++;
            if (page == 63)
                break;
        }
        FileOutputStream out = new FileOutputStream("test1.xlsx");
        workbook.write(out);
        out.close();
    }

    private boolean parsListPage(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return false;
        }
        for (Element e : doc.select("a.product_name")) {
            try {
                parsProductPage(e.attr("href"));
            } catch (IOException ex) {
            }
        }
        return true;
    }

    private void parsProductPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String name = doc.select("h3.productDetail_right_name").text();
        String categ = doc.select("div.productDetail_textRow")
                .select("tr")
                .stream()
                .map(e -> e.select("td"))
                .filter(td -> td.get(0).text().equals("категорією"))
                .map(td -> td.get(1).text()).findAny().get();
        String proceTable = doc.select("table.price").select("td").first().text();
        writeNewLine(name, categ, proceTable);
    }


    int rowId = 0;

    private void writeNewLine(String... cells) {
        PreparedStatement statement;
        try (connection) {
            statement = connection.prepareStatement("INSERT INTO goods (category_id,goods_name,goods_price) VALUES ?,?,?");
            statement.setString(1, cells[1]);
            statement.setString(1, cells[1]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        XSSFRow row = spreadsheet.createRow(rowId++);
        int cellId = 0;
        for (String obj : cells) {
            Cell cell = row.createCell(cellId++);
            cell.setCellValue(obj);
        }

    }


    private void fillGoods() {
        try (FileInputStream fileInputStream = new FileInputStream(new File("test.xlsx")); connection) {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO goods (category_id,goods_name,goods_price)" +
                            "values (?,?,?)");

            HashMap<String, Integer> hashMap = new HashMap<>();
            int i = 1;
            for (String s : getList()) {
                hashMap.put(s, i++);
            }
            // формируем из файла экземпляр HSSFWorkbook

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

// выбираем первый лист для обработки
// нумерация начинается с 0
            XSSFSheet sheet = workbook.getSheetAt(0);

// получаем Iterator по всем строкам в листе
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    preparedStatement.setString(2, cellIterator.next().getStringCellValue());
                    preparedStatement.setInt(1, hashMap.get(cellIterator.next().getStringCellValue()));
                    preparedStatement.setDouble(3,
                            Double.parseDouble(cellIterator.next().getStringCellValue().split(" ")[0].replace(",", ".")));
                    preparedStatement.execute();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    private void fillCategory() {
        Set<String> strings = getList();
        PreparedStatement statement;
        try (connection) {
            statement = connection.prepareStatement("INSERT INTO category (category_name) VALUES (?)");

            for (String s : strings) {
                statement.setString(1, s);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Set<String> getList() {
        Set<String> list = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("categ.txt"))) {
            String str = bufferedReader.readLine();
            while (str != null) {
                list.add(str);
                str = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


}
