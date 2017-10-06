package com.servihogar.custom.general.api.service;

import com.servihogar.custom.general.api.model.Generico0;

public interface GeneralBusinessService {
	
	Generico0 ejecutaPAC(String pac, String function, boolean tratarMensajes, Object... parameters);
}
