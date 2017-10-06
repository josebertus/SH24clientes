package com.servihogar.custom.general.api.service;

import com.csi_ti.itaca.architecture.tools.api.ItacaAPIResponse;
import com.servihogar.custom.general.api.model.Generico0DTO;

public interface GeneralBusinessServiceProxy {
	
	ItacaAPIResponse<Generico0DTO> ejecutaPAC(String pac, String function, boolean tratarMensajes, Object... parameters);
}
