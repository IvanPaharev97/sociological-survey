package com.epam.training.survey.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Question implements Serializable {
    private static final long serialVersionUID = -1796032485897907034L;

    private int id;
    private Topic topic;
    private String question;
    
    public Question() {
        super();
    }

    public Question(Topic topic, String question) {
        super();
        this.topic = topic;
        this.question = question;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((question == null) ? 0 : question.hashCode());
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
        Question other = (Question) obj;
        if (question == null) {
            if (other.question != null)
                return false;
        } else if (!question.equals(other.question))
            return false;
        return true;
    }

}
