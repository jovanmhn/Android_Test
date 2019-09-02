package com.example.myapplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HTTPConnection {


public static final int HTTP_TIMEOUT = 120 * 1000; // milliseconds

private static HttpClient mHttpClient;



private static HttpClient getHttpClient() {

if (mHttpClient == null) {
    mHttpClient = new DefaultHttpClient();


    final HttpParams params = mHttpClient.getParams();
    HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
    HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
    ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
}

return mHttpClient;
}




public static byte[] readBytes(InputStream inputStream) throws IOException {
// this dynamically extends to take the bytes you read
ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

// this is storage overwritten on each iteration with bytes
int bufferSize = 1024;
byte[] buffer = new byte[bufferSize];

// we need to know how may bytes were read to write them to the byteBuffer
int len = 0;
while ((len = inputStream.read(buffer)) != -1) {
    byteBuffer.write(buffer, 0, len);
}

// and then we can return your byte array.
return byteBuffer.toByteArray();
}


public static String ExecuteHttpGet(String url) throws Exception {

BufferedReader in = null;

try {

    HttpClient client = getHttpClient();

    HttpGet request = new HttpGet();

    request.setURI(new URI(url));

    HttpResponse response = client.execute(request);

    in = new BufferedReader(new InputStreamReader(response.getEntity()

            .getContent()));


    StringBuffer sb = new StringBuffer("");

    String line = "";

    String NL = System.getProperty("line.separator");

    while ((line = in.readLine()) != null) {

        sb.append(line + NL);

    }

    in.close();


    String result = sb.toString();

    return result;

} finally {

    if (in != null) {

        try {

            in.close();

        } catch (IOException e) {

            Log.e("log_tag", "Greska prilikom konverzije " + e.toString());

            e.printStackTrace();

        }

    }

}

}

public static String ExecuteHttpGet(String url, String stavke) throws Exception {

BufferedReader in = null;

try {

    HttpClient client = getHttpClient();

    HttpPost request = new HttpPost(url);

    // Add your data
    List nameValuePairs = new ArrayList<NameValuePair>();
    nameValuePairs.add(new BasicNameValuePair("stavke", stavke));
    request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    HttpResponse response = client.execute(request);

    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


    StringBuffer sb = new StringBuffer("");

    String line = "";

    String NL = System.getProperty("line.separator");

    while ((line = in.readLine()) != null) {

        sb.append(line + NL);

    }

    in.close();


    String result = sb.toString();

    return result;

} finally {

    if (in != null) {

        try {

            in.close();

        } catch (IOException e) {

            Log.e("log_tag", "Greska prilikom konverzije " + e.toString());

            e.printStackTrace();

        }

    }

}

}


private List postData;
private HttpClient client;
private HttpPost request;
public HTTPConnection(String url) {
postData = new ArrayList<NameValuePair>();

client = getHttpClient();
request = new HttpPost(url);

//addData("u", ActivityMain.userString);
//addData("p", ActivityMain.passwordString);
}

public void addData(String name, String value) {
postData.add(new BasicNameValuePair(name, value));
}

public String execute() throws Exception {
BufferedReader in = null;

try {

    request.setEntity(new UrlEncodedFormEntity(postData));

    HttpResponse response = client.execute(request);

    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


    StringBuffer sb = new StringBuffer("");

    String line = "";

    String NL = System.getProperty("line.separator");

    while ((line = in.readLine()) != null) {

        sb.append(line + NL);

    }

    in.close();


    String result = sb.toString();

    return result;

} finally {

    if (in != null) {

        try {

            in.close();

        } catch (IOException e) {

            Log.e("log_tag", "Greska prilikom konverzije " + e.toString());

            e.printStackTrace();

        }

    }

}
}


}
