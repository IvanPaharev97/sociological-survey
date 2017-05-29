package com.epam.training.survey.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Survey implements Serializable {
    private static final long serialVersionUID = 4609763926571049809L;

    private int id;
    private String name;
    private Date createdDate;
    private Date completedDate;
    private List<Question> questions;
    
    public Survey() {
        super();
    }

    public Survey(String name, Date createdDate, Date completedDate) {
        super();
        this.name = name;
        this.createdDate = createdDate;
        this.completedDate = completedDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((completedDate == null) ? 0 : completedDate.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
        Survey other = (Survey) obj;
        if (completedDate == null) {
            if (other.completedDate != null)
                return false;
        } else if (!completedDate.equals(other.completedDate))
            return false;
        if (createdDate == null) {
            if (other.createdDate != null)
                return false;
        } else if (!createdDate.equals(other.createdDate))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (questions == null) {
            if (other.questions != null)
                return false;
        } else if (!questions.equals(other.questions))
            return false;
        return true;
    }
 
}
