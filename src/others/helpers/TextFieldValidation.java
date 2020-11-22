package others.helpers;

import javafx.scene.control.TextField;

public class TextFieldValidation {


    public static boolean isFieldNotEmpty(TextField textField) {
        return textField.getText() != null && !textField.getText().isEmpty();
    }

    public static boolean isInputInteger(TextField textField) {
        return textField.getText().chars().allMatch(Character::isDigit);
    }

    public static boolean isTextFieldValid(TextField textField) {
        return TextFieldValidation.isFieldNotEmpty(textField) && TextFieldValidation.isInputInteger(textField);
    }
}
