package org.kgromov.drugstore.ui.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.model.Category;
import org.kgromov.drugstore.model.DrugsForm;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.kgromov.drugstore.ui.components.DrugsUploader;
import org.kgromov.drugstore.ui.model.DrugsFilter;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Route(value = "/")
@PageTitle("Drugs")
@RequiredArgsConstructor
public class DrugsListView extends VerticalLayout {
    private final DrugsRepository drugsRepository;
    private final DrugsUploader drugsUploader;
    private Grid<DrugsInfo> grid;

    @Override
    protected void onAttach(AttachEvent event) {
        if (event.isInitialAttach()) {
            this.configureGrid();
            // TODO: dialogue with toolbar button is the best option
//            add(this.createToolbar(), this.grid);
            add(this.drugsUploader, this.grid);
            this.subscribeToUploadChanges();
            setSizeFull();
        }
    }

    private void subscribeToUploadChanges() {
        this.drugsUploader.emitter()
                .filter(e -> e)
                .subscribe(_ -> getUI().ifPresent(ui -> ui.access(() -> {
                    grid.setItems(drugsRepository.findAll());
                    this.grid.getListDataView().refreshAll();
                })));
    }

    private void configureGrid() {
        this.grid = new Grid<>(DrugsInfo.class, false);
        Editor<DrugsInfo> editor = grid.getEditor();

        var nameColumn = grid.addColumn(DrugsInfo::getName)
                .setAutoWidth(true)
                .setSortable(true);
        var categoryColumn = grid.addColumn(DrugsInfo::getCategory)
                .setSortable(true)
                .setWidth("150px").setFlexGrow(0);
        var formTypeColumn = grid.addColumn(DrugsInfo::getForm)
                .setSortable(true)
                .setWidth("150px").setFlexGrow(0);
        var expirationDateColumn = grid.addColumn(DrugsInfo::getExpirationDate)
                .setSortable(true)
                .setWidth("150px").setFlexGrow(0);

        var editColumn = grid.addComponentColumn(drug -> {
            Button editButton = new Button(VaadinIcon.PENCIL.create());
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(drug);
            });
            return editButton;
        }).setWidth("50px").setFlexGrow(0);

        var deleteColumn = grid.addComponentColumn(drug -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                drugsRepository.delete(drug);
                grid.setItems(drugsRepository.findAll());
            });
            return deleteButton;
        }).setWidth("50px").setFlexGrow(0);

        Binder<DrugsInfo> binder = new Binder<>(DrugsInfo.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField nameField = new TextField();
        nameField.setWidthFull();
        binder.forField(nameField)
                .asRequired("Drugs name must not be empty")
//                .withStatusLabel()
                .bind(DrugsInfo::getName, DrugsInfo::setName);
        nameColumn.setEditorComponent(nameField);

        ComboBox<Category> categoryField = new ComboBox<>();
        categoryField.setItems(List.of(Category.values()));
        categoryField.setItemLabelGenerator(Category::name);
        categoryField.setWidthFull();
        binder.forField(categoryField).asRequired("Category must not be empty")
//                .withStatusLabel(lastNameValidationMessage)
                .bind(DrugsInfo::getCategory, DrugsInfo::setCategory);
        categoryColumn.setEditorComponent(categoryField);

        ComboBox<DrugsForm> formTypeField = new ComboBox<>();
        formTypeField.setItems(List.of(DrugsForm.values()));
        formTypeField.setItemLabelGenerator(DrugsForm::name);
        formTypeField.setWidthFull();
        binder.forField(formTypeField).asRequired("Form type must not be empty")
//                .withStatusLabel(lastNameValidationMessage)
                .bind(DrugsInfo::getForm, DrugsInfo::setForm);
        formTypeColumn.setEditorComponent(formTypeField);

        DatePicker expirationDateField = new DatePicker();
        expirationDateField.setWidthFull();
        binder.forField(expirationDateField).asRequired("Expiration date must not be empty")
//                .withStatusLabel(lastNameValidationMessage)
                .bind(DrugsInfo::getExpirationDate, DrugsInfo::setExpirationDate);
        expirationDateColumn.setEditorComponent(expirationDateField);
        this.setupGridEditor(editColumn);

        getUI().ifPresent(ui -> {
            var items = drugsRepository.findAll();
            ui.access(() -> {
                grid.setItems(items);
                DrugsFilter filter = new DrugsFilter(grid.getListDataView());
                HeaderRow headerRow = grid.appendHeaderRow();
                headerRow.getCell(nameColumn).setComponent(this.createFilterHeader("Name", filter::setName));
                headerRow.getCell(categoryColumn).setComponent(this.createFilterHeader("Category", filter::setCategory));
                headerRow.getCell(formTypeColumn).setComponent(this.createFilterHeader("Form", filter::setForm));
                headerRow.getCell(expirationDateColumn).setComponent(this.createFilterHeader("Expiration date", filter::setExpirationDate));
            });
        });
    }

    private Component createFilterHeader(String header, Consumer<String> filterChanges) {
        NativeLabel label = new NativeLabel(header);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(e -> filterChanges.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }

    // TODO:makes sense only as alternative to statically defined uploader - to show in dialogue
    private Component createToolbar() {
        Button uploadButton = new Button("Upload", VaadinIcon.ARROW_UP.create());
        HorizontalLayout toolbar = new HorizontalLayout(uploadButton);
        return toolbar;
    }

    private void setupGridEditor(Grid.Column<DrugsInfo> editColumn) {
        Editor<DrugsInfo> editor = grid.getEditor();
        Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            if (editor.save()) {
                DrugsInfo updatedDrugs = editor.getBinder().getBean();
                drugsRepository.update(updatedDrugs);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
    }
}
