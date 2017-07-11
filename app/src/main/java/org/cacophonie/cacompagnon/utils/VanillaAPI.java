package org.cacophonie.cacompagnon.utils;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VanillaAPI {
    private String remoteURL;
    private String sessionCookie = "";

    private enum types {
        CATEGORIES
    }

    public interface Callback {
        void onFinished(Object result);
    }

    public VanillaAPI(String remoteURL_p) {
        remoteURL = remoteURL_p;
        if (!remoteURL.endsWith("/"))
            remoteURL += "/";
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

    private class AsyncRequest extends AsyncTask<Object, Void, Object> {
        private types mType;
        private Callback mCb;

        protected void onPostExecute(Object ret) {
            switch (mType) {
                case CATEGORIES:
                    mCb.onFinished(ret);
                    break;
            }
        }

        protected Object doInBackground(Object... params) {
            InputStream in;
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
                in = conn.getInputStream();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            switch (mType) {
                case CATEGORIES:
                    return parseCategories(new InputStreamReader(in));
            }
            return null;
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
        public String DateMarkedRead;
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
        public String DateInserted;
        public String DateUpdated;
        public int LastCommentID;
        public int LastDiscussionID;
        public String LastDateInserted;
        public String AllowedDiscussionTypes;
        public String DefaultDiscussionType;
        public boolean AllowFileUploads;
        public int CountAllDiscussions;
        public int CountAllComments;
        public String Url;
        public List ChildIDs;
        public String PhotoUrl;
        public boolean NoComment; // for Discussion but not Headings
        public String LastTitle;
        public int LastUserID;
        public int LastDiscussionUserID;
        public String LastUrl;
        public String DateLastComment;
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
                                DateMarkedRead = "";
                                reader.nextNull();
                            }
                            else
                                DateMarkedRead = reader.nextString();
                            break;
                        case "AllowDiscussions":
                            if (reader.peek() == JsonToken.NULL) {
                                AllowDiscussions = false;
                                reader.nextNull();
                            }
                            else
                                AllowDiscussions = StoB(reader.nextString());
                            break;
                        case "Archived":
                            if (reader.peek() == JsonToken.NULL) {
                                Archived = false;
                                reader.nextNull();
                            }
                            else
                                Archived = StoB(reader.nextString());
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
                                HideAllDiscussions = StoB(reader.nextString());
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
                                DateInserted = "";
                                reader.nextNull();
                            }
                            else
                                DateInserted = reader.nextString();
                            break;
                        case "DateUpdated":
                            if (reader.peek() == JsonToken.NULL) {
                                DateUpdated = "";
                                reader.nextNull();
                            }
                            else
                                DateUpdated = reader.nextString();
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
                                LastDateInserted = "";
                                reader.nextNull();
                            }
                            else
                                LastDateInserted = reader.nextString();
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
                                AllowFileUploads = StoB(reader.nextString());
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
                                ChildIDs = readList(reader);
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
                                DateLastComment = "";
                                reader.nextNull();
                            }
                            else
                                DateLastComment = reader.nextString();
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

        private List readList(JsonReader reader) {
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

        private boolean StoB(String s) {
            return s.equals("1");
        }
    }
}
