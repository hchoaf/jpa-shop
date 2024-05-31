package com.hchoaf.jpashop.service;

import com.hchoaf.jpashop.entity.item.Book;
import com.hchoaf.jpashop.entity.item.Movie;
import com.hchoaf.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    // @Rollback(value = false)
    public void saveItemTest() throws Exception {
        Book book = new Book();
        book.setAuthor("Hangsun");

        Long savedId = itemService.saveItem(book);
        assertEquals(itemService.findOne(savedId), book);
    }

    @Test
    public void findAllItemsTest() throws Exception {
        Book book = new Book();
        book.setAuthor("Hangsun");
        Movie movie = new Movie();
        movie.setDirector("Hangsun");

        Long bookId = itemService.saveItem(book);
        Long movieId = itemService.saveItem(movie);

        Assertions.assertThat(itemService.findItems()).containsExactly(book, movie);
    }

}