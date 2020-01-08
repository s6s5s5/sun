package com.zh.wechat.ltp.common.util;


import java.util.Date;


/**
 * 实体类相关工具类
 * 解决问题： 1、快速对实体的常驻字段，如：crtUser、crtHost、updUser等值快速注入
 * @author zhangLin
 */
public class EntityUtils {
	private final static String DELFLAG = "delFlag";
	private final static String CREATEUSER = "createUser";
	private final static String CREATETIME = "createTime";
	private final static String MODIFYUSER = "modifyUser";
	private final static String MODIFYTIME = "modifyTime";
	/**
	 * 快速将bean的crtUser、crtHost、crtTime、updUser、updHost、updTime附上相关值
	 */
	/*public static <T> void setCreatAndUpdatInfo(T entity) {
		setCreateInfo(entity);
		setUpdatedInfo(entity);
	}*/

	/**
	 * 快速附上delFlag
	 */
	public static <T> void setDelFlag(T entity) {
		// 默认属性
		String[] fields;
		// 默认值
		Object [] value;
		if (ReflectionUtils.hasField(entity,DELFLAG)&& ReflectionUtils.invokeGetter(entity,DELFLAG) == null) {
			fields = new String[]{DELFLAG};
			value = new Object []{Short.valueOf("0")};
		} else {
			return;
		}
		// 填充默认属性值
		setDefaultValues(entity, fields, value);
	}

	/**
	 * 快速将bean的crtUser、crtHost、crtTime附上相关值
	 */
	/*public static <T> void setCreateInfo(T entity){
        // 默认属性
        String[] fields;
        // 默认值
        Object [] value;
	    if (ReflectionUtils.hasField(entity,CREATEUSER)&& ReflectionUtils.invokeGetter(entity,CREATEUSER) == null) {
            Integer userId = LoginInfoUtil.getLoginUserId();
            fields = new String[]{CREATEUSER,CREATETIME,DELFLAG};
            value = new Object []{String.valueOf(userId),new Date(),Short.valueOf("0")};
        } else {
            fields = new String[]{CREATETIME,DELFLAG};
            value = new Object []{new Date(),Short.valueOf("0")};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
	}*/

	/**
	 * 快速将bean的updUser、updHost、updTime附上相关值
	 *
	 * @param entity 实体bean
	 */
	/*public static <T> void setUpdatedInfo(T entity){
		// 默认属性
		String[] fields;
		// 默认值
		Object [] value;
        if (ReflectionUtils.hasField(entity,MODIFYUSER)&& ReflectionUtils.invokeGetter(entity,MODIFYUSER) == null) {
            Integer userId = LoginInfoUtil.getLoginUserId();
            fields = new String[]{MODIFYUSER,MODIFYTIME};;
            value = new Object []{String.valueOf(userId),new Date()};
        } else {
            fields = new String[]{MODIFYTIME};
            value = new Object []{new Date()};
        }
		// 填充默认属性值
		setDefaultValues(entity, fields, value);
	}*/
	/**
	 * 依据对象的属性数组和值数组对对象的属性进行赋值
	 *
	 * @param entity 对象
	 * @param fields 属性数组
	 * @param value 值数组
	 */
	private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
		for(int i=0;i<fields.length;i++){
			String field = fields[i];
			if(ReflectionUtils.hasField(entity, field)){
				ReflectionUtils.invokeSetter(entity, field, value[i]);
			}
		}
	}
	/**
	 * 根据主键属性，判断主键是否值为空
	 *
	 * @param entity
	 * @param field
	 * @return 主键为空，则返回false；主键有值，返回true
	 */
	public static <T> boolean isPKNotNull(T entity,String field){
		if(!ReflectionUtils.hasField(entity, field)) {
			return false;
		}
		Object value = ReflectionUtils.getFieldValue(entity, field);
		return value!=null&&!"".equals(value);
	}
}
