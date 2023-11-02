package cn.master.gallywix.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.TableConfig;
import com.mybatisflex.codegen.dialect.IDialect;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.codegen.Generator;

import javax.sql.DataSource;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
public class Codegen {
    public static void main(String[] args) {
        Generator generator = new Generator(dataSource(), createGlobalConfigUseStyle(), IDialect.MYSQL);
        generator.generate();
    }
    private static DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/gallywix?characterEncoding=UTF-8&useInformationSchema=true");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");
        return dataSource;
    }
    public static GlobalConfig createGlobalConfigUseStyle() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        // 列配置 ColumnConfig
        ColumnConfig logicDeleteColumn = new ColumnConfig();
        logicDeleteColumn.setLogicDelete(true);
        logicDeleteColumn.setColumnName("del_flag");

        ColumnConfig idConfig = new ColumnConfig();
        idConfig.setColumnName("id");
        idConfig.setKeyType(KeyType.Generator);
        idConfig.setKeyValue(KeyGenerators.flexId);

        TableConfig tableConfig = new TableConfig();
        tableConfig.setTableName("tb_system_workspace");
        tableConfig.setColumnConfig(logicDeleteColumn);
        tableConfig.setColumnConfig(idConfig);

        // 设置根包
        globalConfig.getPackageConfig()
                .setBasePackage("cn.master.gallywix")
                .setSourceDir(System.getProperty("user.dir") + "/backend/src/main/java")
                .setMapperXmlPath(System.getProperty("user.dir") + "/backend/src/main/resources/mappers");
        globalConfig.getStrategyConfig().setTablePrefix("tb_");
        globalConfig.setTableConfig(tableConfig);
        // 注释配置 JavadocConfig
        globalConfig.getJavadocConfig().setAuthor("11's papa").setSince("1.0.0");
        // 设置生成 entity 并启用 Lombok
        globalConfig.enableEntity().setWithLombok(true).setOverwriteEnable(false);
        globalConfig.enableController();
        globalConfig.enableService().setClassPrefix("I");
        globalConfig.enableServiceImpl();
        // 设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();
        return globalConfig;
    }

}
