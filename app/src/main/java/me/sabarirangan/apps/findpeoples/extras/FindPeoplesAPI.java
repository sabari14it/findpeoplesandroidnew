package me.sabarirangan.apps.findpeoples.extras;

import java.util.List;

import me.sabarirangan.apps.findpeoples.model.NewProject;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Result;
import me.sabarirangan.apps.findpeoples.model.Tags;
import me.sabarirangan.apps.findpeoples.model.UserProfile;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import android.support.v7.widget.RecyclerView;

import java.util.List;

//import me.sabarirangan.apps.findpeoples.data.Avatar;
//import me.sabarirangan.apps.findpeoples.data.Category;
//import me.sabarirangan.apps.findpeoples.data.CategoryGson;
//import me.sabarirangan.apps.findpeoples.data.Comment;
//import me.sabarirangan.apps.findpeoples.data.GenericResponse;
//import me.sabarirangan.apps.findpeoples.data.MyProjectBrief;
//import me.sabarirangan.apps.findpeoples.data.MyProjectBriefData;
//import me.sabarirangan.apps.findpeoples.data.ProjectBrief;
//import me.sabarirangan.apps.findpeoples.data.ProjectTitle;
//import me.sabarirangan.apps.findpeoples.data.Result;
//import me.sabarirangan.apps.findpeoples.data.Review;
//import me.sabarirangan.apps.findpeoples.data.ReviewGet;
//import me.sabarirangan.apps.findpeoples.data.Skill;
//import me.sabarirangan.apps.findpeoples.data.Tags;
//import me.sabarirangan.apps.findpeoples.data.Token;
//import me.sabarirangan.apps.findpeoples.data.UserBrief;
//import me.sabarirangan.apps.findpeoples.data.UserMe;
//import me.sabarirangan.apps.findpeoples.data.UserRegister;
import me.sabarirangan.apps.findpeoples.model.Token;

public interface FindPeoplesAPI {
//    @POST("auth/register/")
//    Call<UserRegister> registerUser(@Body UserRegister user);
    @POST("api/login/token/")
    Call<Token> loginUser(@Body Token user);
    @GET("api/projects/")
    Call<List<Project>> getProjectList(@Header("Authorization") String s);
    @GET("api/userprofile/")
    Call<UserProfile> getUserProfile(@Header("Authorization")String s);
    @GET("api/myprojects/")
    Call<List<Project>> getMyProjects(@Header("Authorization")String s);
    @GET("api/tags/0")
    Call<List<Tags>> getTags(@Header("Authorization")String s);
    @POST("api/editskills/")
    Call<Result> editSkills(@Header("Authorization")String s, @Body List<String> t);
    @POST("api/projects/")
    Call<Result>postProject(@Header("Authorization") String s,@Body NewProject brief);
//    @GET("tags/0/")
//    Call<List<Skill>> getUserSkills(@Header("Authorization") String s);
//    @POST("projects/")
//    Call<MyProjectBrief>postProject(@Header("Authorization") String s,@Body MyProjectBriefData brief);
//    @POST("reviews/")
//    Call<ReviewGet> postReview(@Header("Authorization") String s,@Body Review review);
//    @GET("tags/1/")
//    Call<List<CategoryGson>> getCategories(@Header("Authorization") String s);
//    @POST("tags/1/")
//    Call<Tags> postTag(@Header("Authorization") String s,@Body Tags t);
//    @Multipart
//    @POST("uploadprofilepic/")
//    Call<Result> uploadImage(@Header("Authorization") String s,@Part MultipartBody.Part file);
//    @GET("userprofile/")
//    Call<Avatar> getProfilePic(@Header("Authorization")String s);
//    @GET("myprojects/")
//    Call<List<ProjectTitle>> getMyProjects(@Header("Authorization")String s);
//    @POST("editskills/")
//    Call<Result> editSkills(@Header("Authorization")String s,@Body List<String> t);
//    @GET("reviews/")
//    Call<List<Comment>> getReview(@Header("Authorization")String s);
//    @GET("getprojectreview/{pro_id}/")
//    Call<List<ReviewGet>> getProjectReview(@Header("Authorization")String s, @Path("pro_id") String u);
//    @POST("changeemail/")
//    Call<GenericResponse> changeEmail(@Header("Authorization")String s,@Body GenericResponse g);
//    @POST("changemobilenumber/")
//    Call<GenericResponse> changeMobileNumber(@Header("Authorization")String s,@Body GenericResponse g);
//    @POST("changemobilenumber/")
//    Call<GenericResponse> setMobileNumber(@Body GenericResponse g);
//    @POST("changepassword/")
//    Call<GenericResponse> changePassword(@Header("Authorization")String s,@Body GenericResponse g);
//    @POST("getproject/")
//    Call<List<ProjectBrief>> getPrjectOnReview(@Header("Authorization")String s,@Body GenericResponse g);
//    @POST("getcomment/")
//    Call<List<Comment>> getProjectComment(@Header("Authorization")String s,@Body GenericResponse g);
//    @GET("projects/{pro_id}")
//    Call<ProjectBrief> getprojectDetails(@Header("Authorization")String s,@Path("pro_id")String id);
//    @GET("auth/me")
//    Class<UserMe> getMyDetails(@Header("Authorization")String s);
}
