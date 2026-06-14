package org.ifsp.scholardesktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ifsp.scholardesktop.dao.impl.SchoolDAOImpl;
import org.ifsp.scholardesktop.dao.impl.TeacherDAOImpl;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.service.AuthService;

import java.io.IOException;

public class RegisterController {

    private AuthService authService;

    public RegisterController() {
        this.authService = new AuthService(
                new TeacherDAOImpl(),
                new SchoolDAOImpl()
        );
    }

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField schoolNameField;

    @FXML
    private TextField teacherNameField;

    @FXML
    void handleBack(ActionEvent event) {
        navigateToLogin();
    }

    @FXML
    void handleRegister(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String schoolName = schoolNameField.getText();
        String teacherName = teacherNameField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || schoolName.isEmpty() || teacherName.isEmpty()) {
            errorLabel.setText("Preencha todos os campos");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Insira um email válido!");
            return;
        }

        try {
            authService.register(teacherName, email, password, confirmPassword, schoolName);
            navigateToLogin();
        } catch (InvalidOperationException e) {
            errorLabel.setText(e.getMessage());
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/ifsp/scholardesktop/view/login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CNA box");

        } catch (IOException e) {
            errorLabel.setText("Erro ao carregar tela de login.");
        }
    }

}
