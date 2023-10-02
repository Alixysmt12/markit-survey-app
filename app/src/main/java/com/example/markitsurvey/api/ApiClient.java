package com.example.markitsurvey.api;

import static com.example.markitsurvey.api.Api.R3baseURL;
import static com.example.markitsurvey.api.Api.S3SiteUrl;

import com.example.markitsurvey.models.AppVersion;
import com.example.markitsurvey.models.ApplicationDeviceModel;
import com.example.markitsurvey.models.ApplicationDeviceResponse;
import com.example.markitsurvey.models.ApplicationLoginLogIdModel;
import com.example.markitsurvey.models.ApplicationLoginLogIdResponse;
import com.example.markitsurvey.models.ApplicationLoginLogModel;
import com.example.markitsurvey.models.ApplicationLoginLogResponse;
import com.example.markitsurvey.models.ProjectMetaDataModel;
import com.example.markitsurvey.models.ProjectOneOffSettingsModel;
import com.example.markitsurvey.models.ProjectSettingsModels;
import com.example.markitsurvey.models.QAStatusByUserIdModel;
import com.example.markitsurvey.models.QuestionnaireModel;
import com.example.markitsurvey.models.RegionModel;
import com.example.markitsurvey.models.ServerDateTime;
import com.example.markitsurvey.models.SyncModel;
import com.example.markitsurvey.models.SyncResponse;
import com.example.markitsurvey.models.UserLoginModel;
import com.example.markitsurvey.models.UserModelsResponse;
import com.example.markitsurvey.models.WaveModelResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClient {

    /////////////////For Login
    @Headers("Content-Type: application/json")
    @POST("simpleSurveyAuth")
    Observable<UserModelsResponse> doLogin(@Body UserLoginModel loginResponseModel);

    /////////////////For App Version
    @GET(S3SiteUrl + "applicationVersion/getActiveVersion/{versionNumber}")
    Observable<AppVersion> checkVersion(@Path("versionNumber") String versionNumber);

    /////////////////For Date Time
    @GET("getServerDateTime")
    Observable<ServerDateTime> checkDate();


    /////////////////For Post Application Device Detail
    @Headers("Content-Type: application/json")
    @POST(S3SiteUrl + "applicationDeviceDetail")
    Observable<ApplicationDeviceResponse> postApplicationDeviceDetailed(@Body ApplicationDeviceModel applicationDeviceModel);


    /////////////////For Post Application Login  log
    @Headers("Content-Type: application/json")
    @POST(S3SiteUrl + "applicationLoginLog")
    Observable<ApplicationLoginLogResponse> postApplicationLoginLog(@Body ApplicationLoginLogModel applicationLoginLogModel);


    /////////////////For Post Application Login  log ID
    @Headers("Content-Type: application/json")
    @POST(S3SiteUrl + "applicationSyncDataId/")
    Observable<ApplicationLoginLogIdResponse> postApplicationLoginLogId(@Body ApplicationLoginLogIdModel applicationLoginLogIdModel);


    /////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////Projects Detailed Api's
    /////////////////For get Questionnaire Version
    @GET("checkQuestionnaireVersionByProjectId/{projectId}")
    Observable<QuestionnaireModel> getQuestionnaireVersion(@Path("projectId") Integer projectId);


    /////////////////For get Region
    @GET("getUserRegionProjectWise/{userId}/{projectId}")
    Observable<RegionModel> getRegions(@Path("userId") Integer userId,
                                       @Path("projectId") Integer projectId);

    ///////////////For get Get Section Questionnaire
    @GET("getProjectSectionByUser/{userId}/{projectId}")
    Observable<JsonObject> getQuestionnaire(@Path("userId") Integer userId,
                                            @Path("projectId") Integer projectId);

    ///////////////For get Get Wave
    @GET("projectWaveSettingByProjectId/{projectId}")
    Observable<WaveModelResponse> getWave(@Path("projectId") Integer projectId);


    ///////////////For get Get Project Classification One Off Setting By ProjectId
    @GET("projectClassificationOneOffSettingByProjectId/{projectId}")
    Observable<ProjectOneOffSettingsModel> getOneOffSetting(@Path("projectId") Integer projectId);

    /////////////////QA Status Pass Fail
    @GET("getCustomizedResearchQAStatusByUserId/{userId}/{projectId}/{waveName}")
    Observable<QAStatusByUserIdModel> getCustomizedResearchQAStatusByUserId(@Path("userId") Integer projectId,
                                                                            @Path("projectId") Integer userId,
                                                                            @Path("waveName") String waveName);

    /////////////////////Get Project Meta Data
    @GET("getMetaData/{projectId}")
    Observable<ProjectMetaDataModel> getMetaData(@Path("projectId") Integer projectId);

    /////////////////////Get Project Setting By ProjectId
    @GET("getProjectSettingByProjectId/{projectId}")
    Observable<ProjectSettingsModels> getProjectSettingByProjectId(@Path("projectId") Integer projectId);


    //////////////For Sync Data
    @Headers("Content-Type: application/json")
    @POST(R3baseURL + "syncSimpleSurveyData")
    Observable<SyncResponse> syncSimpleSurveyData(@Body String syncModel);




}

