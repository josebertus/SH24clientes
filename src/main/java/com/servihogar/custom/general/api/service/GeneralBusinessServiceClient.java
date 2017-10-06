package com.servihogar.custom.general.api.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.csi_ti.itaca.architecture.tools.api.ItacaAPIResponse;
import com.csi_ti.itaca.architecture.tools.api.UnexpectedNotSuccessException;
import com.servihogar.custom.general.api.model.Generico0;
import com.servihogar.custom.general.api.model.Generico0DTO;

public class GeneralBusinessServiceClient implements GeneralBusinessService {

	@Autowired
	private GeneralBusinessServiceProxy proxy;	
	
	@Override
	public Generico0 ejecutaPAC(String pac, String function, boolean tratarMensajes, Object... parameters) {
		ItacaAPIResponse<Generico0DTO> response = proxy.ejecutaPAC(pac, function, tratarMensajes, parameters);
		if(response.isSuccess()){
			return response.getResponse();
		}else{
			throw new UnexpectedNotSuccessException();
		}
	}

}

