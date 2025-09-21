package com.example.jobapplicationmanager;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import java.time.LocalDate;

public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {
    private final DatePicker datePicker = new DatePicker();

    public DatePickerTableCell() {
        datePicker.setOnAction(event -> {
            commitEdit(datePicker.getValue());
        });

        datePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) return;
        datePicker.setValue(getItem());
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? getItem().toString() : "");
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            datePicker.setValue(item);
            setGraphic(datePicker);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setText(item != null ? item.toString() : "");
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
}
