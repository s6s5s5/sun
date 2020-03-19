package com.zh.wechat.ltp.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SimKeyCompare {
    static  HashMap<String, Vector> map;

    /**
     * 项目启动执行一次加载model.txt文件
     */
    @PostConstruct
    private  void init(){
        map = LoadModel("static/model.txt");
        System.out.println("初始化了");
    }

    /**
     * 计算两个关键词向量的余弦值
     *
     * @param
     * @return
     */
    public float CosineCompare1(String keyword, String simword) {

        float wordSimilarity = 0.0f;
        Vector vec1;
        Vector vec2;
        vec1 = Words2Vec(keyword);
        vec2 = Words2Vec(simword);
        wordSimilarity=vec2.cosine(vec1);
        return wordSimilarity;
    }
    public float CosineCompare(String keyword, String simword,Map map) {
        return this.CosineCompare1(keyword,simword);
    }
    /**
     * 将关键词转换成字向量求和取平均后的结果
     *
     * @param
     * @return
     */
    public Vector Words2Vec(String words) {
        List<String> wordList = Stream.iterate(0, n -> ++n).limit(words.length())
                .map(n -> "" + words.charAt(n))
                .collect(Collectors.toList());
        Vector wordsVec = new Vector(200);
        int cnt = 0;
        for (String word : wordList) {
            if (map.containsKey(word)) {
                wordsVec = wordsVec.add(map.get(word));
                cnt += 1;
            }
        }
        return wordsVec.divideToSelf(cnt);
    }

    /**
     * 读取model.txt文件，将其转化为一个字：向量 的字典
     */
    public static HashMap<String, Vector> LoadModel(String filename) {
        HashMap<String, Vector> map = new HashMap<String, Vector>();
        try {
            String temp = null;
            //File f = new File(filename);
            Resource resource = new ClassPathResource(filename);
            InputStreamReader read = new InputStreamReader(resource.getInputStream(), "utf-8");
            BufferedReader reader = new BufferedReader(read);
            while ((temp = reader.readLine()) != null && !"".equals(temp)) {
                String[] shuzu = temp.split(" ");
                String key;
                float[] vc = new float[200];
                Vector vec = new Vector(200);
                key = shuzu[0];
                for (int i = 1; i < shuzu.length; i++) {
                    vc[i - 1] = Float.parseFloat(shuzu[i]);
                    vec = new Vector(vc);
                }
                map.put(key, vec);
            }
            read.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}


    //    public float CosineCompare(String keyword,String simword,HashMap<String,String> resultMap){
//        /**
//         * 根据关键词查找Map对应的value值，判断value值是否一致 一致视为相似返回0.93 否：0.3
//         */
//        String keyValue="";
//        String SimValue="";
//        try {
//            if(keyword.equals(simword)){
//                return 1f;
//            }else {
//                //HashMap<String, String> resultMap = ReadAndWriteFile();
//                keyValue = resultMap.get(keyword) == null ? null : resultMap.get(keyword);
//                SimValue = resultMap.get(simword) == null ? null : resultMap.get(simword);
//                if (keyValue == null || SimValue == null) {
//                    return 0.3f;
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return keyValue.equals(SimValue)?0.93f:0.3f;
//    }

