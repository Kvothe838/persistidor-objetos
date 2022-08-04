package com.example.persistidorobjetos;

import com.example.persistidorobjetos.services.GenericService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericServiceTest {
    @Autowired
    GenericService genericService;

    @Test
    public void TestGetSimpleObjectFromString() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object o1 = this.genericService.getSimpleObjectFromString(Integer.class, "123");

        assertEquals(o1, 123);
        assertEquals(o1.getClass(), Integer.class);

        Object o2 = this.genericService.getSimpleObjectFromString(Boolean.class, "true");

        assertEquals(o2, true);
        assertEquals(o2.getClass(), Boolean.class);

        Object o3 = this.genericService.getSimpleObjectFromString(BigInteger.class, "123456789");
        System.out.println(o3);
        System.out.println(o3.getClass());
    }
}
