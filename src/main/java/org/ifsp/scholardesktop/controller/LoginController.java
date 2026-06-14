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
import org.ifsp.scholardesktop.model.Teacher;
import org.ifsp.scholardesktop.service.AuthService;

import java.io.IOException;

public class LoginController {

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService(
                new TeacherDAOImpl(),
                new SchoolDAOImpl()
        );
    }

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Preencha todos os campos");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Insira um email válido!");
            return;
        }

        try  {
            Teacher teacher = authService.login(email, password);
            navigateToDashboard(teacher);
        } catch (InvalidOperationException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        navigateToRegister();
    }

    private void navigateToDashboard(Teacher teacher) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/ifsp/scholardesktop/view/dashboard.fxml")
            );
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(teacher);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CNA box");

        } catch (IOException e) {
            errorLabel.setText("Erro ao carregar o dashboard.");
        }
    }

    private void navigateToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/ifsp/scholardesktop/view/register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CNA box");

        } catch (IOException e) {
            errorLabel.setText("Erro ao carregar tela de registro.");
        }
    }

}
