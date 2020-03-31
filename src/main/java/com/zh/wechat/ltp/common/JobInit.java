package com.zh.wechat.ltp.common;

import com.zh.wechat.ltp.model.Knowledge;
import com.zh.wechat.ltp.model.KnowledgeSimilar;
import com.zh.wechat.ltp.service.KnowledgeService;
import com.zh.wechat.ltp.service.Sentence2vector;
import edu.hit.ir.ltp4j.Postagger;
import edu.hit.ir.ltp4j.Segmentor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiJun
 * @version 1.0
 * @date 2019/9/25 11:20
 */
@Service
@Log4j2
public class JobInit {

    @Value("${MyYml.SimilarityPath}")
    private String SimilarityPath;

    @Value("${MyYml.ModelPath}")
    private String modelPath;

    @Value("${MyYml.KeywordPath}")
    private String keywordPath;

    @Value("${MyYml.SimWordPath}")
    private String simwordPath;

    @Value("${MyYml.PosModelPath}")
    private String posModelPath;

    @Resource
    KnowledgeService knowledgeService;
    @Resource
    Sentence2vector sentence2vector;

    /**
     * >=数据库中问答库数据条数
     */
    public static int data_num = 30000;
    public static int knowledge_num = 0;
    /**
     * 静态区域存储的问题 2-电子税务局 3-ITS 4-常识类
     */
    public static String[] separate_question = new String[data_num];
    public static int[] knowledge_id = new int[data_num];

    /**
     * 静态区域存储问题的答案
     * 为了后续知识库关键词查询功能，提前将答案进行分词保存
     */
    public static String[] separate_answer = new String[data_num];

    /**
     * 静态区域存储同义词替换
     */
    public static List<String> sim_word_shoudbe = new ArrayList<String>();
    public static List<String> sim_word = new ArrayList<String>();

    /**
     * 将词语翻译模型保存到HashMap
     */
    public static HashMap<String,String> resultMap = new HashMap<>();

    public static Segmentor segmentor = new Segmentor();
    public static Postagger postagger = new Postagger();

    @PostConstruct
    public void init() throws Exception{
        segmentor.create(modelPath,keywordPath);
        postagger.create(posModelPath);
        log.info("=======开始加载词语翻译模型 begin=======");
        ReadAndWriteFile();
        log.info("=======加载词语翻译模型结束 end=======");
        log.info("=======初始化近义词到静态区 begin=======");
        init_sim_word();
        log.info("=======初始化近义词到静态区 end=======");
        log.info("=======初始化静态存储分词结果 begin=======");
        SegmentorKnowledge();
        log.info("=======初始化静态存储分词结果 end=======");
    }

    /**
     * 静态存储分词结果
     */
    public void SegmentorKnowledge(){
        log.info(System.getProperty("java.library.path"));
        log.info("启动分词时加载动态库文件：ltp4j-0.1.0-SNAPSHOT.so");
        log.info("初始化静态存储分词结果，加载知识库中并分词保存中...");
        try {
            Knowledge entity = new Knowledge();
            entity.setDelFlag(Short.valueOf("0"));
            List<Knowledge> knowledgeList = knowledgeService.selectList(entity);
            int row=0;
            String wordsPos ="BfMadnv";
            for (Knowledge knowledge : knowledgeList) {
                try {
                    if(!knowledge.getQuestionToCalculate().equals("null")&&!knowledge.equals("null")){
                        /**
                         * 存储分词后的结果
                         */
                        List<String> termList = new ArrayList<String>();
                        int size = segmentor.segment(knowledge.getQuestionToCalculate(),termList);
                        /**
                         * 形容词 a 副词 d 名词 n 动词 v
                         * B区别词 f方位词 m数词
                         */
                        List<String> posTermList = new ArrayList<>();
                        int posSize = postagger.postag(termList,posTermList);

                        List<String> newList = new ArrayList<>();
                        for(int i = 0;i<posTermList.size();i++){
                            if(wordsPos.contains(posTermList.get(i))){
                                newList.add(termList.get(i));
                            }
                        }
                        //System.out.println(termList.toString());
                        //System.out.println(newList.toString());
                        //separate_question[row] = termList.toString();
                        separate_question[row] = newList.toString();
                        knowledge_id[row] = knowledge.getId();

                        /**
                         * 将问题对应的答案也提请进行分词后静态存储
                         */
                        List<String> termList_answer = new ArrayList<String>();
                        int size_answer = segmentor.segment(knowledge.getAnswer(),termList_answer);
                        separate_answer[row] = termList_answer.toString();

                        row++;
                    }
                    knowledge_num = row;
                }
                catch (Exception e){
                    //打印异常
                    log.error("error in here:{},question{}", knowledge.getId(),knowledge.getQuestion());
                }
            }
            log.info("知识库分词结束，分词条数:{}",row);
        } catch (Exception e) {
            //打印异常
            log.error("JobInit.createIndex_w.error{}", e);
        }
    }

    /**
     * 初始化近义词到静态区
     */
    public void init_sim_word(){
        try {
            File sim_word_file = new File(simwordPath);
            BufferedReader sim_word_br = new BufferedReader(new FileReader(sim_word_file));
            String line = "";
            while ((line = sim_word_br.readLine()) != null) {
                String[] sp = line.split(",");
                sim_word.add(sp[0]);
                sim_word_shoudbe.add(sp[1]);
            }
            sim_word_br.close();
            log.info("近义词个数："+sim_word.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件目录,文件格式,按行读取并写入Map
     */
    public void ReadAndWriteFile() throws Exception{
        String encoding = "UTF-8";
        log.info("词语模型路径："+SimilarityPath);
        File file  = new File(SimilarityPath);
        if(file.isFile() && file.exists()){
            InputStreamReader readTxt = new InputStreamReader(new FileInputStream(file),encoding);
            BufferedReader bf = new BufferedReader(readTxt);
            String lineTxt = null;
            int rows = 0;
            while ((lineTxt = bf.readLine()) != null) {
                if(!"".equals(lineTxt) && lineTxt.split("\\|").length==2) {
                    String wordKey = lineTxt.split("\\|")[0];
                    String wordValue = lineTxt.split("\\|")[1];
                    resultMap.put(wordKey,wordValue);
                }
                rows++;
            }
            log.info("加载翻译模型行数："+rows);
        }
    }


    public Map<String,Float> GetResult(String[] vec_seg, int[] vec_seg_id, String question){
        int index_of_result = 0;
        float [][] result = new float[2][data_num];

        List<String> termList = new ArrayList<>();
        segmentor.segment(question,termList);

        Map<String,Float> map = new HashMap<>();

        for (int i = 0; i < vec_seg.length; i++){
            if(vec_seg[i] != null) {
                result[0][index_of_result] = Float.parseFloat(String.valueOf(vec_seg_id[i]));

                float ii = sentence2vector.calSimilarity(termList.toString(),vec_seg[i],resultMap);
                BigDecimal b1 = BigDecimal.valueOf(ii);

                float jj = sentence2vector.calSimilarity(vec_seg[i],termList.toString(),resultMap);
                BigDecimal b2 = BigDecimal.valueOf(jj);

                float avg = b1.add(b2).floatValue()/2.0f;
                result[1][index_of_result] = avg;
                index_of_result++;

                map.put(String.valueOf(vec_seg_id[i]),avg);
            }
        }
        return map;
    }

    public List<KnowledgeSimilar> GetResultSimilar(String[] vec_seg, int[] vec_seg_id, String question){
        int index_of_result = 0;
        float [][] result = new float[2][data_num];

        List<String> termList = new ArrayList<>();
        segmentor.segment(question,termList);

        List<KnowledgeSimilar> knowledgeSimilarList = new ArrayList<>();

        for (int i = 0; i < vec_seg.length; i++){
            if(vec_seg[i] != null) {
                KnowledgeSimilar knowledgeSimilar = new KnowledgeSimilar();
                result[0][index_of_result] = Float.parseFloat(String.valueOf(vec_seg_id[i]));

                float ii = sentence2vector.calSimilarity(termList.toString(),vec_seg[i],resultMap);
                BigDecimal b1 = BigDecimal.valueOf(ii);

                float jj = sentence2vector.calSimilarity(vec_seg[i],termList.toString(),resultMap);
                BigDecimal b2 = BigDecimal.valueOf(jj);

                float avg = b1.add(b2).floatValue()/2.0f;
                result[1][index_of_result] = avg;
                index_of_result++;
                knowledgeSimilar.setKnowledgeId(String.valueOf(vec_seg_id[i]));
                knowledgeSimilar.setSimilar(avg);
                knowledgeSimilar.setQuestionWords(vec_seg[i]);

                knowledgeSimilarList.add(knowledgeSimilar);
            }
        }
        return knowledgeSimilarList;
    }
}
