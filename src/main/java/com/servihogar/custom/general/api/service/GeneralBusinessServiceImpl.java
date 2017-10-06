package com.servihogar.custom.general.api.service;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.servihogar.custom.general.api.model.Generico0;
import com.servihogar.custom.general.server.jdbc.util.ConversionUtil;


public class GeneralBusinessServiceImpl implements GeneralBusinessService {

	public Connection conn;
	ConversionUtil convert = new ConversionUtil();
	
	@Autowired
	@Qualifier("plsqlDataSource")
	public DataSource plsqlDataSource;

	@Override
	public Generico0 ejecutaPAC(String pac, String function, boolean tratarMensajes, Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}
