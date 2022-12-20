package shpp.db.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoodsTest {


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test

    void ValidationTest(){
        Goods goods = new Goods(null,"Name",51.2);
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(goods);
        Assertions.assertEquals(1, goodsValidator.size());
        assertEquals("не должно равняться null",goodsValidator.iterator().next().getMessage());
    }
    @Test
    void ValidationPrice(){
        Goods goods = new Goods(new Category("ss"),"Name",49.9);
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(goods);
        Assertions.assertEquals(1, goodsValidator.size());
        assertEquals("должно быть не меньше 50",goodsValidator.iterator().next().getMessage());
    }
}