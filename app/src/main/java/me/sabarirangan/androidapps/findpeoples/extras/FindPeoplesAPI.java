package me.sabarirangan.androidapps.findpeoples.extras;

import java.util.List;

import me.sabarirangan.androidapps.findpeoples.model.Comment;
import me.sabarirangan.androidapps.findpeoples.model.NewComment;
import me.sabarirangan.androidapps.findpeoples.model.NewProject;
import me.sabarirangan.androidapps.findpeoples.model.NewReview;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import me.sabarirangan.androidapps.findpeoples.model.Review;
import me.sabarirangan.androidapps.findpeoples.model.Tags;
import me.sabarirangan.androidapps.findpeoples.model.UserProfile;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
import me.sabarirangan.androidapps.findpeoples.model.Token;

public interface FindPeoplesAPI {
    @POST("api/login/token/")
    Call<Token> loginUser(@Body Token user);
    @GET("api/projects/{pageno}/")
    Call<List<Project>> getProjectList(@Header("Authorization") String s,@Path("pageno")String pageno);
    @PUT("api/project/{pid}/")
    Call<Project> updateProject(@Header("Authorization") String s,@Body NewProject p,@Path("pid") String pid);
    @DELETE("api/project/{pid}/")
    Call<Response<Void>> deleteProject(@Header("Authorization") String s, @Path("pid") String pid);
    @GET("api/userprofile/{uid}/")
    Call<UserProfile> getUserProfile(@Header("Authorization")String s,@Path("uid")String uid);
    @GET("api/tags/1/")
    Call<List<Tags>> getTags(@Header("Authorization")String s);
    @POST("api/editskills/")
    Call<UserProfile> editSkills(@Header("Authorization")String s, @Body List<String> t);
    @POST("api/projects/0/")
    Call<Result>postProject(@Header("Authorization") String s,@Body NewProject brief);
    @GET("api/projects/{pid}/comment/{pgno}/")
    Call<List<Comment>> getProjectComment(@Header("Authorization")String s, @Path("pid") String pid, @Path("pgno") String pgno);
    @POST("api/projects/{pid}/comment/0/")
    Call<NewComment> postComment(@Header("Authorization") String s,@Path("pid") String pid,@Body NewComment comment);
    @POST("api/projects/{pid}/reviews/")
    Call<NewReview> postReview(@Header("Authorization") String s, @Path("pid") String pid, @Body NewReview review);
    @PUT("api/projects/{pid}/reviews/")
    Call<NewReview> updatePostReview(@Header("Authorization") String s, @Path("pid") String pid, @Body NewReview review);
    @GET("api/user/{uid}/projects/{pgno}/")
    Call<List<Project>> getProjects(@Header("Authorization") String s,@Path("uid") String uid,@Path("pgno")String pgno);
    @POST("api/tags/projects/")
    Call<List<Project>> getProjectsByTag(@Header("Authorization") String s,@Body Result tag);
    @POST("api/fcmtoken/")
    Call<Result> sendFCMtoken(@Header("Authorization") String s,@Body Token t);
    @POST("api/fcmrevoke/")
    Call<Result> revokeFCM(@Header("Authorization") String s,@Body Token t);
    @GET("api/usercomments/{pgno}/")
    Call<List<Comment>> getMyComment(@Header("Authorization") String s,@Path("pgno") String pgno);
}
