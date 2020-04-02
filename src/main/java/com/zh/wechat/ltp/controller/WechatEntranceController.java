package com.zh.wechat.ltp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.wechat.ltp.common.JobInit;
import com.zh.wechat.ltp.common.util.DateHelper;
import com.zh.wechat.ltp.model.Knowledge;
import com.zh.wechat.ltp.model.KnowledgeSimilar;
import com.zh.wechat.ltp.service.KnowledgeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.zh.wechat.ltp.common.JobInit.*;

/**
 * @author LiJun
 * @version 1.0
 * @date 2019/9/29 9:55
 */

@RestController
@RequestMapping("/wechat-ai")
@Log4j2
public class WechatEntranceController {

    @Value("${MyYml.ModelPath}")
    private String modelPath;
    @Value("${MyYml.KeywordPath}")
    private String keywordPath;
    @Resource
    JobInit jobInit;
    @Resource
    KnowledgeService knowledgeService;

    /**
     * 业务问答 - 根据问题 返回库中最相似的问题
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/businessChat",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String businessChat(@RequestBody JSONObject jsonParam){
        System.out.println("ai开始"+System.currentTimeMillis());
        log.info("请求参数：{}",jsonParam.toJSONString());
        String question = jsonParam.get("question").toString();
        /**
         * 问题标准化
         */
        question = normalizing_question(question);
        List<KnowledgeSimilar> resultSimilar = new ArrayList<>();
        resultSimilar = jobInit.GetResultSimilar(separate_question,knowledge_id,question);
        String jsonStr = JSONArray.toJSONString(resultSimilar);
        System.out.println("ai结束"+System.currentTimeMillis());
        return jsonStr;
    }

    /**
     * 知识库搜素
     * @param
     * @return
     */
    @GetMapping(value = "/getSegAnswer")
    public String getSegemtorAnswer(){
        List<KnowledgeSimilar> resultSimilar = resultAnswer(separate_answer,knowledge_id,separate_question);
        String jsonStr = JSONArray.toJSONString(resultSimilar);
        return jsonStr;
    }

    public List<KnowledgeSimilar> resultAnswer(String[] separate_answer, int[] knowledge_id,String[] separate_question){
        List<KnowledgeSimilar> knowledgeSimilarList = new ArrayList<>();
        for (int i = 0; i < separate_answer.length; i++){
            if(separate_answer[i] != null) {
                KnowledgeSimilar knowledgeSimilar = new KnowledgeSimilar();
                knowledgeSimilar.setKnowledgeId(String.valueOf(knowledge_id[i]));
                knowledgeSimilar.setAnswerWords(separate_answer[i]);
                knowledgeSimilar.setQuestionWords(separate_question[i]);
                knowledgeSimilarList.add(knowledgeSimilar);
            }
        }
        return knowledgeSimilarList;
    }


    public static String getMapToString(Map<String,Float> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if(i != keyArray.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }


    /**
     * 标准化、统一化问题
     * @param question
     * @return question_normalized
     */
    public String normalizing_question(String question){
        String question_normalized = question;

        //近义词标准化
        for(int i = 0;i<sim_word.size();i++) {
            int intIndex = question.indexOf(sim_word.get(i));
            int intIndexbe = question.indexOf(sim_word_shoudbe.get(i));
            if (intIndex == -1 || intIndexbe != -1) {
                continue;
            } else {
                question_normalized = question.substring(0, intIndex) + sim_word_shoudbe.get(i) + question.substring(sim_word.get(i).length() + intIndex, question.length());
                question = question_normalized;
            }
        }

        //时间标准化
        String today = DateHelper.getCurrentDate();
        String year = today.substring(0,4)+"年";
        String month;
        if(today.substring(5,6).equals("0")){
            month= today.substring(6,7)+"月";
        }
        else{
            month= today.substring(5,7)+"月";
        }

        //处理年份
        if(question.contains("今年")){
            question_normalized = question.replace("今年",year);
            question = question_normalized;
        }

        //处理月份
        if(question.contains("本月")&& !question.contains(year)){
            question_normalized = question.replace("本月",year+month);
            question = question_normalized;
        }
        else {
            if(question.contains("本月")&&question.contains(year)){
                question_normalized = question.replace("本月",month);
                question = question_normalized;
            }
        }

        String hanziyuefen[] = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        for (int indexyue=0;indexyue<hanziyuefen.length;indexyue++){
            if(question.contains(hanziyuefen[indexyue])&&question.contains("征期")){
                question_normalized = question.replace(hanziyuefen[indexyue],String.valueOf(indexyue+1)+"月");
                question = question_normalized;
            }
        }

        log.info("标准化问题:{}",question_normalized);
        return question;
    }

}
