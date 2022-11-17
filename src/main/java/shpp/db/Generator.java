package shpp.db;


import shpp.db.dto.Balance;
import shpp.db.dto.Goods;
import shpp.db.dto.Market;
import shpp.db.manager.ConnectionManager;
import shpp.db.manager.DTOManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Generator {
    private static final int MAX_SIZE = 3000000;
    private static final int BATCH_SIZE = 1000;

    ConnectionManager connectionManager = new ConnectionManager();
    List<Goods> goods;
    List<Market> markets;

    Connection connection;


    public void generate() {
        Random random = new Random();


        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO goods_list VALUES (?,?)")) {

            DTOManager dtoManager = new DTOManager(connection);
            markets = dtoManager.getMarkets();
            goods = dtoManager.getGoods();
            System.out.println();
            Stream.generate(Balance::new).limit(MAX_SIZE).map((g) -> g.setGoods(goods.get(random.nextInt(goods.size())))
                            .setMarket(markets.get(random.nextInt(markets.size()))))
                    .forEach(t -> putOnTable(t, preparedStatement));
        } catch (SQLException e) {
            throw new RuntimeException();
        }


    }

    int kostul = 0;

    public void putOnTable(Balance balance, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setInt(1, markets.indexOf(balance.getMarket()) + 1);
            preparedStatement.setInt(2, goods.indexOf(balance.getGoods()) + 1);
            kostul++;
            preparedStatement.addBatch();
            if (kostul % BATCH_SIZE == 0) {
                preparedStatement.executeBatch();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}




