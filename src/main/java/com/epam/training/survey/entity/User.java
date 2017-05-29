package com.epam.training.survey.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 2642988614494923906L;
    
    private int id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String userInfo;
    private List<Role> roles;
    private List<Answer> answers;
    
    public User() {
        super();
    }

    public User(
            String email, 
            String password, 
            String firstname, 
            String lastname, 
            String phone, 
            String userInfo) {
        super();
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.userInfo = userInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    } 
}
