package org.cacophonie.cacompagnon.utils;

import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VanillaAPI {
    private String remoteURL;
    private String sessionCookie = "";
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date defaultDate;

    private enum types {
        CATEGORIES,
        CATEGORY,
        DISCUSSION,

        DEBUG
    }

    public interface Callback {
        void onFinished(Object result);
    }

    public VanillaAPI(String remoteURL_p) {
        remoteURL = remoteURL_p;
        if (!remoteURL.endsWith("/"))
            remoteURL += "/";
        try {
            defaultDate = df.parse("1970-01-01 00:00:00");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSessionCookie(String cookie) {
        sessionCookie = cookie;
    }

    public void getCategories(Callback cb) {
        AsyncRequest req = new AsyncRequest();
        req.execute(remoteURL + "categories", types.CATEGORIES, cb);
    }

    private List<Category> parseCategories(InputStreamReader isr) {
        JsonReader jsonReader = new JsonReader(isr);
        List<Category> ret = new ArrayList<>();

        try {
            jsonReader.beginObject();
            if (jsonReader.hasNext() && jsonReader.nextName().equals("Categories")) {
                jsonReader.beginObject(); // Object "Categories"
                while (jsonReader.hasNext()) {
                    jsonReader.nextName(); // we don't care about the name, it's the ID
                    ret.add(new Category(jsonReader));
                }
                jsonReader.endObject();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void getCategory(int id, Callback cb) {
        AsyncRequest req = new AsyncRequest();
        req.execute(remoteURL + "categories/" + id, types.CATEGORY, cb);
    }

    private CategoryFull parseCategoryFull(InputStreamReader isr) {
        JsonReader jsonReader = new JsonReader(isr);
        return new CategoryFull(jsonReader);
    }

    public void getDiscussion(int id, Callback cb) {
        AsyncRequest req = new AsyncRequest();
        req.execute(remoteURL + "discussions/" + id, types.DISCUSSION, cb);
    }

    private DiscussionFull parseDiscussionFull(InputStreamReader isr) {
        JsonReader reader = new JsonReader(isr);
        return new DiscussionFull(reader) ;
    }

    private class AsyncRequest extends AsyncTask<Object, Void, Object> {
        private types mType;
        private Callback mCb;

        protected void onPostExecute(Object ret) {
            if (mType == types.DEBUG) {
                Log.d("JSON", (String) ret);
                return;
            }
            mCb.onFinished(ret);
        }

        protected Object doInBackground(Object... params) {
            InputStreamReader in;
            try {
                URL url = new URL((String) params[0]);
                mType = (types) params[1];
                mCb = (Callback) params[2];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (!sessionCookie.isEmpty()) {
                    conn.setRequestProperty("Cookie", sessionCookie);
                }
                in = new InputStreamReader(conn.getInputStream());
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d("HTTP", "The server returned an error");
                    return null;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            switch (mType) {
                case CATEGORIES:
                    return parseCategories(in);
                case CATEGORY:
                    return parseCategoryFull(in);
                case DISCUSSION:
                    return parseDiscussionFull(in);
                default:
                case DEBUG:
                    return new java.util.Scanner(in).useDelimiter("\\A").next();
            }
        }
    }

    public class Category {
        public int CategoryID;
        public int ParentCategoryID;
        public int TreeLeft;
        public int TreeRight;
        public int Depth;
        public int CountDiscussions;
        public int CountComments;
        public Date DateMarkedRead;
        public boolean AllowDiscussions;
        public boolean Archived;
        public String Name;
        public String UrlCode;
        public String Description;
        public int Sort;
        public String CssClass;
        public String Photo;
        public int PermissionCategoryID;
        public int PointsCategoryID;
        public boolean HideAllDiscussions;
        public String DisplayAs;
        public int InsertUserID;
        public int UpdateUserID;
        public Date DateInserted;
        public Date DateUpdated;
        public int LastCommentID;
        public int LastDiscussionID;
        public Date LastDateInserted;
        public String AllowedDiscussionTypes;
        public String DefaultDiscussionType;
        public boolean AllowFileUploads;
        public int CountAllDiscussions;
        public int CountAllComments;
        public String Url;
        public List<Integer> ChildIDs = new ArrayList<>();
        public String PhotoUrl;
        public boolean NoComment; // for Discussion but not Headings
        public String LastTitle;
        public int LastUserID;
        public int LastDiscussionUserID;
        public String LastUrl;
        public Date DateLastComment;
        public boolean Unfollow;
        public boolean Following;
        public boolean Read;
        public boolean PermsDiscussionsView;
        public boolean PermsDiscussionsAdd;
        public boolean PermsDiscussionsEdit;
        public boolean PermsCommentsAdd;
        public String LastName;
        public String LastEmail; // not for anonymous
        public String LastPhoto;

        public Category(int id) {
            CategoryID = id;
        }

        public String toString() {
            return Name + " (" + CategoryID + ") : " + Description;
        }

        public Category(JsonReader reader) {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "CategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                CategoryID = -1;
                                reader.nextNull();
                            }
                            else
                                CategoryID = reader.nextInt();
                            break;
                        case "ParentCategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                ParentCategoryID = -1;
                                reader.nextNull();
                            }
                            else
                                ParentCategoryID = reader.nextInt();
                            break;
                        case "TreeLeft":
                            if (reader.peek() == JsonToken.NULL) {
                                TreeLeft = -1;
                                reader.nextNull();
                            }
                            else
                                TreeLeft = reader.nextInt();
                            break;
                        case "TreeRight":
                            if (reader.peek() == JsonToken.NULL) {
                                TreeRight = -1;
                                reader.nextNull();
                            }
                            else
                                TreeRight = reader.nextInt();
                            break;
                        case "Depth":
                            if (reader.peek() == JsonToken.NULL) {
                                Depth = -1;
                                reader.nextNull();
                            }
                            else
                                Depth = reader.nextInt();
                            break;
                        case "CountDiscussions":
                            if (reader.peek() == JsonToken.NULL) {
                                CountDiscussions = -1;
                                reader.nextNull();
                            }
                            else
                                CountDiscussions = reader.nextInt();
                            break;
                        case "CountComments":
                            if (reader.peek() == JsonToken.NULL) {
                                CountComments = -1;
                                reader.nextNull();
                            }
                            else
                                CountComments = reader.nextInt();
                            break;
                        case "DateMarkedRead":
                            if (reader.peek() == JsonToken.NULL) {
                                DateMarkedRead = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateMarkedRead = df.parse(reader.nextString());
                            break;
                        case "AllowDiscussions":
                            if (reader.peek() == JsonToken.NULL) {
                                AllowDiscussions = false;
                                reader.nextNull();
                            }
                            else
                                AllowDiscussions = ItoB(reader.nextInt());
                            break;
                        case "Archived":
                            if (reader.peek() == JsonToken.NULL) {
                                Archived = false;
                                reader.nextNull();
                            }
                            else
                                Archived = ItoB(reader.nextInt());
                            break;
                        case "Name":
                            if (reader.peek() == JsonToken.NULL) {
                                Name ="";
                                reader.nextNull();
                            }
                            else
                                Name = reader.nextString();
                            break;
                        case "UrlCode":
                            if (reader.peek() == JsonToken.NULL) {
                                UrlCode = "";
                                reader.nextNull();
                            }
                            else
                                UrlCode = reader.nextString();
                            break;
                        case "Description":
                            if (reader.peek() == JsonToken.NULL) {
                                Description = "";
                                reader.nextNull();
                            }
                            else
                                Description = reader.nextString();
                            break;
                        case "Sort":
                            if (reader.peek() == JsonToken.NULL) {
                                Sort = -1;
                                reader.nextNull();
                            }
                            else
                                Sort = reader.nextInt();
                            break;
                        case "CssClass":
                            if (reader.peek() == JsonToken.NULL) {
                                CssClass = "";
                                reader.nextNull();
                            }
                            else
                                CssClass = reader.nextString();
                            break;
                        case "Photo":
                            if (reader.peek() == JsonToken.NULL) {
                                Photo = "";
                                reader.nextNull();
                            }
                            else
                                Photo = reader.nextString();
                            break;
                        case "PermissionCategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                PermissionCategoryID = -1;
                                reader.nextNull();
                            }
                            else
                                PermissionCategoryID = reader.nextInt();
                            break;
                        case "PointsCategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                PointsCategoryID = -1;
                                reader.nextNull();
                            }
                            else
                                PointsCategoryID = reader.nextInt();
                            break;
                        case "HideAllDiscussions":
                            if (reader.peek() == JsonToken.NULL) {
                                HideAllDiscussions = false;
                                reader.nextNull();
                            }
                            else
                                HideAllDiscussions = ItoB(reader.nextInt());
                            break;
                        case "DisplayAs":
                            if (reader.peek() == JsonToken.NULL) {
                                DisplayAs = "";
                                reader.nextNull();
                            }
                            else
                                DisplayAs = reader.nextString();
                            break;
                        case "InsertUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertUserID = -1;
                                reader.nextNull();
                            }
                            else
                                InsertUserID = reader.nextInt();
                            break;
                        case "UpdateUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateUserID = -1;
                                reader.nextNull();
                            }
                            else
                                UpdateUserID = reader.nextInt();
                            break;
                        case "DateInserted":
                            if (reader.peek() == JsonToken.NULL) {
                                DateInserted = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateInserted = df.parse(reader.nextString());
                            break;
                        case "DateUpdated":
                            if (reader.peek() == JsonToken.NULL) {
                                DateUpdated = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateUpdated = df.parse(reader.nextString());
                            break;
                        case "LastCommentID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastCommentID = -1;
                                reader.nextNull();
                            }
                            else
                                LastCommentID = reader.nextInt();
                            break;
                        case "LastDiscussionID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastDiscussionID = -1;
                                reader.nextNull();
                            }
                            else
                                LastDiscussionID = reader.nextInt();
                            break;
                        case "LastDateInserted":
                            if (reader.peek() == JsonToken.NULL) {
                                LastDateInserted = defaultDate;
                                reader.nextNull();
                            }
                            else
                                LastDateInserted = df.parse(reader.nextString());
                            break;
                        case "AllowedDiscussionTypes":
                            if (reader.peek() == JsonToken.NULL) {
                                AllowedDiscussionTypes = "";
                                reader.nextNull();
                            }
                            else
                                AllowedDiscussionTypes = reader.nextString();
                            break;
                        case "DefaultDiscussionType":
                            if (reader.peek() == JsonToken.NULL) {
                                DefaultDiscussionType = "";
                                reader.nextNull();
                            }
                            else
                                DefaultDiscussionType = reader.nextString();
                            break;
                        case "AllowFileUploads":
                            if (reader.peek() == JsonToken.NULL) {
                                AllowFileUploads = false;
                                reader.nextNull();
                            }
                            else
                                AllowFileUploads = ItoB(reader.nextInt());
                            break;
                        case "CountAllDiscussions":
                            if (reader.peek() == JsonToken.NULL) {
                                CountAllDiscussions = -1;
                                reader.nextNull();
                            }
                            else
                                CountAllDiscussions = reader.nextInt();
                            break;
                        case "CountAllComments":
                            if (reader.peek() == JsonToken.NULL) {
                                CountAllComments = -1;
                                reader.nextNull();
                            }
                            else
                                CountAllComments = reader.nextInt();
                            break;
                        case "Url":
                            if (reader.peek() == JsonToken.NULL) {
                                Url = "";
                                reader.nextNull();
                            }
                            else
                                Url = reader.nextString();
                            break;
                        case "ChildIDs":
                            if (reader.peek() != JsonToken.NULL)
                                ChildIDs = readListInt(reader);
                            break;
                        case "PhotoUrl":
                            if (reader.peek() == JsonToken.NULL) {
                                PhotoUrl = "";
                                reader.nextNull();
                            }
                            else
                                PhotoUrl = reader.nextString();
                            break;
                        case "NoComment":
                            if (reader.peek() == JsonToken.NULL) {
                                NoComment = false;
                                reader.nextNull();
                            }
                            else
                                NoComment = reader.nextBoolean();
                            break;
                        case "LastTitle":
                            if (reader.peek() == JsonToken.NULL) {
                                LastTitle = "";
                                reader.nextNull();
                            }
                            else
                                LastTitle = reader.nextString();
                            break;
                        case "LastUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastUserID = -1;
                                reader.nextNull();
                            }
                            else
                                LastUserID = reader.nextInt();
                            break;
                        case "LastDiscussionUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastDiscussionUserID = -1;
                                reader.nextNull();
                            }
                            else
                                LastDiscussionUserID = reader.nextInt();
                            break;
                        case "LastUrl":
                            if (reader.peek() == JsonToken.NULL) {
                                LastUrl = "";
                                reader.nextNull();
                            }
                            else
                                LastUrl = reader.nextString();
                            break;
                        case "DateLastComment":
                            if (reader.peek() == JsonToken.NULL) {
                                DateLastComment = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateLastComment = df.parse(reader.nextString());
                            break;
                        case "Unfollow":
                            if (reader.peek() == JsonToken.NULL) {
                                Unfollow = false;
                                reader.nextNull();
                            }
                            else
                                Unfollow = reader.nextBoolean();
                            break;
                        case "Following":
                            if (reader.peek() == JsonToken.NULL) {
                                Following = false;
                                reader.nextNull();
                            }
                            else
                                Following = reader.nextBoolean();
                            break;
                        case "Read":
                            if (reader.peek() == JsonToken.NULL) {
                                Read = false;
                                reader.nextNull();
                            }
                            else
                                Read = reader.nextBoolean();
                            break;
                        case "PermsDiscussionsView":
                            if (reader.peek() == JsonToken.NULL) {
                                PermsDiscussionsView = false;
                                reader.nextNull();
                            }
                            else
                                PermsDiscussionsView = reader.nextBoolean();
                            break;
                        case "PermsDiscussionsAdd":
                            if (reader.peek() == JsonToken.NULL) {
                                PermsDiscussionsAdd = false;
                                reader.nextNull();
                            }
                            else
                                PermsDiscussionsAdd = reader.nextBoolean();
                            break;
                        case "PermsDiscussionsEdit":
                            if (reader.peek() == JsonToken.NULL) {
                                PermsDiscussionsEdit = false;
                                reader.nextNull();
                            }
                            else
                                PermsDiscussionsEdit = reader.nextBoolean();
                            break;
                        case "PermsCommentsAdd":
                            if (reader.peek() == JsonToken.NULL) {
                                PermsCommentsAdd = false;
                                reader.nextNull();
                            }
                            else
                                PermsCommentsAdd = reader.nextBoolean();
                            break;
                        case "LastName":
                            if (reader.peek() == JsonToken.NULL) {
                                LastName = "";
                                reader.nextNull();
                            }
                            else
                                LastName = reader.nextString();
                            break;
                        case "LastEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                LastEmail = "";
                                reader.nextNull();
                            }
                            else
                                LastEmail = reader.nextString();
                            break;
                        case "LastPhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                LastPhoto = "";
                                reader.nextNull();
                            }
                            else
                                LastPhoto = reader.nextString();
                            break;
                        default:
                            Log.d("JSON", "Missed variable in Category: " + reader.peek());
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CategoryFull {
        public Category Category;
        public List<Category> Categories = new ArrayList<>();
        public int CategoryID;
        public String Sort;
        public List<Object> Filters = new ArrayList<>();
        public int CountDiscussions;
        public List<Discussion> AnnounceData = new ArrayList<>();
        public List<Discussion> Discussions = new ArrayList<>();

        public CategoryFull(JsonReader reader) {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "Category":
                            Category = new Category(reader);
                            break;
                        case "Categories":
                            // When there's no child categories the API return an empty ARRAY
                            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                                reader.beginArray();
                                reader.endArray();
                                break;
                            }

                            reader.beginObject();
                            while (reader.hasNext()) {
                                reader.skipValue(); // Here name = ID
                                Categories.add(new Category(reader));
                            }
                            reader.endObject();
                            break;
                        case "CategoryID":
                            CategoryID = reader.nextInt();
                            break;
                        case "Sort":
                            Sort = reader.nextString();
                            break;
                        case "Filters":
                            Filters = readListInt(reader);
                            break;
                        case "CountDiscussions":
                            CountDiscussions = reader.nextInt();
                            break;
                        case "AnnounceData":
                            reader.beginArray();
                            while (reader.hasNext())
                                AnnounceData.add(new Discussion(reader));
                            reader.endArray();
                            break;
                        case "Discussions":
                            Discussions = new ArrayList<>();
                            reader.beginArray();
                            while (reader.hasNext()) {
                                Discussions.add(new Discussion(reader));
                            }
                            reader.endArray();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Discussion {
        public int DiscussionID;
        public String Type;
        public int ForeignID;
        public int CategoryID;
        public int InsertUserID;
        public int UpdateUserID;
        public int FirstCommentID;
        public int LastCommentID;
        public String Name;
        public String Body;
        public String Format;
        public String Tags;
        public int CountComments;
        public int CountBookmarks;
        public int CountViews;
        public boolean Closed;
        public boolean Announce;
        public boolean Sink;
        public Date DateInserted;
        public Date DateUpdated;
        public String InsertIPAddress;
        public String UpdateIPAddress;
        public Date DateLastComment;
        public int LastCommentUserID;
        public int Score;
        public int RegardingID;
        public int WatchUserID;
        public Date DateLastViewed;
        public boolean Dismissed;
        public boolean Bookmarked;
        public int CountCommentWatch;
        public boolean Participated;
        public String Url;
        public String Category;
        public String CategoryUrlCode;
        public int PermissionCategoryID;
        public int FirstUserID;
        public Date FirstDate;
        public int LastUserID;
        public Date LastDate;
        public boolean CountUnreadComments;
        public boolean Read;
        public String FirstName;
        public String FirstEmail;
        public String FirstPhoto;
        public String LastName;
        public String LastEmail;
        public String LastPhoto;
        public String InsertName;
        public String InsertEmail;
        public String InsertPhoto;

        public Discussion(JsonReader reader) {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "DiscussionID":
                            if (reader.peek() == JsonToken.NULL) {
                                DiscussionID = 0;
                                reader.nextNull();
                            }
                            else
                                DiscussionID = reader.nextInt();
                            break;
                        case "Type":
                            if (reader.peek() == JsonToken.NULL) {
                                Type = "";
                                reader.nextNull();
                            }
                            else
                                Type = reader.nextString();
                            break;
                        case "ForeignID":
                            if (reader.peek() == JsonToken.NULL) {
                                ForeignID = 0;
                                reader.nextNull();
                            }
                            else
                                ForeignID = reader.nextInt();
                            break;
                        case "CategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                CategoryID = 0;
                                reader.nextNull();
                            }
                            else
                                CategoryID = reader.nextInt();
                            break;
                        case "InsertUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertUserID = 0;
                                reader.nextNull();
                            }
                            else
                                InsertUserID = reader.nextInt();
                            break;
                        case "UpdateUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateUserID = 0;
                                reader.nextNull();
                            }
                            else
                                UpdateUserID = reader.nextInt();
                            break;
                        case "FirstCommentID":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstCommentID = 0;
                                reader.nextNull();
                            }
                            else
                                FirstCommentID = reader.nextInt();
                            break;
                        case "LastCommentID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastCommentID = 0;
                                reader.nextNull();
                            }
                            else
                                LastCommentID = reader.nextInt();
                            break;
                        case "Name":
                            if (reader.peek() == JsonToken.NULL) {
                                Name = "";
                                reader.nextNull();
                            }
                            else
                                Name = reader.nextString();
                            break;
                        case "Body":
                            if (reader.peek() == JsonToken.NULL) {
                                Body = "";
                                reader.nextNull();
                            }
                            else
                                Body = reader.nextString();
                            break;
                        case "Format":
                            if (reader.peek() == JsonToken.NULL) {
                                Format = "";
                                reader.nextNull();
                            }
                            else
                                Format = reader.nextString();
                            break;
                        case "Tags":
                            if (reader.peek() == JsonToken.NULL) {
                                Tags = "";
                                reader.nextNull();
                            }
                            else
                                Tags = reader.nextString();
                            break;
                        case "CountComments":
                            if (reader.peek() == JsonToken.NULL) {
                                CountComments = 0;
                                reader.nextNull();
                            }
                            else
                                CountComments = reader.nextInt();
                            break;
                        case "CountBookmarks":
                            if (reader.peek() == JsonToken.NULL) {
                                CountBookmarks = 0;
                                reader.nextNull();
                            }
                            else
                                CountBookmarks = reader.nextInt();
                            break;
                        case "CountViews":
                            if (reader.peek() == JsonToken.NULL) {
                                CountViews = 0;
                                reader.nextNull();
                            }
                            else
                                CountViews = reader.nextInt();
                            break;
                        case "Closed":
                            if (reader.peek() == JsonToken.NULL) {
                                Closed = false;
                                reader.nextNull();
                            }
                            else
                                Closed = ItoB(reader.nextInt());
                            break;
                        case "Announce":
                            if (reader.peek() == JsonToken.NULL) {
                                Announce = false;
                                reader.nextNull();
                            }
                            else
                                Announce = ItoB(reader.nextInt());
                            break;
                        case "Sink":
                            if (reader.peek() == JsonToken.NULL) {
                                Sink = false;
                                reader.nextNull();
                            }
                            else
                                Sink = ItoB(reader.nextInt());
                            break;
                        case "DateInserted":
                            if (reader.peek() == JsonToken.NULL) {
                                DateInserted = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateInserted = df.parse(reader.nextString());
                            break;
                        case "DateUpdated":
                            if (reader.peek() == JsonToken.NULL) {
                                DateUpdated = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateUpdated = df.parse(reader.nextString());
                            break;
                        case "InsertIPAddress":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertIPAddress = "";
                                reader.nextNull();
                            }
                            else
                                InsertIPAddress = reader.nextString();
                            break;
                        case "UpdateIPAddress":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateIPAddress = "";
                                reader.nextNull();
                            }
                            else
                                UpdateIPAddress = reader.nextString();
                            break;
                        case "DateLastComment":
                            if (reader.peek() == JsonToken.NULL) {
                                DateLastComment = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateLastComment = df.parse(reader.nextString());
                            break;
                        case "LastCommentUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastCommentUserID = 0;
                                reader.nextNull();
                            }
                            else
                                LastCommentUserID = reader.nextInt();
                            break;
                        case "Score":
                            if (reader.peek() == JsonToken.NULL) {
                                Score = 0;
                                reader.nextNull();
                            }
                            else
                                Score = reader.nextInt();
                            break;
                        case "RegardingID":
                            if (reader.peek() == JsonToken.NULL) {
                                RegardingID = 0;
                                reader.nextNull();
                            }
                            else
                                RegardingID = reader.nextInt();
                            break;
                        case "WatchUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                WatchUserID = 0;
                                reader.nextNull();
                            }
                            else
                                WatchUserID = reader.nextInt();
                            break;
                        case "DateLastViewed":
                            if (reader.peek() == JsonToken.NULL) {
                                DateLastViewed = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateLastViewed = df.parse(reader.nextString());
                            break;
                        case "Dismissed":
                            if (reader.peek() == JsonToken.NULL) {
                                Dismissed = false;
                                reader.nextNull();
                            }
                            else
                                Dismissed = ItoB(reader.nextInt());
                            break;
                        case "Bookmarked":
                            if (reader.peek() == JsonToken.NULL) {
                                Bookmarked = false;
                                reader.nextNull();
                            }
                            else
                                Bookmarked = ItoB(reader.nextInt());
                            break;
                        case "CountCommentWatch":
                            if (reader.peek() == JsonToken.NULL) {
                                CountCommentWatch = 0;
                                reader.nextNull();
                            }
                            else
                                CountCommentWatch = reader.nextInt();
                            break;
                        case "Participated":
                            if (reader.peek() == JsonToken.NULL) {
                                Participated = false;
                                reader.nextNull();
                            }
                            else
                                Participated = ItoB(reader.nextInt());
                            break;
                        case "Url":
                            if (reader.peek() == JsonToken.NULL) {
                                Url = "";
                                reader.nextNull();
                            }
                            else
                                Url = reader.nextString();
                            break;
                        case "Category":
                            if (reader.peek() == JsonToken.NULL) {
                                Category = "";
                                reader.nextNull();
                            }
                            else
                                Category = reader.nextString();
                            break;
                        case "CategoryUrlCode":
                            if (reader.peek() == JsonToken.NULL) {
                                CategoryUrlCode = "";
                                reader.nextNull();
                            }
                            else
                                CategoryUrlCode = reader.nextString();
                            break;
                        case "PermissionCategoryID":
                            if (reader.peek() == JsonToken.NULL) {
                                PermissionCategoryID = 0;
                                reader.nextNull();
                            }
                            else
                                PermissionCategoryID = reader.nextInt();
                            break;
                        case "FirstUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstUserID = 0;
                                reader.nextNull();
                            }
                            else
                                FirstUserID = reader.nextInt();
                            break;
                        case "FirstDate":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstDate = defaultDate;
                                reader.nextNull();
                            }
                            else
                                FirstDate = df.parse(reader.nextString());
                            break;
                        case "LastUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                LastUserID = 0;
                                reader.nextNull();
                            }
                            else
                                LastUserID = reader.nextInt();
                            break;
                        case "LastDate":
                            if (reader.peek() == JsonToken.NULL) {
                                LastDate = defaultDate;
                                reader.nextNull();
                            }
                            else
                                LastDate = df.parse(reader.nextString());
                            break;
                        case "CountUnreadComments":
                            if (reader.peek() == JsonToken.NULL) {
                                CountUnreadComments = false;
                                reader.nextNull();
                            }
                            // Sometimes, the API returns false, sometimes 0 ...
                            else if (reader.peek() == JsonToken.NUMBER)
                                CountUnreadComments = ItoB(reader.nextInt());
                            else
                                CountUnreadComments = reader.nextBoolean();
                            break;
                        case "Read":
                            if (reader.peek() == JsonToken.NULL) {
                                Read = false;
                                reader.nextNull();
                            }
                            else
                                Read = reader.nextBoolean();
                            break;
                        case "FirstName":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstName = "";
                                reader.nextNull();
                            }
                            else
                                FirstName = reader.nextString();
                            break;
                        case "FirstEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstEmail = "";
                                reader.nextNull();
                            }
                            else
                                FirstEmail = reader.nextString();
                            break;
                        case "FirstPhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                FirstPhoto = "";
                                reader.nextNull();
                            }
                            else
                                FirstPhoto = reader.nextString();
                            break;
                        case "LastName":
                            if (reader.peek() == JsonToken.NULL) {
                                LastName = "";
                                reader.nextNull();
                            }
                            else
                                LastName = reader.nextString();
                            break;
                        case "LastEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                LastEmail = "";
                                reader.nextNull();
                            }
                            else
                                LastEmail = reader.nextString();
                            break;
                        case "LastPhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                LastPhoto = "";
                                reader.nextNull();
                            }
                            else
                                LastPhoto = reader.nextString();
                            break;
                        case "InsertName":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertName = "";
                                reader.nextNull();
                            }
                            else
                                InsertName = reader.nextString();
                            break;
                        case "InsertEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertEmail = "";
                                reader.nextNull();
                            }
                            else
                                InsertEmail = reader.nextString();
                            break;
                        case "InsertPhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertPhoto = "";
                                reader.nextNull();
                            }
                            else
                                InsertPhoto = reader.nextString();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DiscussionFull {
        public Discussion Discussion;
        public int CategoryID;
        public Category Category;
        public List<Comment> Comments = new ArrayList<>();
        public int Page;

        public DiscussionFull(JsonReader reader) {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "Discussion":
                            Discussion = new Discussion(reader);
                            break;
                        case "CategoryID":
                            CategoryID = reader.nextInt();
                            break;
                        case "Category":
                            Category = new Category(reader);
                            break;
                        case "Comments":
                            reader.beginArray();
                            while (reader.hasNext())
                                Comments.add(new Comment(reader));
                            reader.endArray();
                            break;
                        case "Page":
                            Page = reader.nextInt();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Comment {
        public int CommentID;
        public int DiscussionID;
        public int InsertUserID;
        public int UpdateUserID;
        public int DeleteUserID;
        public String Body;
        public String Format;
        public Date DateInserted;
        public Date DateDeleted;
        public Date DateUpdated;
        public String InsertIPAddress;
        public String UpdateIPAddress;
        public boolean Flag;
        public int Score;
        public String InsertName;
        public String InsertEmail;
        public String InsertPhoto;
        public String UpdateName;
        public String UpdateEmail;
        public String UpdatePhoto;

        public Comment(JsonReader reader) {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "CommentID":
                            if (reader.peek() == JsonToken.NULL) {
                                CommentID = 0;
                                reader.nextNull();
                            }
                            else
                                CommentID = reader.nextInt();
                            break;
                        case "DiscussionID":
                            if (reader.peek() == JsonToken.NULL) {
                                DiscussionID = 0;
                                reader.nextNull();
                            }
                            else
                                DiscussionID = reader.nextInt();
                            break;
                        case "InsertUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertUserID = 0;
                                reader.nextNull();
                            }
                            else
                                InsertUserID = reader.nextInt();
                            break;
                        case "UpdateUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateUserID = 0;
                                reader.nextNull();
                            }
                            else
                                UpdateUserID = reader.nextInt();
                            break;
                        case "DeleteUserID":
                            if (reader.peek() == JsonToken.NULL) {
                                DeleteUserID = 0;
                                reader.nextNull();
                            }
                            else
                                DeleteUserID = reader.nextInt();
                            break;
                        case "Body":
                            if (reader.peek() == JsonToken.NULL) {
                                Body = "";
                                reader.nextNull();
                            }
                            else
                                Body = reader.nextString();
                            break;
                        case "Format":
                            if (reader.peek() == JsonToken.NULL) {
                                Format = "";
                                reader.nextNull();
                            }
                            else
                                Format = reader.nextString();
                            break;
                        case "DateInserted":
                            if (reader.peek() == JsonToken.NULL) {
                                DateInserted = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateInserted = df.parse(reader.nextString());
                            break;
                        case "DateDeleted":
                            if (reader.peek() == JsonToken.NULL) {
                                DateDeleted = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateDeleted = df.parse(reader.nextString());
                            break;
                        case "DateUpdated":
                            if (reader.peek() == JsonToken.NULL) {
                                DateUpdated = defaultDate;
                                reader.nextNull();
                            }
                            else
                                DateUpdated = df.parse(reader.nextString());
                            break;
                        case "InsertIPAddress":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertIPAddress = "";
                                reader.nextNull();
                            }
                            else
                                InsertIPAddress = reader.nextString();
                            break;
                        case "UpdateIPAddress":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateIPAddress = "";
                                reader.nextNull();
                            }
                            else
                                UpdateIPAddress = reader.nextString();
                            break;
                        case "Flag":
                            if (reader.peek() == JsonToken.NULL) {
                                Flag = false;
                                reader.nextNull();
                            }
                            else
                                Flag = ItoB(reader.nextInt());
                            break;
                        case "Score":
                            if (reader.peek() == JsonToken.NULL) {
                                Score = 0;
                                reader.nextNull();
                            }
                            else
                                Score = reader.nextInt();
                            break;
                        case "InsertName":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertName = "";
                                reader.nextNull();
                            }
                            else
                                InsertName = reader.nextString();
                            break;
                        case "InsertEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertEmail = "";
                                reader.nextNull();
                            }
                            else
                                InsertEmail = reader.nextString();
                            break;
                        case "InsertPhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                InsertPhoto = "";
                                reader.nextNull();
                            }
                            else
                                InsertPhoto = reader.nextString();
                            break;
                        case "UpdateName":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateName = "";
                                reader.nextNull();
                            }
                            else
                                UpdateName = reader.nextString();
                            break;
                        case "UpdateEmail":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdateEmail = "";
                                reader.nextNull();
                            }
                            else
                                UpdateEmail = reader.nextString();
                            break;
                        case "UpdatePhoto":
                            if (reader.peek() == JsonToken.NULL) {
                                UpdatePhoto = "";
                                reader.nextNull();
                            }
                            else
                                UpdatePhoto = reader.nextString();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean ItoB(int i) {
        return i == 1;
    }

    private List readListInt(JsonReader reader) {
        List<Integer> tab = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                tab.add(reader.nextInt());
            }
            reader.endArray();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }
}
