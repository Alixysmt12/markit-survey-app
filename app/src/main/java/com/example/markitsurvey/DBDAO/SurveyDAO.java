package com.example.markitsurvey.DBDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.markitsurvey.dataAccess.DataBaseUtil;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.Survey;

import java.util.ArrayList;
import java.util.List;

public class SurveyDAO {

    Context context;
    private SQLiteDatabase db;
    DataBaseUtil dbUtil;

    public SurveyDAO(Context context)
    {
        this.context = context;
    }

    public long add(Survey survey) {
        long val = 0;
        try
        {
            dbUtil = new DataBaseUtil(context);
            db = dbUtil.openConnection();

            ContentValues values = new ContentValues();


            values.put(Survey.FIELD_USER_ID, survey.getUserId());
            values.put(Survey.FIELD_PROJECT_ID, survey.getProjectId());
            values.put(Survey.FIELD_SECTION_ID, survey.getSectionId());
            values.put(Survey.FIELD_QUESTIONNAIRE_ID, survey.getQuestionnaireId());
            values.put(Survey.FIELD_PARSE_JSON, survey.getParseJSON());
            values.put(Survey.FIELD_ANSWER_JSON, survey.getAnswerJSON());
            values.put(Survey.FIELD_LAT, survey.getLat());
            values.put(Survey.FIELD_LNG, survey.getLng());
            values.put(Survey.FIELD_START_TIME, survey.getStartTime());
            values.put(Survey.FIELD_ENDTIME, survey.getEndTime());
            values.put(Survey.FIELD_GUID, survey.getGuid());
            values.put(Survey.FIELD_COUNTRY_CODE, survey.getCountryId());
            values.put(Survey.FIELD_STATE_CODE, survey.getStateId());
            values.put(Survey.FIELD_CITY_CODE, survey.getCityId());
            values.put(Survey.FIELD_SUPER_AREA_CODE, survey.getSuperAreaId());
            values.put(Survey.FIELD_AREA_CODE, survey.getAreaId());
            values.put(Survey.FIELD_SUBAREA_CODE, survey.getSubAreaId());
            values.put(Survey.FIELD_SUBAREA_NAME, survey.getSubAreaName());
            values.put(Survey.FIELD_LEVEL, survey.getLevel());
            values.put(Survey.FIELD_LOI, survey.getLOI());
            values.put(Survey.FIELD_DISPLAY_META, survey.getDisplayMeta());
            values.put(Survey.FIELD_SURVEY_NATURE, survey.getSurveyNature());
            values.put(Survey.FIELD_VISIT_MONTH, survey.getVisitMonth());
            values.put(Survey.FIELD_FIELD_START_DATE, survey.getFieldStartDate());
            values.put(Survey.FIELD_FIELD_END_DATE, survey.getFieldEndDate());
            values.put(Survey.FIELD_QUESTIONNAIRE_VERSION, survey.getQuestionnaireVersion());
            val = db.insert(Survey.TABLE_NAME, null, values);
            return  val;

        } catch (SQLException e)
        {
            // AppLogger.i("DB Exception", e.toString());

            return val;
        } finally
        {
            dbUtil.closeConnection();
        }
    }

    public List<Survey> getwSurveysByProjectId(int projectId) {
        List<Survey> surveyList = new ArrayList<Survey>();
        try {

            dbUtil = new DataBaseUtil(context);
            db = dbUtil.openConnection();

            Cursor cursor = db.rawQuery(Survey.getSurveyByProjectId(projectId), null);

            if (cursor.moveToFirst()) {
                do {
                    Survey survey = Survey.ConvertToEntity(cursor);

                    surveyList.add(survey);

                } while (cursor.moveToNext());
            }

            cursor.close();
            return surveyList;

        } catch (SQLException e) {
            //  AppLogger.i("DB Exception", e.toString());

            return surveyList;
        } finally {

            dbUtil.closeConnection();
        }
    }

    public Survey getSurvey(long shopCode) {
        Survey survey = null;
        try {

            dbUtil = new DataBaseUtil(context);
            db = dbUtil.openConnection();

            Cursor cursor = db.rawQuery(Survey.getSurvey(shopCode), null);

            if (cursor.moveToFirst()) {
                do {
                    survey= Survey.ConvertToEntity(cursor);



                } while (cursor.moveToNext());
            }

            cursor.close();
            return survey;

        } catch (SQLException e) {
            //  AppLogger.i("DB Exception", e.toString());

            return survey;
        } finally {

            dbUtil.closeConnection();
        }
    }
    public Survey getSurveyById(long id) {
        Survey survey = null;
        try {

            dbUtil = new DataBaseUtil(context);
            db = dbUtil.openConnection();

            Cursor cursor = db.rawQuery(Survey.getSurveyById(id), null);

            if (cursor.moveToFirst()) {
                do {
                    survey= Survey.ConvertToEntity(cursor);



                } while (cursor.moveToNext());
            }

            cursor.close();
            return survey;

        } catch (SQLException e) {
            //  AppLogger.i("DB Exception", e.toString());

            return survey;
        } finally {

            dbUtil.closeConnection();
        }
    }
    public Boolean delete() {
        try
        {

            dbUtil = new DataBaseUtil(context);
            this.db = dbUtil.openConnection();
            db.delete(Survey.TABLE_NAME, null, null);

            return true;

        } catch (Exception e)
        {
            // AppLogger.i("DB Exception", e.toString());
            return false;

        } finally
        {
            dbUtil.closeConnection();
        }

    }

    public Boolean delete(long id) {
        try {

            dbUtil = new DataBaseUtil(context);
            this.db = dbUtil.openConnection();
            db.delete(Survey.TABLE_NAME, Survey.FIELD_ID +" =?", new String[] { String.valueOf(id) });
            //  db.delete(Report.TABLE_NAME, null, null);
            AppLogger.i("Questionnaire Deleted",String.valueOf(id));
            return true;

        } catch (Exception e) {
            Log.e("DB Exception", e.toString());
            return false;

        } finally {
            dbUtil.closeConnection();
        }

    }



    public List<Survey> getAllSurvey() {
        List<Survey> surveyList = new ArrayList<Survey>();
        try {

            dbUtil = new DataBaseUtil(context);
            db = dbUtil.openConnection();

            Cursor cursor = db.rawQuery(Survey.SELECT_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Survey survey = Survey.ConvertToEntity(cursor);

                    surveyList.add(survey);

                } while (cursor.moveToNext());
            }

            cursor.close();
            return surveyList;

        } catch (SQLException e) {
            //  AppLogger.i("DB Exception", e.toString());

            return surveyList;
        } finally {

            dbUtil.closeConnection();
        }
    }
}
