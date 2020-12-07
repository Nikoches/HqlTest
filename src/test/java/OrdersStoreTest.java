import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrdersStoreTest {
    private final BasicDataSource pool = new BasicDataSource();

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        File file = new File("db/dbGenerate");
        List<String> strings = Files.readAllLines(Path.of(("db/dbGenerate")));
        strings.forEach(line -> builder.append(line).append(System.lineSeparator()));
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @AfterEach
    public void droptable() throws SQLException {
       pool.getConnection().prepareStatement("drop table orders").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertEquals(all.size(), (1));
        assertEquals(all.get(0).getDescription(), "description1");
        assertEquals(all.get(0).getId(), (1));
    }

    @Test
    public void whenSaveOrderAndFindByName() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("test1", "description1"));
        Order order = new Order(1, "test1", "description1", new Timestamp(1607079986));
        assertEquals(store.findByName("test1"), order);
    }

    @Test
    public void whenSaveAndUpdate() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("test1", "description1"));
        Order order = new Order(1, "test2", "description2", new Timestamp(1607079986));
        store.updateById(1, "test2", "description2");
        assertEquals(store.findByName("test2"), order);
    }
}