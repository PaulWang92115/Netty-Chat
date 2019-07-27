package com.paul.framework;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * BaseMaper用来整合tkmybatis的mapper接口和mysqlmapper接口
 * @author paul.wang
 *
 */

public interface BaseMapper<T> extends Mapper<T>,MySqlMapper<T>{

}
