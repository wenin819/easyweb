<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${basePackageName}.${moduleName}.dao.${table.className}Dao">
<#macro result4Map javaName jdbcName jdbcType >
        <result property="${javaName}" column="${jdbcName}" jdbcType="${jdbcType}" />
</#macro>
    <resultMap id="BaseResultMap" type="${table.className}">
<#if table.primaryField?? >
        <id property="${table.primaryField.name}" column="${table.primaryField.collumnName}" jdbcType="${table.primaryField.jdbcTypeName}" />
</#if>
<#list table.fieldListWithoutKey as tf>
        <@result4Map javaName="${tf.name}" jdbcName="${tf.collumnName}" jdbcType="${tf.jdbcTypeName}" />
</#list>
    </resultMap>

    <sql id="BaseColumnList">
    <#assign i = 0 />
        <#list table.fieldList as tf><#if i != 0>,</#if><#assign i = i + 1/><#if i == 6><#assign i = 1/>
        </#if>${tf.collumnName}</#list>
    </sql>

    <sql id="BaseColumnValueList">
    <#assign i = 0 />
        <#list table.fieldList as tf><#if i != 0>,</#if><#assign i = i + 1/><#if i == 6><#assign i = 1/>
        </#if>${"#\{"}${tf.name}${"}"}</#list>
    </sql>

    <sql id="TableClause"> sys_user </sql>

    <sql id="WhereClause4Id">
        <where>
        <#if table.primaryField?? >
            ${table.primaryField.collumnName} = ${"#\{"}${table.primaryField.name}${"}"}
        </#if>
        </where>
    </sql>

    <insert id="insert" parameterType="${table.className}">
        insert into <include refid="TableClause" /> (
        <include refid="BaseColumnList" />
        ) values (
        <include refid="BaseColumnValueList" />
        )
    </insert>

    <select id="queryById" parameterType="string" resultMap="BaseResultMap">
        <include refid="public.SelectById" />
    </select>

    <update id="updateById" parameterType="${table.className}">
    <#assign i = 0 />
        update <include refid="TableClause" />
        <set><#list table.fieldListWithoutKey as tf><#if i != 0>,</#if><#assign i = i + 1/>
            ${tf.collumnName}=${"#\{"}${tf.name}${"}"}</#list>
        </set> <include refid="WhereClause4Id" />
    </update>

    <update id="deleteById" parameterType="string">
        <include refid="public.DeleteById" />
    </update>

    <select id="queryByCriteria" resultMap="BaseResultMap" parameterType="CriteriaQuery">
        <include refid="public.Select4Query" />
    </select>

    <select id="countByCriteria" resultType="_int" parameterType="CriteriaQuery">
        <include refid="public.Count4Query" />
    </select>

    <select id="deleteByCriteria" resultType="_int" parameterType="CriteriaQuery">
        <include refid="public.Count4Query" />
    </select>
</mapper>
