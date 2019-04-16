package com.example.wjqcau.inventory;

/**
 * @author wjqcau
 * This class mainly holding the constant for the app
 */
public class Config {
    //The php script server url which will communicate with volley method
    public static final String FILE_UPLOAD_URL = "https://jwang.scweb.ca/PhotoServer/connection.php";
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android_File_Upload";
    //The basic url for warlmat api
    public static final String WalmartString="http://api.walmartlabs.com/v1/search?query=";
    //api key for walmart
    public static final String WalmartKey="&format=json&apiKey=283c225hgrac7m7jx6jzehpb";
}
