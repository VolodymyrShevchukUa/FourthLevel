package shpp.db;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import shpp.db.dto.Balance;
import shpp.db.dto.Category;
import shpp.db.dto.Goods;
import shpp.db.dto.Market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeneratorTest {

    Connection connection = mock(Connection.class);

    AtomicInteger counter = new AtomicInteger(0);

    Generator generator = new Generator(connection,counter);

    PreparedStatement preparedStatement = mock(PreparedStatement.class);

    Balance balance = new Balance().setGoods(new Goods(new Category("Пиво"),"Blance",23.80))
            .setMarket(new Market("ss","Вул. Симоненка"));


    @Test
    void putOnTableTest() {
        int[] answer = {0};
        try {
            when(preparedStatement.executeBatch()).thenReturn(answer);
            doAnswer(i -> answer[0]++).when(preparedStatement).addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        generator.putOnBatch(balance,preparedStatement,new LinkedList<>(),new LinkedList<>());
        Assert.assertEquals(1,answer[0]);
    }

    @Test
    void filterTest(){
       try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
           Validator validator = factory.getValidator();
           Assert.assertEquals(false,generator.isValid(balance,validator));
       }
    }
}