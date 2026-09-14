package co.edu.unicauca.lisw2_t02_g03.services;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {


    public String hash(String password){

        return BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );

    }


    public boolean verify(
            String password,
            String hashedPassword
    ){

        return BCrypt.checkpw(
                password,
                hashedPassword
        );

    }

}
