package com.zh.wechat.ltp.common;

import com.zh.wechat.ltp.model.Knowledge;
import com.zh.wechat.ltp.model.KnowledgeSimilar;
import com.zh.wechat.ltp.service.KnowledgeService;
import com.zh.wechat.ltp.service.Sentence2vector;
import com.zh.wechat.ltp.service.SimKeyCompare;
import com.zh.wechat.ltp.service.Vector;
import edu.hit.ir.ltp4j.Postagger;
import edu.hit.ir.ltp4j.Segmentor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.*;

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

    public static ScheduledExecutorService pool = Executors.newScheduledThreadPool(8);

    /**
     * 开启线程数量
     */
    public static int pageThreadNum = 6;

    /**
     * 线程分页
     */
    public static int pageThreadSize = 13000/pageThreadNum;

    /**
     * 线程超时时间
     */
    public static int threadTimeOut = 3100;

    /**
     * 提前存储知识库问题分词向量数组
     */
    public static Map<Integer,Vector[]> knowledgeVectorMap;

    @Resource
    SimKeyCompare simKeyCompare;

    @PostConstruct
    public void init() throws Exception{
        segmentor.create(modelPath,keywordPath);
        postagger.create(posModelPath);
        /*List<String> termList = new ArrayList<String>();
        int size = mySegment("股权平价转让需要交个税吗",termList);
        String abc = StringUtils.join(termList,",");
        List<String> termList1 = new ArrayList<String>();
        int size1 = mySegment("个人之间平价或低价转让股权如何计算缴纳个人所得税？需要进行核定么？",termList1);
        String abc1 = StringUtils.join(termList1,",");
        List<String> termList2 = new ArrayList<String>();
        int size2 = mySegment("亲属之间无偿转让股权，需要交个人所得税吗？",termList2);
        String abc2 = StringUtils.join(termList2,",");*/
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
    public  static int mySegment(String var1, List<String> var2){
        List<String> termList = new ArrayList<String>();
        int size = segmentor.segment(var1,termList);
        /**
         * 形容词 a 副词 d 名词 n 动词 v
         * b区别词 f方位词 m数词
         */
        List<String> posTermList = new ArrayList<>();
        int posSize = postagger.postag(termList,posTermList);
        for(int i = 0;i<posTermList.size();i++){
            String currentpos = posTermList.get(i);
            if(currentpos.startsWith("a")||currentpos.startsWith("d")||currentpos.startsWith("n")
                    ||currentpos.startsWith("v") || currentpos.startsWith("b") || currentpos.startsWith("f") || currentpos.startsWith("m") || currentpos.startsWith("j")){
                var2.add(termList.get(i));
            }
        }
        return var2.size();
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
            pageThreadSize = knowledgeList.size()/pageThreadNum;
            int row=0;
            //String wordsPos ="bfmadnv";
            knowledgeVectorMap = new HashMap<>();
            for (Knowledge knowledge : knowledgeList) {
                try {
                    if(!knowledge.getQuestionToCalculate().equals("null")&&!knowledge.equals("null")){
                        /**
                         * 存储分词后的结果
                         */
                        List<String> termList = new ArrayList<String>();
                       /* int size = segmentor.segment(knowledge.getQuestionToCalculate(),termList);
                        *//**
                         * 形容词 a 副词 d 名词 n 动词 v
                         * B区别词 f方位词 m数词
                         *//*
                        List<String> posTermList = new ArrayList<>();
                        int posSize = postagger.postag(termList,posTermList);

                        List<String> newList = new ArrayList<>();
                        for(int i = 0;i<posTermList.size();i++){
                            if(wordsPos.contains(posTermList.get(i))){
                                newList.add(termList.get(i));
                            }
                        }*/
                        //System.out.println(termList.toString());
                        //System.out.println(newList.toString());
                        //separate_question[row] = termList.toString();
                        mySegment(knowledge.getQuestionToCalculate(),termList);
                        //获得分词结果
                        String[] termListresult = termList.toString().replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
                        Vector[] veclist = new Vector[termListresult.length];
                        for(int i = 0; i< termListresult.length; i++){
                            Vector vec = simKeyCompare.Words2Vec(termListresult[i]);
                            veclist[i] = vec;
                        }
                        knowledgeVectorMap.put(knowledge.getId(),veclist);
                        separate_question[row] = termList.toString();
                        knowledge_id[row] = knowledge.getId();

                        /**
                         * 将问题对应的答案也提请进行分词后静态存储
                         */
                        List<String> termList_answer = new ArrayList<String>();
                        //int size_answer = segmentor.segment(knowledge.getAnswer(),termList_answer);
                        int size_answer = mySegment(knowledge.getAnswer(),termList_answer);
                        separate_answer[row] = termList_answer.toString();

                        row++;
                    }
                    knowledge_num = row;
                }
                catch (Exception e){
                    e.printStackTrace();
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
        //segmentor.segment(question,termList);
        mySegment(question,termList);

        Map<String,Float> map = new HashMap<>();

        for (int i = 0; i < vec_seg.length; i++){
            if(vec_seg[i] != null) {
                result[0][index_of_result] = Float.parseFloat(String.valueOf(vec_seg_id[i]));

                float ii = sentence2vector.calSimilarity(termList.toString(),vec_seg[i]);
                BigDecimal b1 = BigDecimal.valueOf(ii);

                float jj = sentence2vector.calSimilarity(vec_seg[i],termList.toString());
                BigDecimal b2 = BigDecimal.valueOf(jj);

                float avg = b1.add(b2).floatValue()/2.0f;
                result[1][index_of_result] = avg;
                index_of_result++;

                map.put(String.valueOf(vec_seg_id[i]),avg);
            }
        }
        return map;
    }

    public List<KnowledgeSimilar> GetResultSimilar(Map<Integer,Vector[]> knowledgeVectorMap,String[] separate_question, int[] vec_seg_id, String question){
        List<String> termList = new ArrayList<>();
        //segmentor.segment(question,termList);
        mySegment(question,termList);
        //获得分词结果
        String[] termListresult = termList.toString().replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
        Vector[] quesionveclist = new Vector[termListresult.length];
        for(int i = 0; i< termListresult.length; i++){
            Vector vec = simKeyCompare.Words2Vec(termListresult[i]);
            quesionveclist[i] = vec;
        }

        List<KnowledgeSimilar> knowledgeSimilarList = new ArrayList<>();
        //开启第一个任务
        Future<List> resultitem1 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(0,1*pageThreadSize, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );

        //开启第二个任务
        Future<List> resultitem2 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(1*pageThreadSize,2*pageThreadSize, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );

        //开启第三个任务
        Future<List> resultitem3 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(2*pageThreadSize,3*pageThreadSize, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );

        //开启第四个任务
        Future<List> resultitem4 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(3*pageThreadSize,4*pageThreadSize, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );

        //开启第五个任务
        Future<List> resultitem5 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(4*pageThreadSize,5*pageThreadSize, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );

        //开启第六个任务
        Future<List> resultitem6 = pool.schedule(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return getknowledgeAnswer(5*pageThreadSize,data_num, knowledgeVectorMap, separate_question, vec_seg_id, quesionveclist);
            }
        }, 0 , TimeUnit.SECONDS );
        try {
            long delay1 = System.currentTimeMillis();
            List list1 = resultitem1.get(threadTimeOut,TimeUnit.MILLISECONDS);
            long delay2 = System.currentTimeMillis();
            System.out.println("第一个线程执行完成还剩"+String.valueOf((threadTimeOut-delay2+delay1)>0?(threadTimeOut-delay2+delay1):0));
            List list2 = resultitem2.get((threadTimeOut-delay2+delay1)>0?(threadTimeOut-delay2+delay1):0,TimeUnit.MILLISECONDS);
            long delay3 = System.currentTimeMillis();
            System.out.println("第二个线程执行完成还剩"+String.valueOf((threadTimeOut-delay3+delay1)>0?(threadTimeOut-delay3+delay1):0));
            List list3 = resultitem3.get((threadTimeOut-delay3+delay1)>0?(threadTimeOut-delay3+delay1):0,TimeUnit.MILLISECONDS);
            long delay4 = System.currentTimeMillis();
            System.out.println("第三个线程执行完成还剩"+String.valueOf((threadTimeOut-delay4+delay1)>0?(threadTimeOut-delay4+delay1):0));
            List list4 = resultitem4.get((threadTimeOut-delay4+delay1)>0?(threadTimeOut-delay4+delay1):0,TimeUnit.MILLISECONDS);
            long delay5 = System.currentTimeMillis();
            System.out.println("第四个线程执行完成还剩"+String.valueOf((threadTimeOut-delay5+delay1)>0?(threadTimeOut-delay5+delay1):0));
            List list5 = resultitem5.get((threadTimeOut-delay5+delay1)>0?(threadTimeOut-delay5+delay1):0,TimeUnit.MILLISECONDS);
            long delay6 = System.currentTimeMillis();
            System.out.println("第五个线程执行完成还剩"+String.valueOf((threadTimeOut-delay6+delay1)>0?(threadTimeOut-delay6+delay1):0));
            List list6 = resultitem6.get((threadTimeOut-delay6+delay1)>0?(threadTimeOut-delay6+delay1):0,TimeUnit.MILLISECONDS);
            knowledgeSimilarList.addAll(list1);
            knowledgeSimilarList.addAll(list2);
            knowledgeSimilarList.addAll(list3);
            knowledgeSimilarList.addAll(list4);
            knowledgeSimilarList.addAll(list5);
            knowledgeSimilarList.addAll(list6);
        }catch (Exception e){
            e.printStackTrace();
            log.info("线程任务执行失败");
        }
        return knowledgeSimilarList;
    }

    public List<KnowledgeSimilar> getknowledgeAnswer(int bedin, int end, Map<Integer,Vector[]> knowledgeVectorMap, String[] separate_question, int[] vec_seg_id, Vector[] quesionveclist){
        List<KnowledgeSimilar> knowledgeSimilarList = new ArrayList<>();
        for (int i = bedin; i < end; i++){
            if(separate_question[i] != null) {
                KnowledgeSimilar knowledgeSimilar = new KnowledgeSimilar();

                float ii = sentence2vector.calSimilarity1(quesionveclist,knowledgeVectorMap.get(vec_seg_id[i]));
                BigDecimal b1 = BigDecimal.valueOf(ii);

                float jj = sentence2vector.calSimilarity1(knowledgeVectorMap.get(vec_seg_id[i]),quesionveclist);
                BigDecimal b2 = BigDecimal.valueOf(jj);

                float avg = b1.add(b2).floatValue()/2.0f;
                knowledgeSimilar.setKnowledgeId(String.valueOf(vec_seg_id[i]));
                knowledgeSimilar.setSimilar(avg);
                knowledgeSimilar.setQuestionWords(separate_question[i]);

                knowledgeSimilarList.add(knowledgeSimilar);
            }
        }
        return knowledgeSimilarList;
    }
}
