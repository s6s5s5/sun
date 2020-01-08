package com.zh.wechat.ltp.common.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author zhangLin
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -8833729473062988840L;

    @Transient
    Integer currentPage;

    @Transient
    Integer size;

    @Transient
    String orderBy;
}
