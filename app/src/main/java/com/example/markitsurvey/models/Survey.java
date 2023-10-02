package com.example.markitsurvey.models;

import android.database.Cursor;

import com.example.markitsurvey.logger.AppLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {


    private long id;
    private String guid;
    private int userId;
    private int projectId;
    private int sectionId;
    private int questionnaireId;
    private double lat;
    private double lng;
    private String answerJSON;
    private String parseJSON;
    private String startTime;
    private String endTime;
    private String subAreaName;
    private int subAreaId;
    private int countryId;
    private int stateId;
    private int cityId;
    private int superAreaId;
    private int areaId;
    private int level;
    private List<GPSPoint> gpsPoints;
    private String signBoardImg;
    private long LOI;
    private String displayMeta;
    private String surveyNature;
    private String syncStatus;
    private String visitMonth;
    private String fieldStartDate;
    private String fieldEndDate;
    private String questionnaireVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }



    public String getAnswerJSON() {
        return answerJSON;
    }

    public void setAnswerJSON(String answerJSON) {
        this.answerJSON = answerJSON;
    }

    public String getParseJSON() {
        return parseJSON;
    }

    public void setParseJSON(String parseJSON) {
        this.parseJSON = parseJSON;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(int subAreaId) {
        this.subAreaId = subAreaId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getSubAreaName() {
        return subAreaName;
    }

    public void setSubAreaName(String subAreaName) {
        this.subAreaName = subAreaName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getSuperAreaId() {
        return superAreaId;
    }

    public void setSuperAreaId(int superAreaId) {
        this.superAreaId = superAreaId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }




    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<GPSPoint> getGpsPoints() {
        return gpsPoints;
    }

    public void setGpsPoints(List<GPSPoint> gpsPoints) {
        this.gpsPoints = gpsPoints;
    }

    public String getSignBoardImg() {
        return signBoardImg;
    }

    public void setSignBoardImg(String signBoardImg) {
        this.signBoardImg = signBoardImg;
    }

    public long getLOI() {
        return LOI;
    }

    public void setLOI(long LOI) {
        this.LOI = LOI;
    }

    public String getDisplayMeta() {
        return displayMeta;
    }

    public void setDisplayMeta(String displayMeta) {
        this.displayMeta = displayMeta;
    }

    public String getSurveyNature() {
        return surveyNature;
    }

    public void setSurveyNature(String surveyNature) {
        this.surveyNature = surveyNature;
    }

    public String getVisitMonth() {
        return visitMonth;
    }

    public void setVisitMonth(String visitMonth) {
        this.visitMonth = visitMonth;
    }

    public String getFieldStartDate() {
        return fieldStartDate;
    }

    public void setFieldStartDate(String fieldStartDate) {
        this.fieldStartDate = fieldStartDate;
    }

    public String getFieldEndDate() {
        return fieldEndDate;
    }

    public void setFieldEndDate(String fieldEndDate) {
        this.fieldEndDate = fieldEndDate;
    }

    public String getQuestionnaireVersion() {
        return questionnaireVersion;
    }

    public void setQuestionnaireVersion(String questionnaireVersion) {
        this.questionnaireVersion = questionnaireVersion;
    }

    public static String TABLE_NAME = "tSurvey";
    public static String FIELD_ID = "Id";
    public static String FIELD_USER_ID = "UserId";
    public static String FIELD_PROJECT_ID = "ProjectId";
    public static String FIELD_SECTION_ID = "SectionId";
    public static String FIELD_QUESTIONNAIRE_ID = "QuestionnaireID";
    public static String FIELD_PARSE_JSON = "ParseJSON";
    public static String FIELD_ANSWER_JSON = "AnswerJson";
    public static String FIELD_LAT = "Lat";
    public static String FIELD_LNG = "Lng";
    public static String FIELD_START_TIME = "StartDateTime";
    public static String FIELD_ENDTIME = "EndDateTime";
    public static String FIELD_GUID = "GUID";
    public static String FIELD_COUNTRY_CODE = "CountryCode";
    public static String FIELD_STATE_CODE = "StateCode";
    public static String FIELD_CITY_CODE = "CityCode";
    public static String FIELD_SUPER_AREA_CODE = "SuperAreaCode";
    public static String FIELD_AREA_CODE = "AreaCode";
    public static String FIELD_SUBAREA_CODE = "SubAreaCode";
    public static String FIELD_SUBAREA_NAME = "SubAreaName";
    public static String FIELD_LEVEL = "level";
    public static String FIELD_LOI = "LOI";
    public static String FIELD_DISPLAY_META = "DisplayMeta";
    public static String FIELD_SURVEY_NATURE = "SurveyNature";
    public static String FIELD_SYNC_STATUS = "syncStatus";
    public static String FIELD_VISIT_MONTH = "VisitMonth";
    public static String FIELD_FIELD_START_DATE = "fieldStartDate";
    public static String FIELD_FIELD_END_DATE = "fieldEndDate";
    public static String FIELD_QUESTIONNAIRE_VERSION = "questionnaireVersion";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + FIELD_USER_ID + " INTEGER,"
            + FIELD_PROJECT_ID + " INTEGER,"
            + FIELD_SECTION_ID + " INTEGER,"
            + FIELD_QUESTIONNAIRE_ID + " INTEGER,"
            + FIELD_PARSE_JSON + " TEXT,"
            + FIELD_ANSWER_JSON + " TEXT,"
            + FIELD_LAT + " TEXT,"
            + FIELD_LNG + " TEXT,"
            + FIELD_START_TIME + " TEXT,"
            + FIELD_ENDTIME + " TEXT,"
            + FIELD_GUID + " TEXT,"
            + FIELD_COUNTRY_CODE + " INTEGER,"
            + FIELD_STATE_CODE + " INTEGER,"
            + FIELD_CITY_CODE + " INTEGER,"
            + FIELD_SUPER_AREA_CODE + " INTEGER,"
            + FIELD_AREA_CODE + " INTEGER,"
            + FIELD_SUBAREA_CODE + " INTEGER,"
            + FIELD_SUBAREA_NAME + " TEXT,"
            + FIELD_LEVEL + " TEXT,"
            + FIELD_LOI + " INTEGER,"
            + FIELD_DISPLAY_META + " TEXT,"
            + FIELD_SURVEY_NATURE + " TEXT,"
            + FIELD_SYNC_STATUS + " TEXT,"
            + FIELD_VISIT_MONTH + " TEXT,"
            + FIELD_FIELD_START_DATE + " TEXT,"
            + FIELD_FIELD_END_DATE + " TEXT,"
            + FIELD_QUESTIONNAIRE_VERSION + " TEXT"
            + ")";

    public static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;
    public static String getSurvey(long Id)
    {

        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID +" = "+ Id ;
        return QUERY;
    }
    public static String getNewSurvey()
    {

        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_SURVEY_NATURE +" = 'New'" ;
        return QUERY;
    }
    public static String getSurveyById(long  id)
    {

        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID +" = "+ id ;
        return QUERY;
    }
    public static String getSurveyByProjectId(int  projectId)
    {

        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_PROJECT_ID +" = "+ projectId ;
        return QUERY;
    }
    public static Survey ConvertToEntity(Cursor cursor) {
        Survey survey = new Survey();
        try{
            // AppLogger.i("DB cursor", ""+cursor.getColumnCount());
            survey.setId(cursor.getLong(0));
            survey.setUserId(cursor.getInt(1));
            survey.setProjectId(cursor.getInt(2));
            survey.setSectionId(cursor.getInt(3));
            survey.setQuestionnaireId(cursor.getInt(4));
            survey.setParseJSON(cursor.getString(5));
            survey.setAnswerJSON(cursor.getString(6));
            survey.setLat(cursor.getDouble(7));
            survey.setLng(cursor.getDouble(8));
            survey.setStartTime(cursor.getString(9));
            survey.setEndTime(cursor.getString(10  ));
            survey.setGuid(cursor.getString(11));
            survey.setCountryId(cursor.getInt(12));
            survey.setStateId(cursor.getInt(13));
            survey.setCityId(cursor.getInt(14));
            survey.setSuperAreaId(cursor.getInt(15));
            survey.setAreaId(cursor.getInt(16));
            survey.setSubAreaId(cursor.getInt(17));
            survey.setSubAreaName(cursor.getString(18));
            survey.setLevel(cursor.getInt(19));
            survey.setLOI(cursor.getInt(20));
            survey.setDisplayMeta(cursor.getString(21));
            survey.setSurveyNature(cursor.getString(22));
            survey.setVisitMonth(cursor.getString(24));
            survey.setFieldStartDate(cursor.getString(25));
            survey.setFieldEndDate(cursor.getString(26));
            survey.setQuestionnaireVersion(cursor.getString(27));
        }
        catch (Exception ex)
        {
            AppLogger.i("cursor Exception", ""+ex.getMessage().toString());
        }

        return survey;

    }


    public static ArrayList<Survey> ConvertToEntity(String json)
    {
        ArrayList<Survey> surveyArrayList = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Survey>>() {
            }.getType();
            surveyArrayList = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return surveyArrayList;

    }


}
