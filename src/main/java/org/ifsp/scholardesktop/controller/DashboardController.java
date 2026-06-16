package org.ifsp.scholardesktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.ifsp.scholardesktop.dao.impl.ActivityDAOImpl;
import org.ifsp.scholardesktop.dao.impl.ClassGroupDAOImpl;
import org.ifsp.scholardesktop.dao.impl.StudentDAOImpl;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.*;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.service.*;

import java.util.List;

public class DashboardController {

    private Teacher teacher;
    private ClassGroup selectedClassGroup;

    private final ClassGroupService classGroupService;
    private final StudentService studentService;
    private final ActivityService activityService;
    private final GradeService gradeService;

    public DashboardController() {
        this.classGroupService = new ClassGroupService(new ClassGroupDAOImpl());
        this.studentService = new StudentService(new StudentDAOImpl(), new ClassGroupDAOImpl());
        this.activityService = new ActivityService(new ActivityDAOImpl());
        this.gradeService = new GradeService();
    }

    @FXML private Label teacherNameLabel;
    @FXML private Label teacherAvatarLabel;
    @FXML private ListView<ClassGroup> classGroupListView;
    @FXML private ListView<Student> studentListView;
    @FXML private Label classNameLabel;
    @FXML private Label classModuleLabel;
    @FXML private Label classTeacherLabel;
    @FXML private Label classStudentCountLabel;
    @FXML private Label studentCountBadge;
    @FXML private ComboBox<Student> activityStudentCombo;
    @FXML private ComboBox<ActivityType> activityTypeCombo;
    @FXML private TextField activityGradeField;
    @FXML private Label activityErrorLabel;

    @FXML
    public void initialize() {
        setupClassGroupListView();
        setupStudentListView();
        setupClassGroupListener();
        setupComboConverters();
    }

    public void initData(Teacher teacher) {
        this.teacher = teacher;
        teacherNameLabel.setText(teacher.getName());
        teacherAvatarLabel.setText(getInitials(teacher.getName()));
        loadClassGroups();
    }

    private void setupClassGroupListView() {
        classGroupListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ClassGroup item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox container = new VBox(2);
                    Label nameLabel = new Label(item.getName());
                    nameLabel.getStyleClass().add("class-name-label");
                    Label moduleLabel = new Label(item.getModule().getName());
                    moduleLabel.getStyleClass().add("class-module-label");
                    container.getChildren().addAll(nameLabel, moduleLabel);
                    setGraphic(container);
                    setText(null);
                }
            }
        });
    }

    private void setupStudentListView() {
        studentListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox();
                    container.setMaxWidth(Double.MAX_VALUE);

                    Label nameLabel = new Label(item.getName());
                    nameLabel.getStyleClass().add("student-name-label");
                    HBox.setHgrow(nameLabel, Priority.ALWAYS);
                    nameLabel.setMaxWidth(Double.MAX_VALUE);

                    List<Activity> activities = activityService.findByStudent(item.getId());
                    double grade = gradeService.calculateGrade(activities);
                    Label gradeLabel = new Label(String.format("%.1f%%", grade));

                    if (grade >= 70) gradeLabel.getStyleClass().add("grade-label");
                    else if (grade >= 50) gradeLabel.getStyleClass().add("grade-label-mid");
                    else gradeLabel.getStyleClass().add("grade-label-low");

                    container.getChildren().addAll(nameLabel, gradeLabel);
                    setGraphic(container);
                    setText(null);
                }
            }
        });
    }

    private void setupClassGroupListener() {
        classGroupListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) loadClassGroupDetails(newVal);
                }
        );
    }

    private void setupComboConverters() {
        activityStudentCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Student student) {
                return student == null ? "" : student.getName();
            }

            @Override
            public Student fromString(String string) {
                return null;
            }
        });

        activityTypeCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(ActivityType type) {
                return type == null ? "" : type.getLabel();
            }

            @Override
            public ActivityType fromString(String string) {
                return null;
            }
        });
    }

    private void loadClassGroups() {
        List<ClassGroup> classGroups = classGroupService.getClassesByTeacher(teacher.getId());
        classGroupListView.getItems().setAll(classGroups);
    }

    private void loadClassGroupDetails(ClassGroup classGroup) {
        this.selectedClassGroup = classGroup;

        classNameLabel.setText(classGroup.getName());
        classModuleLabel.setText(classGroup.getModule().getName());
        classTeacherLabel.setText(classGroup.getTeacher().getName());

        List<Student> students = studentService.findByClassGroup(classGroup.getId());
        studentListView.getItems().setAll(students);
        studentCountBadge.setText(String.valueOf(students.size()));
        classStudentCountLabel.setText(String.valueOf(students.size()));

        activityStudentCombo.getItems().setAll(students);
        activityTypeCombo.getItems().setAll(ActivityType.values());
        activityErrorLabel.setText("");
    }

    private void refreshStudents() {
        if (selectedClassGroup != null) {
            List<Student> students = studentService.findByClassGroup(selectedClassGroup.getId());
            studentListView.getItems().setAll(students);
            studentCountBadge.setText(String.valueOf(students.size()));
            classStudentCountLabel.setText(String.valueOf(students.size()));
            activityStudentCombo.getItems().setAll(students);
        }
    }

    @FXML
    void handleAddClassGroup(ActionEvent event) {
        ChoiceDialog<Module> dialog = new ChoiceDialog<>(Module.values()[0], Module.values());
        dialog.setTitle("Nova Turma");
        dialog.setHeaderText("Selecione o módulo");
        dialog.setContentText("Módulo:");

        dialog.showAndWait().ifPresent(module -> {
            classGroupService.createClassGroup(module, teacher);
            loadClassGroups();
        });
    }

    @FXML
    void handleEditClassGroup(ActionEvent event) {
        if (selectedClassGroup == null) return;

        ChoiceDialog<Module> dialog = new ChoiceDialog<>(
                selectedClassGroup.getModule(), Module.values()
        );
        dialog.setTitle("Editar Turma");
        dialog.setHeaderText("Selecione o novo módulo");
        dialog.setContentText("Módulo:");

        dialog.showAndWait().ifPresent(module -> {
            classGroupService.updateModule(selectedClassGroup.getId(), module);
            loadClassGroups();
        });
    }

    @FXML
    void handleDeleteClassGroup(ActionEvent event) {
        if (selectedClassGroup == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Excluir Turma");
        alert.setHeaderText("Excluir " + selectedClassGroup.getName() + "?");
        alert.setContentText("Todos os alunos e atividades serão removidos.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                classGroupService.deleteClassGroup(selectedClassGroup.getId());
                selectedClassGroup = null;
                clearClassGroupDetails();
                loadClassGroups();
            }
        });
    }

    @FXML
    void handleAddStudent(ActionEvent event) {
        if (selectedClassGroup == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Aluno");
        dialog.setHeaderText("Adicionar aluno em " + selectedClassGroup.getName());
        dialog.setContentText("Nome completo:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                studentService.createStudent(name.trim(), selectedClassGroup);
                refreshStudents();
            }
        });
    }

    @FXML
    void handleSaveActivity(ActionEvent event) {
        activityErrorLabel.setText("");

        Student student = activityStudentCombo.getValue();
        ActivityType type = activityTypeCombo.getValue();
        String gradeText = activityGradeField.getText().trim();

        if (student == null || type == null || gradeText.isEmpty()) {
            activityErrorLabel.setText("Preencha todos os campos.");
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeText.replace(",", "."));
        } catch (NumberFormatException e) {
            activityErrorLabel.setText("Nota inválida.");
            return;
        }

        if (grade < 0 || grade > 10) {
            activityErrorLabel.setText("A nota deve estar entre 0 e 10.");
            return;
        }

        try {
            activityService.createActivity(type, grade, student);
            activityGradeField.clear();
            activityStudentCombo.setValue(null);
            activityTypeCombo.setValue(null);
            refreshStudents();
        } catch (InvalidOperationException e) {
            activityErrorLabel.setText(e.getMessage());
        } catch (RuntimeException e) {
            activityErrorLabel.setText(e.getMessage());
        }
    }

    private void clearClassGroupDetails() {
        classNameLabel.setText("");
        classModuleLabel.setText("");
        classTeacherLabel.setText("");
        classStudentCountLabel.setText("");
        studentCountBadge.setText("0");
        studentListView.getItems().clear();
        activityStudentCombo.getItems().clear();
        activityTypeCombo.getItems().clear();
    }

    private String getInitials(String fullName) {
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}