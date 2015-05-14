package wreckingball.bookself;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by Cristian on 4/22/2015.
 */
public class httpTasks {


   public static String sendParametersToAPI(String base, String extra)
   {
       HttpPost httpPost =  new HttpPost(base);
       httpPost.setHeader("content-type","text/json");


       String res = "";
       try {
           URL uri = new URL(base);
           HttpURLConnection con = (HttpURLConnection)uri.openConnection();
           con.setRequestMethod("POST");
           con.setRequestProperty("Content-length", String.valueOf(extra.length()));
           con.setRequestProperty("Content-Type", "text/json");
           con.setDoOutput(true);
           con.setDoInput(true);

           DataOutputStream dos = new DataOutputStream(con.getOutputStream());

           dos.writeBytes(extra);
           dos.close();

           DataInputStream dis = new DataInputStream(con.getInputStream());
           for (int i = dis.read(); i != -1; i = dis.read())
           {
               res += (char)i;
           }
           return res;


       }catch(Exception e)
       {
           return "NO_NETWORK_CONNECTION";
           //e.printStackTrace();
       }


       //return "";
   }
   /*
    public static String sendParametersToAPI(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try
        {
            httpPost.setEntity(new StringEntity(parm));
            HttpResponse httpResp = httpclient.execute(httpPost);
            HttpEntity httpEntity = httpResp.getEntity();

            return (EntityUtils.toString(httpEntity));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }
    */
    /*
    public static String sendParametersToAPI(String base, String extra)
    {
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder stringBuilder = new StringBuilder();


            //address.replace("{","%7B").replace("}","%7D");
            extra = URLEncoder.encode(extra);
            HttpGet httpGet = new HttpGet(base+extra);

        try
        {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();

            if(statusLine.getStatusCode() == 200)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream content = httpEntity.getContent();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                while((line = reader.readLine()) != null)
                    stringBuilder.append(line);
            }else
                Log.e("JSON GETTING ERROR", "Failed JSON object");

        }catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString().replace("!",".").replace("$",":");
    }
    */
}
