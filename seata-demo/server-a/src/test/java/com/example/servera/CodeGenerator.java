package com.example.servera;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @ClassName CodeGenerator
 * @Description 代码生成工具
 
 * @DATE 2022/6/11 16:35
 * @VERSION V1.1.0
 ***/
public class CodeGenerator {
	public static void main(String[] args) {
      builder0();
	}



	public static void builder0() {
		FastAutoGenerator.create("jdbc:mysql://10.100.23.190:3306/learning?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false",
				"root", "123456")
				.globalConfig(builder -> {
					builder.author("lwz") // 设置作者
							.enableSwagger() // 开启 swagger 模式
							.fileOverride() // 覆盖已生成文件
							.outputDir("D:\\home\\"); // 指定输出目录
				})
				.packageConfig(builder -> {
					builder.parent("com.example") // 设置父包名
							.moduleName("api") // 设置父包模块名
							.mapper("dao")
							.entity("domain.entity")
							.controller("controller")
							.service("service")
							.serviceImpl("service.impl")
							.pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\home\\")); // 设置mapperXml生成路径
				})
				.strategyConfig(builder -> {
					builder.addInclude("account")// 设置需要生成的表名
//							.addTablePrefix("statistics")// 设置过滤表前缀
					        .entityBuilder()
							.logicDeleteColumnName("is_delete")
							.controllerBuilder();
				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
}
