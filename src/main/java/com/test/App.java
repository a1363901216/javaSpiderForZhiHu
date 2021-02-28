package com.test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.test.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        String questionId = "313315123";
//        File file = new File("F:\\code\\spider1\\ret\\secret"+questionId+".txt");
//        if (file.exists()) {
//            file.delete();
//        }
        FileWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            int offset = 0;
            boolean isEnd = false;
            while(!isEnd && offset < 20000){
                String zhihuUrl = "https://www.zhihu.com/api/v4/questions/"+questionId+"/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cattachment%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Cis_labeled%2Cpaid_info%2Cpaid_info_content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_recognized%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics%3Bdata%5B%2A%5D.settings.table_of_content.enabled" +
                        "&limit=5" +
                        "&offset="+  offset + "&platform=desktop&sort_by=default";
                Map<String,String> header = new HashMap<>();
                header.put("cookie", "KLBRSID=031b5396d5ab406499e2ac6fe1bb1a43|1614494473|1614486044; Path=/");
                List<String> ans = new ArrayList<>(1024);
                String ret = HttpClientUtil.sendGet(zhihuUrl,new HashMap<String, String>(), header);
                JsonNode node = mapper.readTree(ret);
                if(node.get("data")!=null){
                    for(int i=0;i<node.get("data").size();i++){
                        JsonNode dataNode = node.get("data").get(i);
                        if(dataNode!=null){
                            JsonNode contentNode = dataNode.get("content");
                            if(contentNode!=null){
                                ans.add(contentNode.toString());
                            }
                        }
                    }
                }
                    for(String ansOne : ans){
                        logger.info(ansOne);
                    }
                if(node.get("paging")!=null){
                    if(node.get("paging").get("is_end")!=null){
                        isEnd = new Boolean(node.get("paging").get("is_end").toString()) ;
                    }
                }
                offset+=5;
                Thread.sleep(2000+ new Random().nextInt(1000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

