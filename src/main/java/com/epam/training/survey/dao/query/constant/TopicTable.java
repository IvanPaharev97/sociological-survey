package com.epam.training.survey.dao.query.constant;

public class TopicTable {
    public static final String TABLE_NAME = "topic ";
    public static final String ID_FIELD = "topic.id";
    public static final String NAME_FIELD = "topic.name";
    public static final String DESCROPTION_FIELD = "topic.descroption";
    public static final String INSERT_BODY = "(name, description) VALUES(?, ?) ";
    public static final String UPDATE_BODY = "SET name = ?, description = ? ";
    public static final String JOIN_QUESTION_TABLE = "JOIN question ON topic.id = question.topic_id ";
}
