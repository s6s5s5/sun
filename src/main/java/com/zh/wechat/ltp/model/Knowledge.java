package com.zh.wechat.ltp.model;

import com.zh.wechat.ltp.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Getter
@Setter
public class Knowledge extends BaseEntity {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "QUESTION")
    private String question;

    /**
     * 分类
     * @author zhangLin
     * @date 2019/3/31
     * @param null
     * @return
     */
    @Column(name = "CATEGORY")
    private Integer category;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "QUESTION_TO_CALCULATE")
    private String questionToCalculate;

    /**
     * 文件号
     */
    @Column(name = "FILE_VERSIONS")
    private String fileVersions;
    /**
     * 文件名称
     */
    @Column(name = "FILE_BASIS")
    private String fileBasis;
    /**
     * 代码
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 整理人
     */
    @Column(name = "COLLECTORS")
    private String collectors;
    /**
     * 来源
     */
    @Column(name = "SOURCE")
    private String source;
    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;
    /**
     * 审核人
     */
    @Column(name = "AUDITOR")
    private String auditor;
    /**
     * 插入日期KnowledgeService
     */
    @Column(name = "INSERT_DATE")
    private Date insertDate;
    /**
     * 审核日期
     */
    @Column(name = "AUDIT_DATE")
    private Date auditDate;
    /**
     * 删除时间
     */
    @Column(name = "DEL_DATE")
    private Date delDate;
    /**
     * 删除标识
     */
    @Column(name = "DEL_FLAG")
    private Short delFlag;
    /**
     * 知识图片
     */
    @Column(name = "PIC_LIST")
    private String picList;
    /**
     * 知识库新增来源  00 新增 10 审核进入
     */
    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    /**
     * 用于匹配度排序非数据库字段
     * @author zhangLin
     * @date 2019/6/2
     * @param null
     * @return
     */
    @Transient
    private Integer rank;
}
