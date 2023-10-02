package com.example.markitsurvey.JavaScriptInterface;

import android.content.Context;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.markitsurvey.DBDAO.SurveyDAO;
import com.example.markitsurvey.R;
import com.example.markitsurvey.helper.KeyValueDB;
import com.example.markitsurvey.helper.Utility;
import com.example.markitsurvey.logger.AppLogger;
import com.example.markitsurvey.models.AudioData;
import com.example.markitsurvey.models.GPSPoint;
import com.example.markitsurvey.models.ProjectModel;
import com.example.markitsurvey.models.Questionnaire;
import com.example.markitsurvey.models.Survey;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaScriptInterface {

    AppCompatActivity mContext;
    String questionnaireJSON = "";
    String quotaJSON = "";
    KeyValueDB keyValueDB;
    Survey _survey;
    List<AudioData> audioDataList;

    public JavaScriptInterface(AppCompatActivity c, Questionnaire questionnaire, Survey survey) {
        mContext = c;
        this.questionnaireJSON = questionnaire.getQuestionnaireJSON();
        _survey = survey;
        audioDataList = new ArrayList<>();
        keyValueDB = new KeyValueDB(c.getSharedPreferences(c.getResources().getString(R.string.keyValueDB), Context.MODE_PRIVATE));
    }

    @JavascriptInterface
    public String getFromAndroid() {

        return questionnaireJSON;
    }


    @JavascriptInterface
    public String getQuotaAndroid() {

        return quotaJSON;
    }
    @JavascriptInterface
    public String getAnswerJSON() {

        return _survey.getAnswerJSON()==null?new Gson().toJson(""):_survey.getAnswerJSON();
    }

    @JavascriptInterface
    public void sendData(String data,String surveyData) {

        ProjectModel project = ProjectModel.getProjectById(keyValueDB.getValue("projectJSON", ""),_survey.getProjectId());
        for (int i = 0; i < audioDataList.size(); i++)
        {
            Utility.convertToAudio(mContext,audioDataList.get(i),project.getProjectName());
        }
        AppLogger.i("saving lat",keyValueDB.getValue("qnrlat",""));
        AppLogger.i("saving lng",keyValueDB.getValue("qnrlng",""));
        Double lat = Double.parseDouble(keyValueDB.getValue("qnrlat",""));
        Double lng = Double.parseDouble(keyValueDB.getValue("qnrlng",""));
        _survey.setLat(lat);
        _survey.setLng(lng);
        _survey.setEndTime(Utility.getCurrentDateTime());
        _survey.setLOI(Utility.getLOI(_survey.getEndTime(),_survey.getStartTime()));
        _survey.setAnswerJSON(surveyData);
        _survey.setParseJSON(data);
        String gpsPoints =   keyValueDB.getValue("gpsPoints","");
        _survey.setGpsPoints(GPSPoint.ConvertToEntity(gpsPoints));
        //  _survey.setDisplayMeta(new Gson().toJson(DisplayMeta.convertToEntity(data)));

        setNextPermutation(surveyData);
        SurveyDAO surveyDAO = new SurveyDAO(mContext);
        surveyDAO.add(_survey);

        keyValueDB.save("qnrlat","");
        keyValueDB.save("qnrlat","");
        AppLogger.i("Survey","Complete");
        mContext.finish();


    }


    public  byte[] addAll(final byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
    @JavascriptInterface
    public void sendAudioData(String data, String name,String valName)
    {
        AudioData audioData = null;
        List<byte[]> blocks = new ArrayList<>();
        boolean audioFound = false;


        for (int i=0; i < audioDataList.size(); i++)
        {
            if (valName.equalsIgnoreCase(audioDataList.get(i).getValName()))
            {
                audioFound = true;
                byte[] decodedByte = Base64.decode(audioDataList.get(i).getValue().split(",")[1], 0);
                blocks.add(decodedByte);
                byte[] currentByteArray = Base64.decode(data.split(",")[1], 0);
                blocks.add(currentByteArray);
                byte[] joinedByteArray =    concatenateByteArrays(blocks);
                String encoded = "data:audio/webm;codecs=opus;base64,"+ Base64.encodeToString(joinedByteArray, Base64.DEFAULT);
                audioData = new AudioData();
                audioData.setName(name);
                audioData.setValue(encoded);
                audioData.setValName(valName);
                audioDataList.set(i,audioData);
            }
        }

        if (!audioFound)
        {

            audioData = new AudioData();
            audioData.setName(name);
            audioData.setValue(data);
            audioData.setValName(valName);
            audioDataList.add(audioData);
        }

    }
    public byte[] concatenateByteArrays(List<byte[]> blocks) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (byte[] b : blocks) {
            os.write(b, 0, b.length);
        }
        return os.toByteArray();
    }

    @JavascriptInterface
    public boolean getLeftToRightSettings() {

        boolean textAlignment = Boolean.parseBoolean(keyValueDB.getValue("textAlignment", ""));
        return textAlignment;
    }

    @JavascriptInterface
    public boolean getUrduFontSettings() {

        boolean urduFontSettings = Boolean.parseBoolean(keyValueDB.getValue("urduFont", ""));
        return urduFontSettings;
    }

    @JavascriptInterface
    public int getFontSize() {

        int fontSize = Integer.parseInt(keyValueDB.getValue("fontSize", "").isEmpty() ? "14"
                : keyValueDB.getValue("fontSize", ""));
        return fontSize;
    }

    private void createPermutationList()
    {
        List<String> videoPermutationsList = new ArrayList<>();
        videoPermutationsList.add("1,2");
        videoPermutationsList.add("2,3");
        videoPermutationsList.add("3,4");
        videoPermutationsList.add("4,5");
        videoPermutationsList.add("5,6");
        videoPermutationsList.add("6,1");
        String permutation =  keyValueDB.getValue("permutation","");
        if (!permutation.isEmpty()) {
            for (int i = 0; i < videoPermutationsList.size(); i++) {

                if (videoPermutationsList.get(i).equalsIgnoreCase(permutation)) {
                    int val = i;
                    if (val == videoPermutationsList.size()-1)
                    {
                        keyValueDB.save("permutation", videoPermutationsList.get(0));
                    }
                    else {
                        keyValueDB.save("permutation", videoPermutationsList.get(val + 1));
                    }
                }
            }
        } else {
            keyValueDB.save("permutation", videoPermutationsList.get(0));
        }

    }

    private void setNextPermutation(String JSON)
    {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject.has("thankyou")) {
                if (jsonObject.getString("thankyou").equalsIgnoreCase("1")) {

                    createPermutationList();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
