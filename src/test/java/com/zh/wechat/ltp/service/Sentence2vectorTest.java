package com.zh.wechat.ltp.service;

import edu.hit.ir.ltp4j.Segmentor;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author LiJun
 * @version 1.0
 * @date 2019/9/30 14:05
 */
public class Sentence2vectorTest {

    @Value("${MyYml.SimilarityPath}")
    private static String  SimilarityPath;

    public static void main(String[] args) throws Exception{
        HashMap<String,String> resultMap = new HashMap<>();
        String filePath = "E:\\ltp_data\\Similarity.txt";
        System.out.println(filePath);
        String encoding="UTF-8";
        File file  = new File(filePath);
        if(file.isFile() && file.exists()){
            InputStreamReader readTxt = new InputStreamReader(new FileInputStream(file),encoding);
            BufferedReader bf = new BufferedReader(readTxt);
            String lineTxt = null;
            while ((lineTxt = bf.readLine()) != null) {
                if(!"".equals(lineTxt) && lineTxt.split("\\|").length==2) {
                    String wordKey = lineTxt.split("\\|")[0];
                    String wordValue = lineTxt.split("\\|")[1];
                    resultMap.put(wordKey,wordValue);
                }
            }
        }
        /**
         * 分词接口
         */
        Segmentor segmentor=new Segmentor();
        if(segmentor.create("E:\\ltp_data\\cws.model","E:\\ltp_data\\keyword.txt")<0){
            System.err.println("load failed");
            return;
        }
        String sent_1 = "ITS操作申诉管理是什么？";
        List<String> words_1 = new ArrayList<String>();
        int size_1 = segmentor.segment(sent_1,words_1);

        String sent_2 = "什么是混合销售？";
        List<String> words_2 = new ArrayList<String>();
        int size_2 = segmentor.segment(sent_2,words_2);

        System.out.println(words_1.toString());
        System.out.println(words_2.toString());
        float a = new Sentence2vector().calSimilarity(words_1.toString(),words_2.toString(),resultMap);
        System.out.println(a);
        float b = new Sentence2vector().calSimilarity(words_2.toString(),words_1.toString(),resultMap);
        System.out.println(b);

        BigDecimal a1 = BigDecimal.valueOf(a);
        BigDecimal b1 = BigDecimal.valueOf(b);
        float avg = a1.add(b1).floatValue()/2.0f;
        System.out.println(avg);


        /*String sen1 = "[营改增, 后, ，, 现在, 银行, 手续费, 和, 利息费用, 应该, 取得, 发票, ，, 现在, 银行, 能, 及时, 给, 相应, 回单, 但, 发票, 开具, 比较, 麻烦, 而且, 滞后, ，, 请问, 我们, 大连, 有, 没, 有, 专门, 规定, 现在, 必须, 取得, 银行, 开具, 的, 发票, 还是, 像, 以前, 一样, 只, 凭, 银行, 回单, 就, 列支, 费用, ？]";
        String sen2 = "[发票, 的, 基本, 联次, 有, 哪些, ？]";
        System.out.println(sen1);
        System.out.println(sen2);
        float aa = new Sentence2vector().calSimilarity(sen1,sen2,resultMap);
        System.out.println(aa);
        float bb = new Sentence2vector().calSimilarity(sen2,sen1,resultMap);
        System.out.println(bb);

        BigDecimal aa1 = BigDecimal.valueOf(aa);
        BigDecimal bb1 = BigDecimal.valueOf(bb);
        float avg1 = aa1.add(bb1).floatValue()/2.0f;
        System.out.println(avg1);*/

        segmentor.release();
    }

}