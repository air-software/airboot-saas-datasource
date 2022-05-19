package com.airboot.common.core.utils.sql;

import com.airboot.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * sql操作工具类
 *
 * @author airboot
 */
@Slf4j
public class SqlUtil {
    
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";
    
    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }
    
    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }
    
    /**
     * 创建Schema
     */
    public static void createSchema(String jdbcUrl, String username, String password, String schemaName) throws Exception {
        String sql = "create schema if not exists " + schemaName + " default charset utf8mb4 collate utf8mb4_general_ci";
        executeSql(jdbcUrl, username, password, sql);
    }
    
    /**
     * 执行SQL语句
     */
    public static void executeSql(String jdbcUrl, String username, String password, String sql) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        SqlRunner runner = new SqlRunner(conn);
        try {
            log.info("---准备执行SQL语句, sql={}, jdbcUrl={}---", sql, jdbcUrl);
            runner.run(sql);
            log.info("---执行SQL语句成功！sql={}, jdbcUrl={}---", sql, jdbcUrl);
        } catch (Exception e) {
            log.error("---执行SQL语句异常, jdbcUrl={}, sql={}---", jdbcUrl, sql, e);
            throw new Exception("---执行SQL语句异常---", e);
        } finally {
            conn.close();
        }
    }
    
    /**
     * 执行SQL脚本
     */
    public static void executeScript(String jdbcUrl, String username, String password, String filename) throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        ScriptRunner runner = new ScriptRunner(conn);
        try {
            runner.setStopOnError(true);
            String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "airboot-server" + System.getProperty("file.separator") + "sql" + System.getProperty("file.separator") + filename;
            log.info("---准备执行SQL脚本, 脚本路径={}, jdbcUrl={}---", filePath, jdbcUrl);
            runner.runScript(new FileReader(filePath));
            log.info("---执行SQL脚本成功！脚本路径={}, jdbcUrl={}---", filePath, jdbcUrl);
        } catch (Exception e) {
            log.error("---执行SQL脚本异常, jdbcUrl={}, filename={}---", jdbcUrl, filename, e);
            throw new Exception("---执行SQL脚本异常---", e);
        } finally {
            conn.close();
        }
    }
    
}
