package cn.roothub.bbs.common.dao.builder;

import cn.roothub.bbs.common.dao.injector.DefaultSqlInjector;
import cn.roothub.bbs.common.dao.injector.ISqlInjector;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.session.Configuration;

/**
 * BaseMapper 注解构建器，它的主要职责是：
 * 1.初始化 MapperBuilderAssistant
 * 2.调用 ISqlInjector.inspectInject() 方法注入通用的 SQL
 * @Author: miansen.wang
 * @Date: 2019/8/31 16:31
 */
public class BaseMapperAnnotationBuilder extends MapperAnnotationBuilder{

    /**
     * Mybatis 的配置信息，存储了所有 Mapper 注册与绑定的信息
     */
    private final Configuration configuration;

    /**
     * Mapper 构建助手，将 Mapper 节点信息封装成 MappedStatement 添加到 Configuration 的 mappedStatements 中
     */
    private final MapperBuilderAssistant assistant;

    /**
     *  Mapper 接口
     */
    private final Class<?> mapperClass;

    public BaseMapperAnnotationBuilder(Configuration configuration, Class<?> mapperClass) {
        super(configuration, mapperClass);
        String resource = mapperClass.getName().replace('.', '/') + ".java (best guess)";
        this.configuration = configuration;
        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.mapperClass = mapperClass;
        // 设置当前 Mapper 的全局命名空间
        this.assistant.setCurrentNamespace(mapperClass.getName());
    }

    @Override
    public void parse() {
        ISqlInjector sqlInjector = new DefaultSqlInjector();
        sqlInjector.inspectInject(this.assistant,mapperClass);
    }
}
