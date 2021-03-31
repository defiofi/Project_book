package com.kodilla.books;

import com.kodilla.books.domain.Book;
import com.kodilla.books.service.BookService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    private BookService bookService = BookService.getInstance();
    private Grid<Book> grid = new Grid<>(Book.class);
    private TextField filter = new TextField();
    private BookForm form = new BookForm(this);
    private Button addNewBook = new Button("Add new book");

    public MainView() {
        grid.setColumns("title", "author", "publicationYear", "type");
        filter.setPlaceholder("Filter by title");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> update());
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();
        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBook);
        addNewBook.addClickListener(e -> {
            grid.asSingleSelect().clear(); //"czyścimy" zaznaczenie
            form.setBook(new Book());      //dodajemy nowy obiekt do formularza
        });

        add(toolbar, mainContent);
        setSizeFull();
        refresh();
        form.setBook(null);
        grid.asSingleSelect().addValueChangeListener(event -> form.setBook(grid.asSingleSelect().getValue()));

    }

    public void refresh() {
        grid.setItems(bookService.getBooks());
    }
    private void update() {
        grid.setItems(bookService.findByTitle(filter.getValue()));
    }
}
