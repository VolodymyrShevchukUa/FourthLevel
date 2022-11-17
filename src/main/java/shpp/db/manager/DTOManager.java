package shpp.db.manager;

import shpp.db.dto.Category;
import shpp.db.dto.Goods;
import shpp.db.dto.Market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DTOManager {
    Connection connection;

    public DTOManager(Connection connection) {
        this.connection = connection;
    }

    public List<Category> getCategories() {
        List<Category> list = new LinkedList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT category_name FROM category")) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                list.add(new Category( result.getString(1)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Goods> getGoods() {
        List<Goods> list = new LinkedList<>();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT category.category_name , g.goods_name ,g.goods_price" + // ХЗ чи працює, треба заповнити список goods
                        " FROM category INNER JOIN goods g on category.category_id = g.category_id")) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                list.add(new Goods( new Category(result.getString(1)),result.getString(2)
                        ,Double.parseDouble(result.getString(3))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Market> getMarkets() {
        List<Market> list = new LinkedList<>();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT market_name,market_adres FROM market;")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(new Market(resultSet.getString(2),resultSet.getString(1)));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }


}
