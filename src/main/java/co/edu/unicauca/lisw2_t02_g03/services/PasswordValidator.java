package co.edu.unicauca.lisw2_t02_g03.services;

public class PasswordValidator {

    public boolean isValid(String password) {

        if(password == null || password.length() < 6) {
            return false;
        }


        boolean tieneMayuscula = false;
        boolean tieneNumero = false;
        boolean tieneEspecial = false;


        for(char c : password.toCharArray()) {


            if(Character.isUpperCase(c)) {
                tieneMayuscula = true;
            }


            if(Character.isDigit(c)) {
                tieneNumero = true;
            }


            if(!Character.isLetterOrDigit(c)) {
                tieneEspecial = true;
            }
        }


        return tieneMayuscula && tieneNumero && tieneEspecial;
    }
}